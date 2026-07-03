package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Login : Screen()
    object Register : Screen()
    object ForgotPassword : Screen()
    object Dashboard : Screen()
    data class ProductDetail(val productId: Int) : Screen()
    object Cart : Screen()
    object Wishlist : Screen()
    object Checkout : Screen()
    object OrderHistory : Screen()
    object Profile : Screen()
    object SellerDashboard : Screen()
    object ManageProducts : Screen()
    data class AddEditProduct(val productId: Int? = null) : Screen()
    object Notifications : Screen()
    object AdminDashboard : Screen()
}

class LiduanViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AppRepository

    // Screen State and Navigation Stack
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Login)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()
    private val screenStack = mutableListOf<Screen>()

    // Current logged-in user state
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    // Database flows mapped to lists
    private val _allProducts = MutableStateFlow<List<ProductEntity>>(emptyList())
    val allProducts: StateFlow<List<ProductEntity>> = _allProducts.asStateFlow()

    private val _allUsers = MutableStateFlow<List<UserEntity>>(emptyList())
    val allUsers: StateFlow<List<UserEntity>> = _allUsers.asStateFlow()

    private val _allOrders = MutableStateFlow<List<OrderEntity>>(emptyList())
    val allOrders: StateFlow<List<OrderEntity>> = _allOrders.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItemEntity>>(emptyList())
    val cartItems: StateFlow<List<CartItemEntity>> = _cartItems.asStateFlow()

    private val _wishlistItems = MutableStateFlow<List<WishlistItemEntity>>(emptyList())
    val wishlistItems: StateFlow<List<WishlistItemEntity>> = _wishlistItems.asStateFlow()

    private val _userOrders = MutableStateFlow<List<OrderEntity>>(emptyList())
    val userOrders: StateFlow<List<OrderEntity>> = _userOrders.asStateFlow()

    private val _userNotifications = MutableStateFlow<List<NotificationEntity>>(emptyList())
    val userNotifications: StateFlow<List<NotificationEntity>> = _userNotifications.asStateFlow()

    // Filters and UI parameters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Semua")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Navigation and screen management helpers
    fun navigateTo(screen: Screen) {
        screenStack.add(_currentScreen.value)
        _currentScreen.value = screen
    }

    fun navigateBack() {
        if (screenStack.isNotEmpty()) {
            _currentScreen.value = screenStack.removeAt(screenStack.size - 1)
        } else {
            // Default fallback
            _currentScreen.value = if (_currentUser.value != null) {
                when (_currentUser.value?.role) {
                    "Admin" -> Screen.AdminDashboard
                    "Penjual" -> Screen.SellerDashboard
                    else -> Screen.Dashboard
                }
            } else Screen.Login
        }
    }

    init {
        val database = AppDatabase.getDatabase(application)
        repository = AppRepository(database)

        // Run prepopulate check
        viewModelScope.launch {
            repository.checkAndPrepopulate()
            observeDatabase()
        }
    }

    private fun observeDatabase() {
        viewModelScope.launch {
            // Observe all products
            repository.productDao.getAllProducts().collect { products ->
                _allProducts.value = products
            }
        }

        viewModelScope.launch {
            // Observe all users
            repository.userDao.getAllUsers().collect { users ->
                _allUsers.value = users
            }
        }

        viewModelScope.launch {
            // Observe all orders
            repository.orderDao.getAllOrders().collect { orders ->
                _allOrders.value = orders
            }
        }

        // Whenever currentUser changes, observe user-specific database tables
        viewModelScope.launch {
            _currentUser.collect { user ->
                if (user != null) {
                    // Update cart
                    launch {
                        repository.cartDao.getCartItems(user.id).collect { items ->
                            _cartItems.value = items
                        }
                    }
                    // Update wishlist
                    launch {
                        repository.wishlistDao.getWishlistItems(user.id).collect { items ->
                            _wishlistItems.value = items
                        }
                    }
                    // Update orders
                    launch {
                        repository.orderDao.getOrders(user.id).collect { orders ->
                            _userOrders.value = orders
                        }
                    }
                    // Update notifications
                    launch {
                        repository.notificationDao.getNotifications(user.id).collect { notifications ->
                            _userNotifications.value = notifications
                        }
                    }
                } else {
                    _cartItems.value = emptyList()
                    _wishlistItems.value = emptyList()
                    _userOrders.value = emptyList()
                    _userNotifications.value = emptyList()
                }
            }
        }
    }

    // --- Authentication Actions ---
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _registerError = MutableStateFlow<String?>(null)
    val registerError: StateFlow<String?> = _registerError.asStateFlow()

    private val _forgotPasswordError = MutableStateFlow<String?>(null)
    val forgotPasswordError: StateFlow<String?> = _forgotPasswordError.asStateFlow()

    private val _forgotPasswordSuccess = MutableStateFlow<String?>(null)
    val forgotPasswordSuccess: StateFlow<String?> = _forgotPasswordSuccess.asStateFlow()

    fun clearErrors() {
        _loginError.value = null
        _registerError.value = null
        _forgotPasswordError.value = null
        _forgotPasswordSuccess.value = null
    }

    fun login(email: String, password: String, rememberMe: Boolean): Boolean {
        clearErrors()
        if (email.isBlank() || password.isBlank()) {
            _loginError.value = "Email dan password wajib diisi."
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginError.value = "Format email tidak valid."
            return false
        }
        if (password.length < 6) {
            _loginError.value = "Password minimal 6 karakter."
            return false
        }

        var success = false
        viewModelScope.launch {
            val user = repository.userDao.getUserByEmail(email)
            if (user != null && user.passwordHash == password) {
                _currentUser.value = user
                _currentScreen.value = when (user.role) {
                    "Admin" -> Screen.AdminDashboard
                    "Penjual" -> Screen.SellerDashboard
                    else -> Screen.Dashboard
                }
                success = true
            } else {
                _loginError.value = "Email atau password salah."
            }
        }
        return success
    }

    fun register(fullName: String, email: String, password: String, confirmPassword: String, role: String) {
        clearErrors()
        if (fullName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _registerError.value = "Semua field wajib diisi."
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _registerError.value = "Format email tidak valid."
            return
        }
        if (password.length < 6) {
            _registerError.value = "Password minimal 6 karakter."
            return
        }
        if (password != confirmPassword) {
            _registerError.value = "Konfirmasi password tidak sesuai."
            return
        }

        viewModelScope.launch {
            val existing = repository.userDao.getUserByEmail(email)
            if (existing != null) {
                _registerError.value = "Email sudah terdaftar."
                return@launch
            }

            val newUser = UserEntity(
                fullName = fullName,
                email = email,
                passwordHash = password,
                role = role,
                city = "Yogyakarta", // Default
                phone = "0812345678" // Default
            )
            repository.userDao.insertUser(newUser)
            _loginError.value = "Registrasi sukses! Silakan login."
            _currentScreen.value = Screen.Login
        }
    }

    fun resetPassword(email: String, phone: String, newPasswordHash: String) {
        clearErrors()
        if (email.isBlank() || phone.isBlank() || newPasswordHash.isBlank()) {
            _forgotPasswordError.value = "Semua field wajib diisi."
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _forgotPasswordError.value = "Format email tidak valid."
            return
        }
        if (newPasswordHash.length < 6) {
            _forgotPasswordError.value = "Password baru minimal 6 karakter."
            return
        }

        viewModelScope.launch {
            val user = repository.userDao.getUserByEmail(email)
            if (user == null) {
                _forgotPasswordError.value = "Email tidak terdaftar."
            } else if (user.phone.trim() != phone.trim()) {
                _forgotPasswordError.value = "Nomor telepon salah. Pastikan sesuai profil."
            } else {
                repository.userDao.updateUser(user.copy(passwordHash = newPasswordHash))
                _forgotPasswordSuccess.value = "Password berhasil diubah! Silakan login."
                _loginError.value = "Password berhasil diubah! Silakan login."
                _currentScreen.value = Screen.Login
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _currentScreen.value = Screen.Login
        screenStack.clear()
    }

    // --- Filter Actions ---
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    // --- Product Actions ---
    fun saveProduct(productId: Int?, name: String, price: Double, category: String, stock: Int, description: String, imageUrl: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val img = if (imageUrl.isBlank()) "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500" else imageUrl
            if (productId == null) {
                val newProduct = ProductEntity(
                    name = name,
                    price = price,
                    category = category,
                    stock = stock,
                    description = description,
                    imageUrl = img,
                    sellerName = user.fullName,
                    sellerId = user.id
                )
                repository.productDao.insertProduct(newProduct)

                // Push notify to system about promo
                repository.notificationDao.insertNotification(
                    NotificationEntity(
                        title = "Produk Baru Ditambahkan!",
                        message = "${user.fullName} baru saja memposting produk: $name dalam kategori $category.",
                        type = "Promo",
                        userId = user.id
                    )
                )
            } else {
                val existing = repository.productDao.getProductByIdOneShot(productId) ?: return@launch
                val updated = existing.copy(
                    name = name,
                    price = price,
                    category = category,
                    stock = stock,
                    description = description,
                    imageUrl = img
                )
                repository.productDao.updateProduct(updated)
            }
            _currentScreen.value = Screen.ManageProducts
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            repository.productDao.deleteProductById(productId)
        }
    }

    // --- Wishlist Actions ---
    fun toggleWishlist(productId: Int) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val existing = repository.wishlistDao.getWishlistItem(productId, user.id)
            if (existing != null) {
                repository.wishlistDao.deleteWishlistItem(productId, user.id)
            } else {
                repository.wishlistDao.insertWishlistItem(
                    WishlistItemEntity(productId = productId, userId = user.id)
                )
            }
        }
    }

    fun isProductWishlisted(productId: Int): Boolean {
        return _wishlistItems.value.any { it.productId == productId }
    }

    // --- Cart Actions ---
    fun addToCart(productId: Int, quantity: Int = 1) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val product = repository.productDao.getProductByIdOneShot(productId) ?: return@launch
            if (product.stock <= 0) return@launch

            val existing = repository.cartDao.getCartItemByProduct(productId, user.id)
            if (existing != null) {
                val newQty = (existing.quantity + quantity).coerceAtMost(product.stock)
                repository.cartDao.updateCartItem(existing.copy(quantity = newQty))
            } else {
                repository.cartDao.insertCartItem(
                    CartItemEntity(productId = productId, quantity = quantity, userId = user.id)
                )
            }
        }
    }

    fun updateCartItemQuantity(productId: Int, quantity: Int) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val product = repository.productDao.getProductByIdOneShot(productId) ?: return@launch
            val existing = repository.cartDao.getCartItemByProduct(productId, user.id) ?: return@launch
            val finalQty = quantity.coerceIn(1, product.stock)
            repository.cartDao.updateCartItem(existing.copy(quantity = finalQty))
        }
    }

    fun removeCartItem(productId: Int) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val existing = repository.cartDao.getCartItemByProduct(productId, user.id)
            if (existing != null) {
                repository.cartDao.deleteCartItem(existing)
            }
        }
    }

    fun toggleCartItemSelection(productId: Int) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val existing = repository.cartDao.getCartItemByProduct(productId, user.id)
            if (existing != null) {
                repository.cartDao.updateCartItem(existing.copy(isSelected = !existing.isSelected))
            }
        }
    }

    // --- Checkout & Ordering ---
    fun checkout(paymentMethod: String, shippingAddress: String) {
        val user = _currentUser.value ?: return
        val currentCart = _cartItems.value.filter { it.isSelected }
        if (currentCart.isEmpty()) return

        viewModelScope.launch {
            var orderNames = ""
            var totalAmount = 0.0

            for (item in currentCart) {
                val product = repository.productDao.getProductByIdOneShot(item.productId)
                if (product != null) {
                    val finalQty = item.quantity.coerceAtMost(product.stock)
                    if (finalQty <= 0) continue

                    // Deduct stock
                    repository.productDao.updateProduct(product.copy(stock = product.stock - finalQty))

                    val itemDesc = "${product.name} x$finalQty"
                    orderNames = if (orderNames.isEmpty()) itemDesc else "$orderNames, $itemDesc"
                    totalAmount += product.price * finalQty

                    // Send notification to seller about sold product
                    val sellerUser = repository.userDao.getUserByEmail("andi@mahasiswa.ac.id") // Default seller for demo
                    val notifySellerId = if (product.sellerId > 0) product.sellerId else (sellerUser?.id ?: 0)
                    if (notifySellerId > 0) {
                        repository.notificationDao.insertNotification(
                            NotificationEntity(
                                title = "Produk Terjual!",
                                message = "Hore! Produk Anda '${product.name}' dibeli sebanyak $finalQty buah oleh ${user.fullName}.",
                                type = "Sold",
                                userId = notifySellerId
                            )
                        )
                    }

                    // Remove from cart
                    repository.cartDao.deleteCartItem(item)
                }
            }

            if (orderNames.isNotEmpty()) {
                // Insert order
                val newOrder = OrderEntity(
                    productNames = orderNames,
                    totalAmount = totalAmount,
                    paymentMethod = paymentMethod,
                    shippingAddress = shippingAddress,
                    status = "Diproses", // Default start state
                    userId = user.id
                )
                repository.orderDao.insertOrder(newOrder)

                // Insert buyer notification
                repository.notificationDao.insertNotification(
                    NotificationEntity(
                        title = "Pesanan Berhasil Dibuat",
                        message = "Pesanan Anda senilai Rp${String.format("%,.0f", totalAmount).replace(',', '.')} sedang diproses penjual.",
                        type = "Order",
                        userId = user.id
                    )
                )
            }

            _currentScreen.value = Screen.OrderHistory
        }
    }

    // --- Profile management ---
    fun updateProfile(fullName: String, phone: String, city: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val updated = user.copy(fullName = fullName, phone = phone, city = city)
            repository.userDao.updateUser(updated)
            _currentUser.value = updated
        }
    }

    fun changePassword(newPassword: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val updated = user.copy(passwordHash = newPassword)
            repository.userDao.updateUser(updated)
            _currentUser.value = updated
        }
    }

    // --- Notifications actions ---
    fun clearAllNotifications() {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            repository.notificationDao.clearNotifications(user.id)
        }
    }

    // --- Admin Actions ---
    fun deleteUserByAdmin(userId: Int) {
        viewModelScope.launch {
            repository.userDao.deleteUserById(userId)
        }
    }

    fun updateUserRoleByAdmin(userId: Int, newRole: String) {
        viewModelScope.launch {
            repository.userDao.getUserById(userId).first()?.let { user ->
                repository.userDao.updateUser(user.copy(role = newRole))
            }
        }
    }

    fun deleteProductByAdmin(productId: Int) {
        viewModelScope.launch {
            repository.productDao.deleteProductById(productId)
        }
    }

    fun updateOrderStatusByAdmin(orderId: Int, newStatus: String) {
        viewModelScope.launch {
            _allOrders.value.find { it.id == orderId }?.let { order ->
                repository.orderDao.updateOrder(order.copy(status = newStatus))
            }
        }
    }
}

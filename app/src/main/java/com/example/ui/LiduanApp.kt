package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiduanApp(viewModel: LiduanViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val userNotifications by viewModel.userNotifications.collectAsState()

    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            if (currentUser != null &&
                currentScreen != Screen.Login &&
                currentScreen != Screen.Register &&
                currentScreen !is Screen.ProductDetail &&
                currentScreen != Screen.Checkout
            ) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    val role = currentUser?.role ?: "Pembeli"
                    if (role == "Admin") {
                        NavigationBarItem(
                            selected = currentScreen == Screen.AdminDashboard,
                            onClick = { viewModel.navigateTo(Screen.AdminDashboard) },
                            icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Panel Admin") },
                            label = { Text("Panel Admin", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Green600,
                                selectedTextColor = Green600,
                                indicatorColor = Green50
                            )
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.Profile,
                            onClick = { viewModel.navigateTo(Screen.Profile) },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                            label = { Text("Profil", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Green600,
                                selectedTextColor = Green600,
                                indicatorColor = Green50
                            )
                        )
                    } else if (role == "Penjual") {
                        NavigationBarItem(
                            selected = currentScreen == Screen.SellerDashboard,
                            onClick = { viewModel.navigateTo(Screen.SellerDashboard) },
                            icon = { Icon(Icons.Default.TrendingUp, contentDescription = "Dashboard") },
                            label = { Text("Dashboard", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Green600,
                                selectedTextColor = Green600,
                                indicatorColor = Green50
                            )
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.ManageProducts,
                            onClick = { viewModel.navigateTo(Screen.ManageProducts) },
                            icon = { Icon(Icons.Default.Storefront, contentDescription = "Kelola Produk") },
                            label = { Text("Produk", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Green600,
                                selectedTextColor = Green600,
                                indicatorColor = Green50
                            )
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.Notifications,
                            onClick = { viewModel.navigateTo(Screen.Notifications) },
                            icon = {
                                BadgedBox(badge = {
                                    if (userNotifications.isNotEmpty()) {
                                        Badge { Text(userNotifications.size.toString()) }
                                    }
                                }) {
                                    Icon(Icons.Default.Notifications, contentDescription = "Notifikasi")
                                }
                            },
                            label = { Text("Notifikasi", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Green600,
                                selectedTextColor = Green600,
                                indicatorColor = Green50
                            )
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.Profile,
                            onClick = { viewModel.navigateTo(Screen.Profile) },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                            label = { Text("Profil", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Green600,
                                selectedTextColor = Green600,
                                indicatorColor = Green50
                            )
                        )
                    } else {
                        // Buyer Role Bottom Navigation
                        NavigationBarItem(
                            selected = currentScreen == Screen.Dashboard,
                            onClick = { viewModel.navigateTo(Screen.Dashboard) },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
                            label = { Text("Home", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Green600,
                                selectedTextColor = Green600,
                                indicatorColor = Green50
                            )
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.Wishlist,
                            onClick = { viewModel.navigateTo(Screen.Wishlist) },
                            icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorit") },
                            label = { Text("Favorit", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Green600,
                                selectedTextColor = Green600,
                                indicatorColor = Green50
                            )
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.Cart,
                            onClick = { viewModel.navigateTo(Screen.Cart) },
                            icon = {
                                BadgedBox(badge = {
                                    if (cartItems.isNotEmpty()) {
                                        Badge { Text(cartItems.size.toString()) }
                                    }
                                }) {
                                    Icon(Icons.Default.ShoppingCart, contentDescription = "Keranjang")
                                }
                            },
                            label = { Text("Keranjang", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Green600,
                                selectedTextColor = Green600,
                                indicatorColor = Green50
                            )
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.OrderHistory,
                            onClick = { viewModel.navigateTo(Screen.OrderHistory) },
                            icon = { Icon(Icons.AutoMirrored.Outlined.ReceiptLong, contentDescription = "Pesanan") },
                            label = { Text("Pesanan", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Green600,
                                selectedTextColor = Green600,
                                indicatorColor = Green50
                            )
                        )
                        NavigationBarItem(
                            selected = currentScreen == Screen.Profile,
                            onClick = { viewModel.navigateTo(Screen.Profile) },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
                            label = { Text("Profil", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Green600,
                                selectedTextColor = Green600,
                                indicatorColor = Green50
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                },
                label = "ScreenTransition"
            ) { target ->
                when (target) {
                    is Screen.Login -> LoginScreen(viewModel)
                    is Screen.Register -> RegisterScreen(viewModel)
                    is Screen.ForgotPassword -> ForgotPasswordScreen(viewModel)
                    is Screen.Dashboard -> DashboardScreen(viewModel)
                    is Screen.ProductDetail -> ProductDetailScreen(viewModel, target.productId)
                    is Screen.Cart -> CartScreen(viewModel)
                    is Screen.Wishlist -> WishlistScreen(viewModel)
                    is Screen.Checkout -> CheckoutScreen(viewModel)
                    is Screen.OrderHistory -> OrderHistoryScreen(viewModel)
                    is Screen.Profile -> ProfileScreen(viewModel)
                    is Screen.SellerDashboard -> SellerDashboardScreen(viewModel)
                    is Screen.ManageProducts -> ManageProductsScreen(viewModel)
                    is Screen.AddEditProduct -> AddEditProductScreen(viewModel, target.productId)
                    is Screen.Notifications -> NotificationsScreen(viewModel)
                    is Screen.AdminDashboard -> AdminDashboardScreen(viewModel)
                }
            }
        }
    }
}

// ==================== 1. LOGIN SCREEN ====================
@Composable
fun LoginScreen(viewModel: LiduanViewModel) {
    var email by remember { mutableStateOf("budi@mahasiswa.ac.id") }
    var password by remember { mutableStateOf("password123") }
    var rememberMe by remember { mutableStateOf(true) }
    var showPassword by remember { mutableStateOf(false) }

    val error by viewModel.loginError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo Icon and Brand
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Green500, Green700)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Storefront,
                contentDescription = "LiduaN Logo",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "LiduaN",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Green600
        )
        Text(
            text = "E-Commerce Mahasiswa Terpadu",
            fontSize = 14.sp,
            color = Gray500,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.dp, Gray200)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Selamat Datang!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900
                )
                Text(
                    text = "Silakan login untuk mulai bertransaksi di kampus",
                    fontSize = 12.sp,
                    color = Gray500
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Email field
                Text("Email Mahasiswa", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Gray700)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("contoh@mahasiswa.ac.id", color = Gray300) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_email_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green600,
                        unfocusedBorderColor = Gray300
                    ),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                Text("Password", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Gray700)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Minimal 6 karakter", color = Gray300) },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_password_input"),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showPassword) "Sembunyikan password" else "Tampilkan password",
                                tint = Gray500
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green600,
                        unfocusedBorderColor = Gray300
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Remember Me and Lupa Password
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { rememberMe = !rememberMe }
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(checkedColor = Green600)
                        )
                        Text("Remember Me", fontSize = 12.sp, color = Gray700)
                    }

                    Text(
                        text = "Lupa Password?",
                        fontSize = 12.sp,
                        color = Green600,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable {
                                viewModel.clearErrors()
                                viewModel.navigateTo(Screen.ForgotPassword)
                            }
                            .testTag("login_forgot_password_link")
                    )
                }

                if (error != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = error!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Submit button
                Button(
                    onClick = { viewModel.login(email, password, rememberMe) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("login_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Green600),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Masuk Sekarang", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Demo Accounts Shortcut Info Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Green50),
            border = BorderStroke(1.dp, Green100)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("💡 Akun Simulasi Presentasi:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Green700)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("1. Pembeli", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Gray900)
                        Text("budi@mahasiswa.ac.id", fontSize = 8.sp, color = Gray700)
                        Text("pass: password123", fontSize = 8.sp, color = Gray700)
                        Button(
                            onClick = {
                                email = "budi@mahasiswa.ac.id"
                                password = "password123"
                            },
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                            modifier = Modifier.height(24.dp).padding(top = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green600)
                        ) {
                            Text("Set Pembeli", fontSize = 8.sp, color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("2. Penjual", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Gray900)
                        Text("andi@mahasiswa.ac.id", fontSize = 8.sp, color = Gray700)
                        Text("pass: password123", fontSize = 8.sp, color = Gray700)
                        Button(
                            onClick = {
                                email = "andi@mahasiswa.ac.id"
                                password = "password123"
                            },
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                            modifier = Modifier.height(24.dp).padding(top = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green600)
                        ) {
                            Text("Set Penjual", fontSize = 8.sp, color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("3. Admin", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Gray900)
                        Text("admin@mahasiswa.ac.id", fontSize = 8.sp, color = Gray700)
                        Text("pass: admin123", fontSize = 8.sp, color = Gray700)
                        Button(
                            onClick = {
                                email = "admin@mahasiswa.ac.id"
                                password = "admin123"
                            },
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
                            modifier = Modifier.height(24.dp).padding(top = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green600)
                        ) {
                            Text("Set Admin", fontSize = 8.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Link to Register
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Belum punya akun? ", fontSize = 13.sp, color = Gray500)
            Text(
                text = "Daftar di sini",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Green600,
                modifier = Modifier
                    .clickable {
                        viewModel.clearErrors()
                        viewModel.navigateTo(Screen.Register)
                    }
                    .testTag("to_register_link")
            )
        }
    }
}

// ==================== 1B. FORGOT PASSWORD SCREEN ====================
@Composable
fun ForgotPasswordScreen(viewModel: LiduanViewModel) {
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    val error by viewModel.forgotPasswordError.collectAsState()
    val success by viewModel.forgotPasswordSuccess.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo / Icon
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(Green500, Green700)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock Icon",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Lupa Password",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Green600
        )
        Text(
            text = "Atur ulang kata sandi akun LiduaN Anda",
            fontSize = 14.sp,
            color = Gray500,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.dp, Gray200)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Ganti Password",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900
                )
                Text(
                    text = "Verifikasi menggunakan email & nomor telepon terdaftar",
                    fontSize = 12.sp,
                    color = Gray500
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Email
                Text("Email Mahasiswa", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Gray700)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("contoh@mahasiswa.ac.id", color = Gray300) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("forgot_email_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green600,
                        unfocusedBorderColor = Gray300
                    ),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Phone
                Text("Nomor Telepon", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Gray700)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = { Text("contoh: 08123456789", color = Gray300) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("forgot_phone_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green600,
                        unfocusedBorderColor = Gray300
                    ),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // New Password
                Text("Password Baru", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Gray700)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    placeholder = { Text("Minimal 6 karakter", color = Gray300) },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("forgot_new_password_input"),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showPassword) "Sembunyikan password" else "Tampilkan password",
                                tint = Gray500
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green600,
                        unfocusedBorderColor = Gray300
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                if (error != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = error!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (success != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = success!!,
                        color = Green600,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.resetPassword(email, phone, newPassword)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("forgot_submit_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Green600),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Reset Password", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Kembali ke ", fontSize = 13.sp, color = Gray500)
            Text(
                text = "Login",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Green600,
                modifier = Modifier
                    .clickable {
                        viewModel.clearErrors()
                        viewModel.navigateTo(Screen.Login)
                    }
                    .testTag("forgot_back_to_login")
            )
        }
    }
}

// ==================== 2. REGISTER SCREEN ====================
@Composable
fun RegisterScreen(viewModel: LiduanViewModel) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Pembeli") } // "Pembeli" or "Penjual"
    var showPassword by remember { mutableStateOf(false) }

    val error by viewModel.registerError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.linearGradient(colors = listOf(Green500, Green700))),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AppRegistration,
                contentDescription = "Register Icon",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Daftar Akun LiduaN",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Gray900
        )
        Text(
            text = "Mulai bergabung dalam ekosistem e-commerce mahasiswa",
            fontSize = 12.sp,
            color = Gray500,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = BorderStroke(1.dp, Gray200)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Full Name
                Text("Nama Lengkap", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Gray700)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = { Text("Nama Lengkap Mahasiswa", color = Gray300) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_fullname_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green600,
                        unfocusedBorderColor = Gray300
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Email
                Text("Email Kampus", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Gray700)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("NIM@mahasiswa.ac.id", color = Gray300) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_email_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green600,
                        unfocusedBorderColor = Gray300
                    ),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Role Options
                Text("Role Akun", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Gray700)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedRole = "Pembeli" },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.5.dp, if (selectedRole == "Pembeli") Green600 else Gray200),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedRole == "Pembeli") Green50 else Color.White
                        )
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().padding(12.dp), contentAlignment = Alignment.Center) {
                            Text("Pembeli", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (selectedRole == "Pembeli") Green700 else Gray700)
                        }
                    }

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedRole = "Penjual" },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.5.dp, if (selectedRole == "Penjual") Green600 else Gray200),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedRole == "Penjual") Green50 else Color.White
                        )
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().padding(12.dp), contentAlignment = Alignment.Center) {
                            Text("Penjual", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (selectedRole == "Penjual") Green700 else Gray700)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Password
                Text("Password", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Gray700)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Minimal 6 karakter", color = Gray300) },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_password_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green600,
                        unfocusedBorderColor = Gray300
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Confirm Password
                Text("Konfirmasi Password", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Gray700)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("Ulangi password", color = Gray300) },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("register_confirm_password_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green600,
                        unfocusedBorderColor = Gray300
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                if (error != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = error!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Register button
                Button(
                    onClick = { viewModel.register(fullName, email, password, confirmPassword, selectedRole) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("register_submit_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Green600),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Daftar Sekarang", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Link to Login
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Sudah punya akun? ", fontSize = 13.sp, color = Gray500)
            Text(
                text = "Login di sini",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Green600,
                modifier = Modifier
                    .clickable {
                        viewModel.clearErrors()
                        viewModel.navigateTo(Screen.Login)
                    }
                    .testTag("to_login_link")
            )
        }
    }
}

// ==================== 3. DASHBOARD UTAMA (BUYER HOME) ====================
@Composable
fun DashboardScreen(viewModel: LiduanViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val products by viewModel.allProducts.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val categories = listOf("Semua", "Buku Kuliah", "Elektronik", "Perlengkapan Kos", "Fashion", "Aksesoris", "Jasa", "Lainnya")

    // Filtered products list
    val filteredProducts = remember(products, searchQuery, selectedCategory) {
        products.filter { p ->
            val matchesSearch = p.name.contains(searchQuery, ignoreCase = true) || p.description.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "Semua" || p.category == selectedCategory
            matchesSearch && matchesCategory
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        // --- 3.1. HEADER ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            tonalElevation = 2.dp,
            shadowElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Logo and Title
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Green600),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Storefront, contentDescription = "Logo", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("LiduaN", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Green600)
                        Text(" Kampus", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Gray500)
                    }

                    // Top Action Icons
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Wishlist Icon
                        IconButton(onClick = { viewModel.navigateTo(Screen.Wishlist) }) {
                            Icon(Icons.Default.FavoriteBorder, contentDescription = "Wishlist", tint = Gray700)
                        }

                        // Cart with Badge
                        IconButton(onClick = { viewModel.navigateTo(Screen.Cart) }) {
                            BadgedBox(badge = {
                                if (cartItems.isNotEmpty()) {
                                    Badge { Text(cartItems.size.toString()) }
                                }
                            }) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Keranjang", tint = Gray700)
                            }
                        }

                        // Notifications with Badge
                        IconButton(onClick = { viewModel.navigateTo(Screen.Notifications) }) {
                            val notifs by viewModel.userNotifications.collectAsState()
                            BadgedBox(badge = {
                                if (notifs.isNotEmpty()) {
                                    Badge { Text(notifs.size.toString()) }
                                }
                            }) {
                                Icon(Icons.Default.Notifications, contentDescription = "Notifikasi", tint = Gray700)
                            }
                        }

                        // Profile Icon
                        IconButton(onClick = { viewModel.navigateTo(Screen.Profile) }) {
                            Icon(Icons.Default.Person, contentDescription = "Profil", tint = Gray700)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Cari buku, elektronik, kost, jasa...", fontSize = 13.sp, color = Gray300) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Gray500) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Gray500)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("dashboard_search_bar"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green500,
                        unfocusedBorderColor = Gray200,
                        focusedContainerColor = Gray50,
                        unfocusedContainerColor = Gray50
                    ),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }
        }

        // --- BODY CONTENT (SCROLLABLE) ---
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // --- 3.2. PROMO BANNER ---
            item {
                Text(
                    text = "Promo & Info Kampus",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    item {
                        PromoBanner(
                            title = "Diskon Buku Kuliah!",
                            subtitle = "Potongan s.d 30% dari kakak tingkat",
                            tag = "Buku",
                            gradient = Brush.linearGradient(colors = listOf(Green600, Green500))
                        )
                    }
                    item {
                        PromoBanner(
                            title = "Sewa Barang Kos Murah",
                            subtitle = "Kasur, rice cooker, kipas bekas maba",
                            tag = "Kos",
                            gradient = Brush.linearGradient(colors = listOf(Color(0xFF3B82F6), Color(0xFF60A5FA)))
                        )
                    }
                    item {
                        PromoBanner(
                            title = "Elektronik Bekas Bergaransi",
                            subtitle = "iPad, Laptop, Kalkulator siap pakai",
                            tag = "Gadget",
                            gradient = Brush.linearGradient(colors = listOf(Color(0xFFF59E0B), Color(0xFFFBBF24)))
                        )
                    }
                }
            }

            // --- 3.3. CATEGORY PRODUCT CHIPS ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Kategori Kebutuhan",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900,
                    modifier = Modifier.padding(start = 16.dp, bottom = 10.dp)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setSelectedCategory(category) },
                            label = { Text(category, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Green600,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = Gray700
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                selectedBorderColor = Green600,
                                borderColor = Gray200
                            )
                        )
                    }
                }
            }

            // --- 3.4. PRODUCT POPULAR HEADER ---
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.isNotEmpty() || selectedCategory != "Semua") "Hasil Pencarian" else "Produk Populer Kampus",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gray900
                    )
                    Text(
                        text = "${filteredProducts.size} item ditemukan",
                        fontSize = 12.sp,
                        color = Gray500
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Check if product list is empty
            if (filteredProducts.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Inventory, contentDescription = "Empty", tint = Gray300, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Produk tidak ditemukan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray700)
                        Text("Coba cari kata kunci lain atau pilih kategori berbeda.", fontSize = 11.sp, color = Gray500, textAlign = TextAlign.Center)
                    }
                }
            } else {
                // Chunk the list into rows of 2 columns
                val chunkedProducts = filteredProducts.chunked(2)
                items(chunkedProducts) { rowItems ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (item in rowItems) {
                            Box(modifier = Modifier.weight(1f).padding(bottom = 10.dp)) {
                                ProductCard(
                                    product = item,
                                    isWishlisted = viewModel.isProductWishlisted(item.id),
                                    onWishlistToggle = { viewModel.toggleWishlist(item.id) },
                                    onAddToCart = { viewModel.addToCart(item.id); println("added ${item.name} to cart") },
                                    onClick = { viewModel.navigateTo(Screen.ProductDetail(item.id)) }
                                )
                            }
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

// --- Promo Banner Component ---
@Composable
fun PromoBanner(title: String, subtitle: String, tag: String, gradient: Brush) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(110.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(tag, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(subtitle, color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp, maxLines = 1)
            }
        }
    }
}

// --- Product Card Component ---
@Composable
fun ProductCard(
    product: ProductEntity,
    isWishlisted: Boolean,
    onWishlistToggle: () -> Unit,
    onAddToCart: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Gray200)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                // Async Product Image
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Category Tag
                Surface(
                    color = Green600,
                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = product.category,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Wishlist Button overlay
                IconButton(
                    onClick = onWishlistToggle,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(32.dp)
                        .background(Color.White.copy(alpha = 0.85f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isWishlisted) Red500 else Gray500,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                // Title
                Text(
                    text = product.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Gray900,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.height(34.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Price
                Text(
                    text = "Rp${String.format("%,.0f", product.price).replace(',', '.')}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Green600
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Seller
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Storefront, contentDescription = "Seller", tint = Gray500, modifier = Modifier.size(11.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = product.sellerName,
                        fontSize = 10.sp,
                        color = Gray500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Rating & Actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = Yellow500, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(product.rating.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Gray700)
                    }

                    // Stock Tag
                    if (product.stock <= 0) {
                        Surface(
                            color = Color(0xFFFEE2E2),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text("Habis", color = Red600, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    } else {
                        // Quick Add to Cart button
                        Button(
                            onClick = onAddToCart,
                            modifier = Modifier
                                .height(26.dp)
                                .testTag("add_to_cart_btn_${product.id}"),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green600),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Beli", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

// ==================== 4. DETAIL PRODUK SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(viewModel: LiduanViewModel, productId: Int) {
    val products by viewModel.allProducts.collectAsState()
    val isWishlisted = viewModel.isProductWishlisted(productId)

    val product = remember(products, productId) {
        products.find { it.id == productId }
    }

    if (product == null) {
        // Fallback or back
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Produk tidak ditemukan")
                Button(onClick = { viewModel.navigateBack() }) {
                    Text("Kembali")
                }
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Toolbar
        TopAppBar(
            title = { Text("Detail Produk", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { viewModel.toggleWishlist(product.id) }) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isWishlisted) Red500 else Gray900
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Main Big Photo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Category overlay tag
                Surface(
                    color = Green600,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = product.category,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // Gallery Thumbnails
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Galeri Produk", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray700)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Render 3 similar thumbnails
                    for (i in 0 until 3) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .border(1.dp, if (i == 0) Green600 else Gray200, RoundedCornerShape(6.dp))
                        ) {
                            AsyncImage(
                                model = product.imageUrl,
                                contentDescription = "Thumb",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            Divider(color = Gray100, thickness = 6.dp)

            // Name and Price Information
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = product.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray900
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rp${String.format("%,.0f", product.price).replace(',', '.')}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Green600
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = "Rating", tint = Yellow500, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(product.rating.toString(), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)
                        Text(" / 5.0", fontSize = 11.sp, color = Gray500)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Stock badge
                    Surface(
                        color = if (product.stock > 0) Green50 else Color(0xFFFEE2E2),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (product.stock > 0) "Tersedia: ${product.stock} stok" else "Stok Habis",
                            color = if (product.stock > 0) Green700 else Red600,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Divider(color = Gray100, thickness = 6.dp)

            // Seller Card Info
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Informasi Penjual", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray500)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Green50),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(product.sellerName.take(2).uppercase(), fontWeight = FontWeight.Bold, color = Green700, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(product.sellerName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = Gray500, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Universitas Terpadu (Jogja)", fontSize = 11.sp, color = Gray500)
                        }
                    }
                    Button(
                        onClick = { /* Handle chat */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Green50),
                        border = BorderStroke(1.dp, Green600),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text("Hubungi", color = Green700, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Divider(color = Gray100, thickness = 6.dp)

            // Description
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Deskripsi Produk", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product.description,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = Gray700
                )
            }
        }

        // Action Bottom Bar
        Surface(
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth(),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Add to Cart
                Button(
                    onClick = {
                        viewModel.addToCart(product.id)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Green50),
                    border = BorderStroke(1.5.dp, Green600),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = "Add Cart", tint = Green700, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("+ Keranjang", color = Green700, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                // Buy Now
                Button(
                    onClick = {
                        viewModel.addToCart(product.id)
                        viewModel.navigateTo(Screen.Cart)
                    },
                    modifier = Modifier
                        .weight(1.2f)
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Green600),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Beli Sekarang", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ==================== 5. KERANJANG (CART) ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: LiduanViewModel) {
    val cartItems by viewModel.cartItems.collectAsState()
    val products by viewModel.allProducts.collectAsState()

    // Map cart items with their corresponding products
    val cartWithProduct = remember(cartItems, products) {
        cartItems.mapNotNull { item ->
            val product = products.find { it.id == item.productId }
            if (product != null) Pair(item, product) else null
        }
    }

    // Subtotal calculations
    val subtotal = remember(cartWithProduct) {
        cartWithProduct
            .filter { it.first.isSelected }
            .sumOf { it.first.quantity * it.second.price }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        TopAppBar(
            title = { Text("Keranjang Belanja", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        if (cartWithProduct.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Empty", tint = Gray200, modifier = Modifier.size(80.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Keranjang Anda kosong", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Gray900)
                Text("Silakan telusuri barang kebutuhan kampus Anda di halaman depan.", fontSize = 12.sp, color = Gray500, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { viewModel.navigateTo(Screen.Dashboard) },
                    colors = ButtonDefaults.buttonColors(containerColor = Green600)
                ) {
                    Text("Mulai Belanja", color = Color.White)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartWithProduct) { pair ->
                    val item = pair.first
                    val product = pair.second

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Gray200)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Checkbox
                            Checkbox(
                                checked = item.isSelected,
                                onCheckedChange = { viewModel.toggleCartItemSelection(product.id) },
                                colors = CheckboxDefaults.colors(checkedColor = Green600)
                            )

                            // Image
                            AsyncImage(
                                model = product.imageUrl,
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            // Details
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(product.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Penjual: ${product.sellerName}", fontSize = 10.sp, color = Gray500)
                                Text("Rp${String.format("%,.0f", product.price).replace(',', '.')}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Green600)

                                Spacer(modifier = Modifier.height(6.dp))

                                // Controls
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // Quantity Controls
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = { viewModel.updateCartItemQuantity(product.id, item.quantity - 1) },
                                            modifier = Modifier.size(24.dp).background(Gray100, CircleShape)
                                        ) {
                                            Icon(Icons.Default.Remove, contentDescription = "Minus", modifier = Modifier.size(12.dp))
                                        }

                                        Text(item.quantity.toString(), modifier = Modifier.padding(horizontal = 10.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)

                                        IconButton(
                                            onClick = { viewModel.updateCartItemQuantity(product.id, item.quantity + 1) },
                                            modifier = Modifier.size(24.dp).background(Gray100, CircleShape)
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = "Plus", modifier = Modifier.size(12.dp))
                                        }
                                    }

                                    // Remove button
                                    IconButton(
                                        onClick = { viewModel.removeCartItem(product.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Red500, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Subtotal Footer
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth(),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Subtotal", fontSize = 13.sp, color = Gray700)
                        Text(
                            text = "Rp${String.format("%,.0f", subtotal).replace(',', '.')}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Gray900
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (subtotal > 0) {
                                viewModel.navigateTo(Screen.Checkout)
                            }
                        },
                        enabled = subtotal > 0,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("checkout_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Green600, disabledContainerColor = Gray300),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Checkout Sekarang", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==================== 6. FAVORIT SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(viewModel: LiduanViewModel) {
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val products by viewModel.allProducts.collectAsState()

    val wishlistedProducts = remember(wishlistItems, products) {
        wishlistItems.mapNotNull { item ->
            products.find { it.id == item.productId }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        TopAppBar(
            title = { Text("Favorit Saya", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        if (wishlistedProducts.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = "Empty Wishlist", tint = Gray200, modifier = Modifier.size(80.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Belum ada produk favorit", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Gray900)
                Text("Ketuk ikon hati pada produk kampus yang Anda minati agar tersimpan di sini.", fontSize = 12.sp, color = Gray500, textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(wishlistedProducts) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(Screen.ProductDetail(product.id)) },
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Gray200)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = product.imageUrl,
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(product.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Rp${String.format("%,.0f", product.price).replace(',', '.')}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Green600)
                                Text("Penjual: ${product.sellerName}", fontSize = 10.sp, color = Gray500)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Add directly to Cart
                                IconButton(onClick = { viewModel.addToCart(product.id) }) {
                                    Icon(Icons.Default.AddShoppingCart, contentDescription = "Add Cart", tint = Green600)
                                }

                                // Delete from Wishlist
                                IconButton(onClick = { viewModel.toggleWishlist(product.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove Favorit", tint = Red500)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== 7. CHECKOUT SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(viewModel: LiduanViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val products by viewModel.allProducts.collectAsState()

    val selectedCart = remember(cartItems, products) {
        cartItems.filter { it.isSelected }.mapNotNull { item ->
            val product = products.find { it.id == item.productId }
            if (product != null) Pair(item, product) else null
        }
    }

    val subtotal = remember(selectedCart) {
        selectedCart.sumOf { it.first.quantity * it.second.price }
    }

    var shippingAddress by remember { mutableStateOf("Kos Melati, Jl. Kaliurang KM 5.5 No. 42, Sleman, DIY") }
    var paymentMethod by remember { mutableStateOf("Transfer Bank") } // "Transfer Bank", "E-Wallet", "COD"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        TopAppBar(
            title = { Text("Checkout Pesanan", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Address Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Gray200)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Alamat Pengiriman Kampus / Kos", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = shippingAddress,
                        onValueChange = { shippingAddress = it },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Green600, unfocusedBorderColor = Gray300),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Order Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Gray200)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Ringkasan Pesanan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)
                    Spacer(modifier = Modifier.height(10.dp))

                    for (pair in selectedCart) {
                        val item = pair.first
                        val product = pair.second
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${product.name} (x${item.quantity})", fontSize = 12.sp, color = Gray700, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("Rp${String.format("%,.0f", product.price * item.quantity).replace(',', '.')}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray900)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Payment Methods
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Gray200)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Metode Pembayaran", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)
                    Spacer(modifier = Modifier.height(10.dp))

                    val methods = listOf("Transfer Bank", "E-Wallet", "COD")
                    for (m in methods) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { paymentMethod = m }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = paymentMethod == m,
                                onClick = { paymentMethod = m },
                                colors = RadioButtonDefaults.colors(selectedColor = Green600)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(m, fontSize = 13.sp, color = Gray900)
                        }
                    }
                }
            }
        }

        // Summary Payment Sticky Footer
        Surface(
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth(),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total Pembayaran", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900)
                    Text(
                        text = "Rp${String.format("%,.0f", subtotal).replace(',', '.')}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Green600
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.checkout(paymentMethod, shippingAddress)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("checkout_order_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = Green600),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Bayar & Pesan", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ==================== 8. RIWAYAT PESANAN SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(viewModel: LiduanViewModel) {
    val orders by viewModel.userOrders.collectAsState()

    var selectedStatusTab by remember { mutableStateOf("Semua") }
    val tabs = listOf("Semua", "Menunggu Pembayaran", "Diproses", "Dikirim", "Selesai")

    val filteredOrders = remember(orders, selectedStatusTab) {
        if (selectedStatusTab == "Semua") {
            orders
        } else {
            orders.filter { it.status.equals(selectedStatusTab, ignoreCase = true) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        TopAppBar(
            title = { Text("Riwayat Pesanan Saya", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { viewModel.navigateTo(Screen.Dashboard) }) {
                    Icon(Icons.Default.Home, contentDescription = "Home")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        // Status filter tabs row
        ScrollableTabRow(
            selectedTabIndex = tabs.indexOf(selectedStatusTab).coerceAtLeast(0),
            containerColor = Color.White,
            contentColor = Green600,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                val index = tabs.indexOf(selectedStatusTab).coerceAtLeast(0)
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
                    color = Green600
                )
            }
        ) {
            tabs.forEach { tab ->
                Tab(
                    selected = selectedStatusTab == tab,
                    onClick = { selectedStatusTab = tab },
                    text = { Text(tab, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    selectedContentColor = Green600,
                    unselectedContentColor = Gray500
                )
            }
        }

        if (filteredOrders.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.AutoMirrored.Outlined.ReceiptLong, contentDescription = "Empty Orders", tint = Gray200, modifier = Modifier.size(80.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Tidak ada pesanan", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Gray900)
                Text("Daftar pesanan dengan status '$selectedStatusTab' kosong.", fontSize = 12.sp, color = Gray500, textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredOrders) { order ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Gray200)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("ID Pesanan: #LDN${order.id}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Gray500)

                                // Status Badge
                                val badgeColor = when (order.status) {
                                    "Menunggu Pembayaran" -> Color(0xFFFEF3C7) // Yellow
                                    "Diproses" -> Color(0xFFDBEAFE) // Blue
                                    "Dikirim" -> Color(0xFFE0F2FE) // Light Blue
                                    "Selesai" -> Color(0xFFD1FADF) // Green
                                    else -> Color(0xFFFEE2E2) // Red / Cancelled
                                }
                                val textColor = when (order.status) {
                                    "Menunggu Pembayaran" -> Yellow500
                                    "Diproses" -> Blue500
                                    "Dikirim" -> Color(0xFF0284C7)
                                    "Selesai" -> Green700
                                    else -> Red600
                                }

                                Surface(
                                    color = badgeColor,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(order.status, color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Divider(color = Gray100)
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(order.productNames, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Metode: ${order.paymentMethod}", fontSize = 11.sp, color = Gray500)
                            Text("Alamat: ${order.shippingAddress}", fontSize = 11.sp, color = Gray500)

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Total Tagihan", fontSize = 12.sp, color = Gray700)
                                Text(
                                    text = "Rp${String.format("%,.0f", order.totalAmount).replace(',', '.')}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Green600
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== 9. USER PROFILE SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: LiduanViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()

    var isEditMode by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(currentUser?.fullName ?: "") }
    var editPhone by remember { mutableStateOf(currentUser?.phone ?: "") }
    var editCity by remember { mutableStateOf(currentUser?.city ?: "") }

    var isChangePasswordMode by remember { mutableStateOf(false) }
    var newPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        TopAppBar(
            title = { Text("Profil Pengguna", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = { viewModel.logout() }) {
                    Icon(Icons.Default.Logout, contentDescription = "Log Out", tint = Red500)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Avatar Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Gray200)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Green50)
                            .border(2.dp, Green600, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (currentUser?.fullName ?: "M").take(2).uppercase(),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = Green700
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(currentUser?.fullName ?: "Mahasiswa", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Gray900)
                    Text(currentUser?.email ?: "maba@mahasiswa.ac.id", fontSize = 13.sp, color = Gray500)

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        color = Green50,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "Role: ${currentUser?.role ?: "Pembeli"}",
                            color = Green700,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Edit Profile / Profile Details
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Gray200)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Informasi Kontak & Domisili", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)

                        Text(
                            text = if (isEditMode) "Batal" else "Edit",
                            color = Green600,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { isEditMode = !isEditMode }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isEditMode) {
                        Text("Nama Lengkap", fontSize = 11.sp, color = Gray500)
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Green600)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Nomor Telepon", fontSize = 11.sp, color = Gray500)
                        OutlinedTextField(
                            value = editPhone,
                            onValueChange = { editPhone = it },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Green600)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Kota", fontSize = 11.sp, color = Gray500)
                        OutlinedTextField(
                            value = editCity,
                            onValueChange = { editCity = it },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Green600)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.updateProfile(editName, editPhone, editCity)
                                isEditMode = false
                            },
                            modifier = Modifier.fillMaxWidth().height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green600)
                        ) {
                            Text("Simpan Perubahan", color = Color.White)
                        }
                    } else {
                        // Read Only Detail Row
                        DetailRow("Telepon", currentUser?.phone?.ifBlank { "Belum diatur" } ?: "081234567")
                        DetailRow("Kota", currentUser?.city?.ifBlank { "Yogyakarta" } ?: "Yogyakarta")
                        DetailRow("Tanggal Bergabung", "25 Juni 2026")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Change Password Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Gray200)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Ubah Password Keamanan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)
                        Text(
                            text = if (isChangePasswordMode) "Batal" else "Ubah",
                            color = Green600,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { isChangePasswordMode = !isChangePasswordMode }
                        )
                    }

                    if (isChangePasswordMode) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Password Baru", fontSize = 11.sp, color = Gray500)
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Green600),
                            visualTransformation = PasswordVisualTransformation()
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                if (newPassword.length >= 6) {
                                    viewModel.changePassword(newPassword)
                                    isChangePasswordMode = false
                                    newPassword = ""
                                }
                            },
                            enabled = newPassword.length >= 6,
                            modifier = Modifier.fillMaxWidth().height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Green600)
                        ) {
                            Text("Perbarui Password", color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Notification / Privacy Mock Options
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Gray200)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Pengaturan Privasi & Notifikasi", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Terima Notifikasi Promo", fontSize = 12.sp, color = Gray700)
                        Switch(checked = true, onCheckedChange = {}, colors = SwitchDefaults.colors(checkedThumbColor = Green600, checkedTrackColor = Green100))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sembunyikan Nomor Telepon", fontSize = 12.sp, color = Gray700)
                        Switch(checked = false, onCheckedChange = {}, colors = SwitchDefaults.colors(checkedThumbColor = Green600, checkedTrackColor = Green100))
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(label, fontSize = 11.sp, color = Gray500)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Gray900)
        Divider(color = Gray100, modifier = Modifier.padding(top = 6.dp))
    }
}

// ==================== 10. SELLER DASHBOARD ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerDashboardScreen(viewModel: LiduanViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val products by viewModel.allProducts.collectAsState()

    // Calculate metrics for Andi Wijaya products
    val sellerProducts = remember(products, currentUser) {
        products.filter { it.sellerName == (currentUser?.fullName ?: "Andi Wijaya") }
    }

    val totalProducts = sellerProducts.size
    val totalSalesVal = 3 // simulated sales orders count for demo
    val totalOrdersVal = 3
    val totalRevenueVal = 220000.0 // simulated revenue for demo

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        TopAppBar(
            title = { Text("Dashboard Penjual", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header Welcome
            Text(
                text = "Halo, Kak ${currentUser?.fullName ?: "Penjual"}!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Gray900
            )
            Text(
                text = "Kelola lapak jualan kamu dengan mudah dan jangkau maba/kating",
                fontSize = 12.sp,
                color = Gray500
            )

            Spacer(modifier = Modifier.height(16.dp))

            // KPI Grid (Metrics)
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f).padding(end = 6.dp)) {
                    KpiCard("Total Produk", totalProducts.toString(), Icons.Default.Inventory, Green600)
                }
                Box(modifier = Modifier.weight(1f).padding(start = 6.dp)) {
                    KpiCard("Total Terjual", "5 item", Icons.Default.TrendingUp, Color(0xFF3B82F6))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f).padding(end = 6.dp)) {
                    KpiCard("Total Pesanan", "$totalOrdersVal", Icons.Default.Receipt, Color(0xFFF59E0B))
                }
                Box(modifier = Modifier.weight(1f).padding(start = 6.dp)) {
                    KpiCard("Pendapatan", "Rp${String.format("%,.0f", totalRevenueVal).replace(',', '.')}", Icons.Default.AttachMoney, Color(0xFF10B981))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sales Statistics simple bar chart
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Gray200)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Statistik Penjualan Mingguan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Simulated weekly sales data chart
                    val days = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
                    val values = listOf(0.2f, 0.5f, 0.1f, 0.8f, 0.4f, 0.9f, 0.3f) // heights ratio

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        days.forEachIndexed { idx, day ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .fillMaxHeight(values[idx])
                                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                        .background(Green600)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(day, fontSize = 10.sp, color = Gray500)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Actions
            Text("Aksi Cepat Penjual", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)
            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.navigateTo(Screen.ManageProducts) },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Gray200)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.ListAlt, contentDescription = "Manage list", tint = Green600, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Kelola Lapak Jualan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900)
                        Text("Tambah produk baru, edit harga, deskripsi, dan update stok", fontSize = 11.sp, color = Gray500)
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = "Go", tint = Gray500)
                }
            }
        }
    }
}

@Composable
fun KpiCard(title: String, value: String, icon: ImageVector, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Gray200)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 11.sp, color = Gray500, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(color.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Gray900)
        }
    }
}

// ==================== 11. KELOLA PRODUK SCREEN (SELLER LIST) ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductsScreen(viewModel: LiduanViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val products by viewModel.allProducts.collectAsState()

    // Seller products
    val sellerProducts = remember(products, currentUser) {
        products.filter { it.sellerName == (currentUser?.fullName ?: "Andi Wijaya") }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        TopAppBar(
            title = { Text("Kelola Lapak Saya", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            actions = {
                // Add Product Button
                Button(
                    onClick = { viewModel.navigateTo(Screen.AddEditProduct(null)) },
                    colors = ButtonDefaults.buttonColors(containerColor = Green600),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                    modifier = Modifier.height(32.dp).testTag("add_product_action_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Product", tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("Tambah", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        if (sellerProducts.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Storefront, contentDescription = "Empty store", tint = Gray200, modifier = Modifier.size(80.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Lapak Anda masih kosong", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Gray900)
                Text("Daftarkan buku kuliah, perlengkapan kos, atau jasa bimbinganmu agar segera laku!", fontSize = 12.sp, color = Gray500, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.navigateTo(Screen.AddEditProduct(null)) },
                    colors = ButtonDefaults.buttonColors(containerColor = Green600)
                ) {
                    Text("Mulai Jualan", color = Color.White)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(sellerProducts) { product ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Gray200)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = product.imageUrl,
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(product.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Kategori: ${product.category}", fontSize = 11.sp, color = Gray500)
                                Text("Stok: ${product.stock} | Rp${String.format("%,.0f", product.price).replace(',', '.')}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Green600)
                            }

                            Row {
                                // Edit
                                IconButton(onClick = { viewModel.navigateTo(Screen.AddEditProduct(product.id)) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Green600)
                                }

                                // Delete
                                IconButton(onClick = { viewModel.deleteProduct(product.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Red500)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== 12. ADD / EDIT PRODUCT SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(viewModel: LiduanViewModel, productId: Int?) {
    val products by viewModel.allProducts.collectAsState()
    val existing = remember(products, productId) {
        if (productId != null) products.find { it.id == productId } else null
    }

    var name by remember { mutableStateOf(existing?.name ?: "") }
    var priceStr by remember { mutableStateOf(existing?.price?.toInt()?.toString() ?: "") }
    var stockStr by remember { mutableStateOf(existing?.stock?.toString() ?: "1") }
    var category by remember { mutableStateOf(existing?.category ?: "Buku Kuliah") }
    var description by remember { mutableStateOf(existing?.description ?: "") }
    var imageUrl by remember { mutableStateOf(existing?.imageUrl ?: "") }

    val categories = listOf("Buku Kuliah", "Elektronik", "Perlengkapan Kos", "Fashion", "Aksesoris", "Jasa", "Lainnya")
    var categoryDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopAppBar(
            title = { Text(if (productId == null) "Tambah Jualan Baru" else "Edit Detail Jualan", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Product Name
            Text("Nama Produk", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray700)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Contoh: Kalkulator Scientific Casio", color = Gray300) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("form_product_name"),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Green600, unfocusedBorderColor = Gray200),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Price and Stock row
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Text("Harga (Rupiah)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray700)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        placeholder = { Text("Contoh: 150000", color = Gray300) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("form_product_price"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Green600, unfocusedBorderColor = Gray200),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                    Text("Stok Barang", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray700)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = stockStr,
                        onValueChange = { stockStr = it },
                        placeholder = { Text("Contoh: 3", color = Gray300) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("form_product_stock"),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Green600, unfocusedBorderColor = Gray200),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Category Selection
            Text("Kategori Produk", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray700)
            Spacer(modifier = Modifier.height(4.dp))
            ExposedDropdownMenuBox(
                expanded = categoryDropdownExpanded,
                onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = category,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Green600, unfocusedBorderColor = Gray200),
                    shape = RoundedCornerShape(8.dp)
                )

                ExposedDropdownMenu(
                    expanded = categoryDropdownExpanded,
                    onDismissRequest = { categoryDropdownExpanded = false }
                ) {
                    categories.forEach { selection ->
                        DropdownMenuItem(
                            text = { Text(selection) },
                            onClick = {
                                category = selection
                                categoryDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Image URL
            Text("URL Foto Produk (Kosongkan untuk default)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray700)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = imageUrl,
                onValueChange = { imageUrl = it },
                placeholder = { Text("https://image-url...", color = Gray300) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("form_product_image"),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Green600, unfocusedBorderColor = Gray200),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Description
            Text("Deskripsi / Kondisi Barang", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray700)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Tulis kondisi barang secara detail agar pembeli maba/kating yakin", color = Gray300) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .testTag("form_product_desc"),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Green600, unfocusedBorderColor = Gray200),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val price = priceStr.toDoubleOrNull() ?: 0.0
                    val stock = stockStr.toIntOrNull() ?: 1
                    if (name.isNotBlank() && price > 0) {
                        viewModel.saveProduct(productId, name, price, category, stock, description, imageUrl)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("form_product_submit"),
                colors = ButtonDefaults.buttonColors(containerColor = Green600),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Simpan & Terbitkan Produk", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ==================== 13. NOTIFIKASI SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(viewModel: LiduanViewModel) {
    val notifications by viewModel.userNotifications.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        TopAppBar(
            title = { Text("Notifikasi Saya", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            actions = {
                if (notifications.isNotEmpty()) {
                    Text(
                        text = "Hapus Semua",
                        color = Red500,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { viewModel.clearAllNotifications() }
                            .padding(end = 16.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        if (notifications.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.NotificationsNone, contentDescription = "Empty", tint = Gray200, modifier = Modifier.size(80.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Tidak ada notifikasi", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Gray900)
                Text("Notifikasi mengenai pesanan dan info promo kampus akan muncul di sini.", fontSize = 12.sp, color = Gray500, textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(notifications) { notif ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Gray200)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Icon based on type
                            val (icon, tintBg, tintIcon) = when (notif.type) {
                                "Promo" -> Triple(Icons.Default.Celebration, Color(0xFFFEF3C7), Yellow500)
                                "Order" -> Triple(Icons.Default.Receipt, Color(0xFFDBEAFE), Blue500)
                                "Sold" -> Triple(Icons.Default.CheckCircle, Color(0xFFD1FADF), Green700)
                                else -> Triple(Icons.Default.Favorite, Color(0xFFFEE2E2), Red500)
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(tintBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(icon, contentDescription = notif.type, tint = tintIcon, modifier = Modifier.size(18.dp))
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(notif.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(notif.message, fontSize = 12.sp, color = Gray700, lineHeight = 16.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Beberapa saat yang lalu", fontSize = 10.sp, color = Gray500)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== 12. ADMIN DASHBOARD SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(viewModel: LiduanViewModel) {
    val allProducts by viewModel.allProducts.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Statistik", "Manajemen User", "Moderasi Jualan", "Daftar Transaksi")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("LiduaN Admin Panel", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Gray900)
                        Text("Akses Utama Moderasi Platform", fontSize = 11.sp, color = Gray500)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.logout() }) {
                        Icon(Icons.Default.Logout, contentDescription = "Log Out", tint = Red500)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Gray50)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Green600,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Green600
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == index) Green600 else Gray500
                            )
                        }
                    )
                }
            }

            // Tab Content
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    0 -> AdminStatsTab(allProducts, allUsers, allOrders)
                    1 -> AdminUsersTab(allUsers, viewModel)
                    2 -> AdminProductsTab(allProducts, viewModel)
                    3 -> AdminOrdersTab(allOrders, viewModel)
                }
            }
        }
    }
}

@Composable
fun AdminStatsTab(
    products: List<ProductEntity>,
    users: List<UserEntity>,
    orders: List<OrderEntity>
) {
    val activeOrders = orders.filter { it.status != "Dibatalkan" }
    val totalRevenue = activeOrders.sumOf { it.totalAmount }
    val pendingPaymentCount = orders.count { it.status == "Menunggu Pembayaran" }
    val processedCount = orders.count { it.status == "Diproses" }
    val shippedCount = orders.count { it.status == "Dikirim" }
    val completedCount = orders.count { it.status == "Selesai" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary Cards Grid (2x2)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Card 1: Total Revenue
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Gray200)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFD1FADF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Payments, contentDescription = "Revenue", tint = Green700, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Total Transaksi", fontSize = 11.sp, color = Gray500)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                "Rp${String.format("%,.0f", totalRevenue).replace(',', '.')}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Gray900
                            )
                        }
                    }

                    // Card 2: Total Users
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Gray200)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFDBEAFE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Group, contentDescription = "Users", tint = Blue500, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Total Pengguna", fontSize = 11.sp, color = Gray500)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${users.size} Orang", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900)
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Card 3: Total Products
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Gray200)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Inventory, contentDescription = "Products", tint = Yellow500, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Total Jualan", fontSize = 11.sp, color = Gray500)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${products.size} Produk", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900)
                        }
                    }

                    // Card 4: Orders Count
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Gray200)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF3E8FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.ShoppingBag, contentDescription = "Orders", tint = Purple500, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Total Pesanan", fontSize = 11.sp, color = Gray500)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("${orders.size} Pesanan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray900)
                        }
                    }
                }
            }
        }

        // Charts Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Gray200)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Distribusi Peran Pengguna", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Perbandingan jumlah Pembeli, Penjual, dan Admin di platform", fontSize = 11.sp, color = Gray500)
                    Spacer(modifier = Modifier.height(16.dp))

                    val buyers = users.count { it.role == "Pembeli" }
                    val sellers = users.count { it.role == "Penjual" }
                    val admins = users.count { it.role == "Admin" }
                    val total = (buyers + sellers + admins).coerceAtLeast(1).toFloat()

                    // Horizontal stacked bar represent roles
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                    ) {
                        if (buyers > 0) {
                            Box(
                                modifier = Modifier
                                    .weight((buyers / total).coerceAtLeast(0.05f))
                                    .fillMaxHeight()
                                    .background(Green600),
                                contentAlignment = Alignment.Center
                            ) {
                                if (buyers / total > 0.15f) Text("Pembeli ($buyers)", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                        if (sellers > 0) {
                            Box(
                                modifier = Modifier
                                    .weight((sellers / total).coerceAtLeast(0.05f))
                                    .fillMaxHeight()
                                    .background(Blue500),
                                contentAlignment = Alignment.Center
                            ) {
                                if (sellers / total > 0.15f) Text("Penjual ($sellers)", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                        if (admins > 0) {
                            Box(
                                modifier = Modifier
                                    .weight((admins / total).coerceAtLeast(0.05f))
                                    .fillMaxHeight()
                                    .background(Red500),
                                contentAlignment = Alignment.Center
                            ) {
                                if (admins / total > 0.15f) Text("Admin ($admins)", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    // Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        LegendItem(color = Green600, label = "Pembeli ($buyers)")
                        LegendItem(color = Blue500, label = "Penjual ($sellers)")
                        LegendItem(color = Red500, label = "Admin ($admins)")
                    }
                }
            }
        }

        // Analytical Statistics for Order Status
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Gray200)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Status Pemrosesan Pesanan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)
                    Spacer(modifier = Modifier.height(14.dp))

                    StatusProgessBar("Selesai", completedCount, orders.size, Green700)
                    Spacer(modifier = Modifier.height(10.dp))
                    StatusProgessBar("Dikirim", shippedCount, orders.size, Blue500)
                    Spacer(modifier = Modifier.height(10.dp))
                    StatusProgessBar("Diproses", processedCount, orders.size, Purple500)
                    Spacer(modifier = Modifier.height(10.dp))
                    StatusProgessBar("Belum Bayar", pendingPaymentCount, orders.size, Yellow500)
                }
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, fontSize = 11.sp, color = Gray700)
    }
}

@Composable
fun StatusProgessBar(label: String, count: Int, total: Int, color: Color) {
    val pct = if (total > 0) count.toFloat() / total else 0f
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Gray700)
            Text("$count (${String.format("%.0f", pct * 100)}%)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Gray900)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = pct,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = Gray100
        )
    }
}

@Composable
fun AdminUsersTab(
    users: List<UserEntity>,
    viewModel: LiduanViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredUsers = remember(users, searchQuery) {
        if (searchQuery.isBlank()) users
        else users.filter {
            it.fullName.contains(searchQuery, ignoreCase = true) ||
                    it.email.contains(searchQuery, ignoreCase = true) ||
                    it.role.contains(searchQuery, ignoreCase = true)
        }
    }

    var userToEditRole by remember { mutableStateOf<UserEntity?>(null) }
    var showDeleteConfirmDialog by remember { mutableStateOf<UserEntity?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari pengguna...", fontSize = 12.sp, color = Gray300) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Gray400) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = Gray400)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Green500,
                unfocusedBorderColor = Gray200,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredUsers) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Gray200)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(user.fullName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    // Role Badge
                                    val (badgeBg, badgeText) = when (user.role) {
                                        "Admin" -> Pair(Color(0xFFFEE2E2), Red500)
                                        "Penjual" -> Pair(Color(0xFFDBEAFE), Blue500)
                                        else -> Pair(Color(0xFFD1FADF), Green700)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(badgeBg)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(user.role, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = badgeText)
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(user.email, fontSize = 11.sp, color = Gray600)
                                Text("Telepon: ${user.phone.ifBlank { "-" }} • Kota: ${user.city.ifBlank { "-" }}", fontSize = 11.sp, color = Gray500)
                            }

                            // Quick actions
                            Row {
                                IconButton(onClick = { userToEditRole = user }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit Role", tint = Green600, modifier = Modifier.size(18.dp))
                                }
                                IconButton(onClick = { showDeleteConfirmDialog = user }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Hapus User", tint = Red500, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Role Edit Dialog
    if (userToEditRole != null) {
        val rolesList = listOf("Pembeli", "Penjual", "Admin")
        AlertDialog(
            onDismissRequest = { userToEditRole = null },
            title = { Text("Ubah Peran Pengguna", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Pilih peran baru untuk ${userToEditRole!!.fullName}:", fontSize = 12.sp, color = Gray700)
                    Spacer(modifier = Modifier.height(14.dp))
                    rolesList.forEach { roleOption ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.updateUserRoleByAdmin(userToEditRole!!.id, roleOption)
                                    userToEditRole = null
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = userToEditRole!!.role == roleOption,
                                onClick = {
                                    viewModel.updateUserRoleByAdmin(userToEditRole!!.id, roleOption)
                                    userToEditRole = null
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = Green600)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(roleOption, fontSize = 13.sp, color = Gray900)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { userToEditRole = null }) {
                    Text("Batal", color = Gray600)
                }
            }
        )
    }

    // Delete confirmation Dialog
    if (showDeleteConfirmDialog != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = null },
            title = { Text("Hapus Pengguna?", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Red500) },
            text = {
                Text(
                    "Apakah Anda yakin ingin menghapus akun ${showDeleteConfirmDialog!!.fullName}? Tindakan ini bersifat permanen.",
                    fontSize = 12.sp,
                    color = Gray700
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteUserByAdmin(showDeleteConfirmDialog!!.id)
                        showDeleteConfirmDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Red500)
                ) {
                    Text("Hapus Permanen", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = null }) {
                    Text("Batal", color = Gray600)
                }
            }
        )
    }
}

@Composable
fun AdminProductsTab(
    products: List<ProductEntity>,
    viewModel: LiduanViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredProducts = remember(products, searchQuery) {
        if (searchQuery.isBlank()) products
        else products.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.sellerName.contains(searchQuery, ignoreCase = true) ||
                    it.category.contains(searchQuery, ignoreCase = true)
        }
    }

    var productToDelete by remember { mutableStateOf<ProductEntity?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Product Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari produk jualan...", fontSize = 12.sp, color = Gray300) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Gray400) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = Gray400)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Green500,
                unfocusedBorderColor = Gray200,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(filteredProducts) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Gray200)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Gray100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Inventory, contentDescription = "Product", tint = Gray400, modifier = Modifier.size(24.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(product.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Gray900, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("Kategori: ${product.category}", fontSize = 10.sp, color = Gray500)
                            Text("Penjual: ${product.sellerName}", fontSize = 10.sp, fontWeight = FontWeight.Medium, color = Green600)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Rp${String.format("%,.0f", product.price).replace(',', '.')}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Gray900)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Stok: ${product.stock}", fontSize = 11.sp, color = if (product.stock == 0) Red500 else Gray600)
                            }
                        }

                        // Moderate/Delete action
                        IconButton(onClick = { productToDelete = product }) {
                            Icon(Icons.Default.Delete, contentDescription = "Moderate", tint = Red500, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }

    if (productToDelete != null) {
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = { Text("Hapus / Moderasi Produk", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Red500) },
            text = {
                Text(
                    "Apakah Anda yakin ingin menghapus produk '${productToDelete!!.name}' dari platform LiduaN?",
                    fontSize = 12.sp,
                    color = Gray700
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteProductByAdmin(productToDelete!!.id)
                        productToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Red500)
                ) {
                    Text("Moderasi & Hapus", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text("Batal", color = Gray600)
                }
            }
        )
    }
}

@Composable
fun AdminOrdersTab(
    orders: List<OrderEntity>,
    viewModel: LiduanViewModel
) {
    var orderToUpdateStatus by remember { mutableStateOf<OrderEntity?>(null) }

    if (orders.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Receipt, contentDescription = "Empty", tint = Gray200, modifier = Modifier.size(64.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("Belum ada transaksi di platform", fontSize = 13.sp, color = Gray500)
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(orders) { order ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Gray200)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Pesanan #${order.id}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Gray500)
                            
                            // Order status badge
                            val (badgeBg, badgeText) = when (order.status) {
                                "Menunggu Pembayaran" -> Pair(Color(0xFFFEF3C7), Yellow500)
                                "Diproses" -> Pair(Color(0xFFF3E8FF), Purple500)
                                "Dikirim" -> Pair(Color(0xFFDBEAFE), Blue500)
                                "Selesai" -> Pair(Color(0xFFD1FADF), Green700)
                                else -> Pair(Color(0xFFFEE2E2), Red500)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(badgeBg)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(order.status, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = badgeText)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(order.productNames, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Gray900)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Metode: ${order.paymentMethod}", fontSize = 11.sp, color = Gray700)
                        Text("Alamat: ${order.shippingAddress}", fontSize = 11.sp, color = Gray600)
                        Text("Jumlah Bayar: Rp${String.format("%,.0f", order.totalAmount).replace(',', '.')}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Green600)

                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { orderToUpdateStatus = order },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                                modifier = Modifier.height(30.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Green600)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Ubah Status", tint = Color.White, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ubah Status Pesanan", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    if (orderToUpdateStatus != null) {
        val statusList = listOf("Menunggu Pembayaran", "Diproses", "Dikirim", "Selesai", "Dibatalkan")
        AlertDialog(
            onDismissRequest = { orderToUpdateStatus = null },
            title = { Text("Simulasikan Status Pesanan", fontSize = 14.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Pilih status pesanan baru:", fontSize = 11.sp, color = Gray500)
                    Spacer(modifier = Modifier.height(14.dp))
                    statusList.forEach { statusOption ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.updateOrderStatusByAdmin(orderToUpdateStatus!!.id, statusOption)
                                    orderToUpdateStatus = null
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = orderToUpdateStatus!!.status == statusOption,
                                onClick = {
                                    viewModel.updateOrderStatusByAdmin(orderToUpdateStatus!!.id, statusOption)
                                    orderToUpdateStatus = null
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = Green600)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(statusOption, fontSize = 12.sp, color = Gray900)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { orderToUpdateStatus = null }) {
                    Text("Batal", color = Gray600)
                }
            }
        )
    }
}


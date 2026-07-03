package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val email: String,
    val passwordHash: String,
    val role: String, // "Pembeli", "Penjual"
    val phone: String = "",
    val city: String = "",
    val joinDate: Long = System.currentTimeMillis()
)

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Double,
    val category: String,
    val stock: Int,
    val description: String,
    val imageUrl: String,
    val rating: Float = 4.5f,
    val sellerName: String,
    val sellerId: Int = 0,
    val isPopular: Boolean = false
)

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val quantity: Int = 1,
    val isSelected: Boolean = true,
    val userId: Int = 0
)

@Entity(tableName = "wishlist_items")
data class WishlistItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val userId: Int = 0
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productNames: String,
    val totalAmount: Double,
    val paymentMethod: String, // "Transfer Bank", "E-Wallet", "COD"
    val shippingAddress: String,
    val status: String, // "Menunggu Pembayaran", "Diproses", "Dikirim", "Selesai", "Dibatalkan"
    val timestamp: Long = System.currentTimeMillis(),
    val userId: Int = 0
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String, // "Order", "Sold", "Promo", "Favorite"
    val userId: Int = 0
)

package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class AppRepository(private val db: AppDatabase) {
    val userDao = db.userDao()
    val productDao = db.productDao()
    val cartDao = db.cartDao()
    val wishlistDao = db.wishlistDao()
    val orderDao = db.orderDao()
    val notificationDao = db.notificationDao()

    suspend fun checkAndPrepopulate() {
        // We use first() on Flow to get a snapshot
        val existingProducts = productDao.getAllProducts().first()
        if (existingProducts.isEmpty()) {
            prepopulateData()
        }
    }

    private suspend fun prepopulateData() {
        // Create demo users
        val buyer = UserEntity(
            fullName = "Budi Santoso",
            email = "budi@mahasiswa.ac.id",
            passwordHash = "password123", // Simple plain-text for demo ease
            role = "Pembeli",
            phone = "08123456789",
            city = "Yogyakarta"
        )
        val seller = UserEntity(
            fullName = "Andi Wijaya",
            email = "andi@mahasiswa.ac.id",
            passwordHash = "password123",
            role = "Penjual",
            phone = "08987654321",
            city = "Bandung"
        )
        val admin = UserEntity(
            fullName = "Admin Utama",
            email = "admin@mahasiswa.ac.id",
            passwordHash = "admin123",
            role = "Admin",
            phone = "08111222333",
            city = "Yogyakarta"
        )
        val buyerId = userDao.insertUser(buyer).toInt()
        val sellerId = userDao.insertUser(seller).toInt()
        val adminId = userDao.insertUser(admin).toInt()

        val productsList = listOf(
            ProductEntity(name = "Buku Kalkulus Purcell Edisi 9", price = 120000.0, category = "Buku Kuliah", stock = 3, description = "Buku wajib kalkulus teknik dan sains. Kondisi 90% mulus, tidak ada coretan pulpen, halaman lengkap.", imageUrl = "https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=500&auto=format&fit=crop&q=60", rating = 4.8f, sellerName = "Andi Wijaya", sellerId = sellerId, isPopular = true),
            ProductEntity(name = "Buku Algoritma & Struktur Data", price = 85000.0, category = "Buku Kuliah", stock = 5, description = "Buku referensi praktis belajar algoritma menggunakan Kotlin dan Java. Sangat direkomendasikan untuk maba informatika.", imageUrl = "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=500&auto=format&fit=crop&q=60", rating = 4.6f, sellerName = "Andi Wijaya", sellerId = sellerId, isPopular = true),
            ProductEntity(name = "Buku Fisika Dasar Halliday Jilid 1", price = 145000.0, category = "Buku Kuliah", stock = 2, description = "Buku pegangan fisika dasar semester 1. Kondisi masih kokoh, ada sedikit lipatan di cover.", imageUrl = "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?w=500&auto=format&fit=crop&q=60", rating = 4.7f, sellerName = "Riska Amalia", sellerId = 991, isPopular = false),
            ProductEntity(name = "Buku Pengantar Ekonomi Makro Mankiw", price = 110000.0, category = "Buku Kuliah", stock = 1, description = "Sangat cocok untuk anak FEB. Kondisi disampul rapi, tidak ada halaman sobek.", imageUrl = "https://images.unsplash.com/photo-1495446815901-a7297e633e8d?w=500&auto=format&fit=crop&q=60", rating = 4.4f, sellerName = "Dewi Lestari", sellerId = 992, isPopular = false),
            ProductEntity(name = "Laptop ASUS Core i5 Bekas Kuliah", price = 3800000.0, category = "Elektronik", stock = 1, description = "Spesifikasi mumpuni untuk coding dan tugas akhir. RAM 8GB, SSD 256GB. Baterai tahan 2-3 jam pemakaian.", imageUrl = "https://images.unsplash.com/photo-1496181130204-755241524eab?w=500&auto=format&fit=crop&q=60", rating = 4.5f, sellerName = "Andi Wijaya", sellerId = sellerId, isPopular = true),
            ProductEntity(name = "iPad Air 4 64GB + Apple Pencil 2", price = 6500000.0, category = "Elektronik", stock = 1, description = "Pembelian tahun lalu untuk catat materi kuliah. Layar jernih, iCloud aman bebas reset, bonus case.", imageUrl = "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=500&auto=format&fit=crop&q=60", rating = 4.9f, sellerName = "Andi Wijaya", sellerId = sellerId, isPopular = true),
            ProductEntity(name = "Kalkulator Casio fx-991EX Classwiz", price = 280000.0, category = "Elektronik", stock = 4, description = "Kalkulator scientific canggih untuk anak teknik/MIPA. Layar resolusi tinggi, lengkap dengan kotak.", imageUrl = "https://images.unsplash.com/photo-1611079830811-b65d1a36b7b5?w=500&auto=format&fit=crop&q=60", rating = 4.7f, sellerName = "Farhan Putra", sellerId = 993, isPopular = false),
            ProductEntity(name = "Monitor LG 24 Inch IPS Borderless", price = 1200000.0, category = "Elektronik", stock = 2, description = "Bekas display kos. Resolusi Full HD 75Hz. Sangat cocok buat dual-screen saat bikin skripsi.", imageUrl = "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=500&auto=format&fit=crop&q=60", rating = 4.6f, sellerName = "Andi Wijaya", sellerId = sellerId, isPopular = false),
            ProductEntity(name = "Kasur Busa Inoac Single 90x200", price = 450000.0, category = "Perlengkapan Kos", stock = 1, description = "Busa masih empuk banget anti-kempes, pemakaian 6 bulan. Siap diangkut sendiri dari kos daerah gejayan.", imageUrl = "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=500&auto=format&fit=crop&q=60", rating = 4.5f, sellerName = "Andi Wijaya", sellerId = sellerId, isPopular = true),
            ProductEntity(name = "Rice Cooker Cosmos 0.6L Hemat", price = 180000.0, category = "Perlengkapan Kos", stock = 2, description = "Sangat cocok untuk anak kos hemat daya (hanya 300W). Kondisi bersih, panci teflon aman tidak baret.", imageUrl = "https://images.unsplash.com/photo-1584269600464-37b1b58a9fe7?w=500&auto=format&fit=crop&q=60", rating = 4.3f, sellerName = "Siti Rahma", sellerId = 994, isPopular = true),
            ProductEntity(name = "Dispenser Air Miyako + Galon Aqua", price = 150000.0, category = "Perlengkapan Kos", stock = 1, description = "Satu paket dispenser hot/normal dan satu galon kosong Aqua. Tinggal isi ulang langsung pakai.", imageUrl = "https://images.unsplash.com/photo-1609146170669-e58f0003cb87?w=500&auto=format&fit=crop&q=60", rating = 4.2f, sellerName = "Bambang Tri", sellerId = 995, isPopular = false),
            ProductEntity(name = "Lemari Plastik Laci 4 Susun", price = 120000.0, category = "Perlengkapan Kos", stock = 3, description = "Lemari plastik portable kokoh, warna netral abu-abu. Muat banyak kaos dan kemeja kuliah.", imageUrl = "https://images.unsplash.com/photo-1595428774223-ef52624120d2?w=500&auto=format&fit=crop&q=60", rating = 4.1f, sellerName = "Siti Rahma", sellerId = 994, isPopular = false),
            ProductEntity(name = "Jaket Varsity Almamater Hitam L", price = 175000.0, category = "Fashion", stock = 1, description = "Jaket varsity kampus bahan katun fleece tebal premium, ukuran L. Sangat hangat dipakai kuliah malam.", imageUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=500&auto=format&fit=crop&q=60", rating = 4.7f, sellerName = "Andi Wijaya", sellerId = sellerId, isPopular = true),
            ProductEntity(name = "Sneakers Vans Old Skool Original 42", price = 550000.0, category = "Fashion", stock = 1, description = "Ukuran 42. Masih bagus solnya, pemakaian wajar ke kampus. Dus box lengkap original.", imageUrl = "https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?w=500&auto=format&fit=crop&q=60", rating = 4.6f, sellerName = "Andi Wijaya", sellerId = sellerId, isPopular = false),
            ProductEntity(name = "Tas Ransel Laptop Eiger 25L", price = 320000.0, category = "Fashion", stock = 2, description = "Sangat awet, jahitan kokoh, muat laptop 15.6 inch + ada rain cover gratis.", imageUrl = "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500&auto=format&fit=crop&q=60", rating = 4.8f, sellerName = "Andi Wijaya", sellerId = sellerId, isPopular = true),
            ProductEntity(name = "Kacamata Anti Radiasi Komputer", price = 95000.0, category = "Aksesoris", stock = 10, description = "Melindungi mata pas ngetik tugas dan coding semalaman. Model frame unisex minimalis.", imageUrl = "https://images.unsplash.com/photo-1511556532299-8f662fc26c06?w=500&auto=format&fit=crop&q=60", rating = 4.4f, sellerName = "Rian Hidayat", sellerId = 996, isPopular = false),
            ProductEntity(name = "Tumbler Stainless LocknLock 500ml", price = 150000.0, category = "Aksesoris", stock = 5, description = "Tahan panas/dingin sampai 12 jam. Cocok dibawa saat kuliah panjang atau di perpustakaan.", imageUrl = "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=500&auto=format&fit=crop&q=60", rating = 4.9f, sellerName = "Andi Wijaya", sellerId = sellerId, isPopular = true),
            ProductEntity(name = "Jasa Instalasi OS & Software Kuliah", price = 50000.0, category = "Jasa", stock = 99, description = "Bisa bantu instal Windows, Office, MATLAB, AutoCAD, SPSS, Photoshop. Bergaransi sampai lancar.", imageUrl = "https://images.unsplash.com/photo-1600132806370-bf17e65e942f?w=500&auto=format&fit=crop&q=60", rating = 4.9f, sellerName = "Andi Wijaya", sellerId = sellerId, isPopular = true),
            ProductEntity(name = "Jasa Penerjemahan Abstrak Inggris", price = 40000.0, category = "Jasa", stock = 99, description = "Penerjemahan cepat untuk abstrak skripsi dan jurnal. Jaminan tata bahasa/grammar rapi.", imageUrl = "https://images.unsplash.com/photo-1455390582262-044cdead277a?w=500&auto=format&fit=crop&q=60", rating = 4.8f, sellerName = "Sari Kartika", sellerId = 997, isPopular = true),
            ProductEntity(name = "Jasa Desain Poster & PPT Interaktif", price = 75000.0, category = "Jasa", stock = 99, description = "Bikin slide presentasi tugas kamu eye-catching dan profesional. Revisi sampai cocok.", imageUrl = "https://images.unsplash.com/photo-1542744094-3a31f103e35f?w=500&auto=format&fit=crop&q=60", rating = 4.7f, sellerName = "Andi Wijaya", sellerId = sellerId, isPopular = false),
            ProductEntity(name = "Binder Catatan Kuliah Ring 26", price = 35000.0, category = "Lainnya", stock = 8, description = "Binder cover semi-transparan warna pastel ring besi. Bonus kertas binder 50 lembar.", imageUrl = "https://images.unsplash.com/photo-1517842645767-c639042777db?w=500&auto=format&fit=crop&q=60", rating = 4.5f, sellerName = "Bambang Tri", sellerId = 995, isPopular = false),
            ProductEntity(name = "Whiteboard Mini 30x40cm + Spidol", price = 45000.0, category = "Lainnya", stock = 3, description = "Sangat berguna untuk coret-coret rumus di kamar kos saat belajar kelompok.", imageUrl = "https://images.unsplash.com/photo-1572021335469-31706a17aaef?w=500&auto=format&fit=crop&q=60", rating = 4.3f, sellerName = "Andi Wijaya", sellerId = sellerId, isPopular = false)
        )

        for (p in productsList) {
            productDao.insertProduct(p)
        }

        // Create some orders for Budi Santoso (buyerId)
        val order1 = OrderEntity(
            productNames = "Buku Kalkulus Purcell Edisi 9 x1",
            totalAmount = 120000.0,
            paymentMethod = "COD",
            shippingAddress = "Asrama Mahasiswa Kampus Terpadu, Gedung B Kamar 12",
            status = "Selesai",
            userId = buyerId
        )
        val order2 = OrderEntity(
            productNames = "Jasa Instalasi OS & Software Kuliah x1",
            totalAmount = 50000.0,
            paymentMethod = "E-Wallet",
            shippingAddress = "Online Delivery (via Google Meet / TeamViewer)",
            status = "Diproses",
            userId = buyerId
        )
        val order3 = OrderEntity(
            productNames = "Kalkulator Casio fx-991EX Classwiz x1",
            totalAmount = 280000.0,
            paymentMethod = "Transfer Bank",
            shippingAddress = "Kos Melati, Jl. Kaliurang KM 5.5 No. 42",
            status = "Dikirim",
            userId = buyerId
        )
        val order4 = OrderEntity(
            productNames = "Kasur Busa Inoac Single 90x200 x1",
            totalAmount = 450000.0,
            paymentMethod = "Transfer Bank",
            shippingAddress = "Kos Melati, Jl. Kaliurang KM 5.5 No. 42",
            status = "Menunggu Pembayaran",
            userId = buyerId
        )

        orderDao.insertOrder(order1)
        orderDao.insertOrder(order2)
        orderDao.insertOrder(order3)
        orderDao.insertOrder(order4)

        // Create initial notifications
        val notif1 = NotificationEntity(
            title = "Promo Diskon Buku Kuliah!",
            message = "Dapatkan diskon hingga 30% untuk buku kuliah teknik dan sosial. Hanya minggu ini!",
            type = "Promo",
            userId = buyerId
        )
        val notif2 = NotificationEntity(
            title = "Pesanan Diproses",
            message = "Pesanan Jasa Instalasi OS & Software sedang diproses oleh Andi Wijaya.",
            type = "Order",
            userId = buyerId
        )
        val notif3 = NotificationEntity(
            title = "Produk Favorit Terjual!",
            message = "Kasur Busa Inoac tinggal tersisa 1 stok lagi! Segera beli sekarang.",
            type = "Favorite",
            userId = buyerId
        )

        // Notification for Andi Wijaya (Seller)
        val notif4 = NotificationEntity(
            title = "Pesanan Baru Masuk!",
            message = "Budi Santoso membeli Jasa Instalasi OS & Software Kuliah. Segera hubungi pembeli.",
            type = "Order",
            userId = sellerId
        )
        val notif5 = NotificationEntity(
            title = "Produk Terjual!",
            message = "Selamat! Buku Kalkulus Purcell Edisi 9 Anda telah laku terjual.",
            type = "Sold",
            userId = sellerId
        )

        notificationDao.insertNotification(notif1)
        notificationDao.insertNotification(notif2)
        notificationDao.insertNotification(notif3)
        notificationDao.insertNotification(notif4)
        notificationDao.insertNotification(notif5)
    }
}

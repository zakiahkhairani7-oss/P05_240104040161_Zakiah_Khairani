\# Praktikum 05 – Pengelolaan Data Lokal (WargaHub P05)



&#x20;Nama Zakiah Khairani

&#x20;NIM 240104040161

&#x20;Kelas TI 24 B

&#x20;Mata Kuliah Praktikum Pemrograman Mobile (PTI23043 • 1 SKS)

&#x20;Dosen Pengampu Muhayat, M.IT

&#x20;Proyek `WargaHubP05` (`id.antasari.p05.wargahub`)



\---



\## 1. Konfigurasi Lingkungan \& Alat

&#x20;IDE Android Studio Rabbit 2 (2026.2.2 Patch 1)

&#x20;JDK  Gradle Wrapper JDK 17  Gradle 8.13

&#x20;AGP  Kotlin  KSP 8.13.1  2.2.21  2.3.12

&#x20;Room 3.0.3 (`androidx.room3`)

&#x20;SDK `compileSdk 36`, `targetSdk 36`, `minSdk 26`

&#x20;Perangkat Uji Emulator Pixel 8 (1080 × 2400 px, 420 dpi), Android 14.0 (API 34)



\---



\## 2. Cara Membuka dan Menjalankan Proyek

1\. Clone atau ekstrak folder `P05\_240104040161\_Zakiah\_Khairani`.

2\. Buka Android Studio, pilih Open, lalu arahkan ke subfolder `WargaHubP05`.

3\. Tunggu proses Gradle Sync hingga selesai.

4\. Pilih perangkat uji (Emulator API 26+ atau perangkat fisik dengan USB Debugging), lalu klik Run (`Shift + F10`).

5\. Untuk build APK debug melalui terminal dari direktori `WargaHubP05`

&#x20;   Windows `.gradlew.bat assembleDebug`

&#x20;   macOSLinux `.gradlew assembleDebug`



\---



\## 3. Ringkasan Fitur \& Alur Data

Aplikasi WargaHub P05 mengelola catatan laporan warga secara lokal di perangkat (tanpa pengiriman ke server) menggunakan arsitektur pemisahan lapisan



&#x20;Alur Data `MainActivity`  `FormActivity` → `MainViewModel`  `FormViewModel` → `LaporanRepository` → `LaporanDao` → `WargaHubDatabase` (RoomSQLite) \& `PengaturanPreferences` (SharedPreferences).

&#x20;Room (`wargahub-p05.db`) Menyimpan data laporan (`id`, `judul`, `kategori`, `deskripsi`) dengan operasi CRUD reaktif menggunakan `Flow` dan `suspend`.

&#x20;SharedPreferences (`id.antasari.p05.wargahub.pengaturan`) Menyimpan preferensi urutan daftar (`urut\_terbaru Boolean`) menggunakan `commit()` pada `Dispatchers.IO`.

&#x20;Validasi \& Persistensi Repository melakukan normalisasi `trim()` dan validasi input sebelum menyentuh database. Persistensi telah diverifikasi menggunakan Force stop pada skenario T11–T14.



\---



\## 4. Dokumentasi \& Bukti Pengujian

&#x20;Laporan Lengkap \& Matriks Uji (T01–T21) Tersedia di folder `docsLaporan\_P05\_240104040161\_Zakiah Khairani.docx`.

&#x20;Tangkapan Layar (Gambar 1–34) Tersedia di folder `screenshots`.

&#x20;Keterbatasan Pengujian

&#x20;  Database Inspector (T20) tidak dapat memuat proses (No Process Selected  Database Inspector is loading...), sehingga verifikasi jumlah dan ID dilakukan melalui UI dan ekspor skema `appschemas...1.json`.

&#x20;  TalkBack (T19) dan uji keyboardlandscapefont besar (T16) diverifikasi secara visual pada antarmuka sesuai kondisi emulator.



\---



\## 5. Ringkasan Jawaban Refleksi

1\. SharedPreferences vs. Room `SharedPreferences` cocok untuk satu nilai kecil key-value (`urut\_terbaru`), sedangkan daftar laporan dengan banyak kolom dan operasi CRUD berdasarkan ID membutuhkan database relasional RoomSQLite.

2\. `apply()` vs. `commit()` `apply()` menulis secara asinkron tanpa nilai kembali, sedangkan `commit()` menulis secara sinkron dan mengembalikan status `Boolean` keberhasilan sehingga dijalankan pada `Dispatchers.IO` agar tidak memblokir UI thread.

3\. Pemisahan Tanggung Jawab `LaporanEntity` mendefinisikan struktur tabel, `LaporanDao` menjalankan query SQL, `WargaHubDatabase` mengelola instans database, `LaporanRepository` memusatkan validasinormalisasi, dan `ViewModel` mengelola state UI (`StateFlow`) secara asinkron.

4\. Primary Key vs. Posisi Baris Primary key (`id`) bersifat permanen dan unik meskipun urutan diubah atau item dihapus, sedangkan posisi baris di `RecyclerView` selalu bergeser sesuai urutan tampil.

5\. Persistensi (`Force stop` vs. Rotasi) Rotasi hanya membuat ulang `Activity` sementara proses dan `ViewModel` tetap hidup di memori; `Force stop` mematikan proses sepenuhnya sehingga membuktikan data benar-benar tersimpan di penyimpanan disk perangkat.


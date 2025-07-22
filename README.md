# Booking Konsultasi Dosen

<p align="center">
  <img src="exampleImg/icon_consultation_booking.png" alt="Logo Aplikasi Cek Resi" width="250"/>
</p>

Aplikasi Android native yang dirancang untuk mempermudah proses penjadwalan konsultasi antara mahasiswa dan dosen. Aplikasi ini dibangun dengan arsitektur modern, UI yang bersih, dan backend yang tangguh untuk memastikan pengalaman pengguna yang lancar dan efisien.

## ✨ Fitur Utama

Aplikasi ini memiliki dua peran utama dengan fungsionalitas yang disesuaikan:

### 👨‍🎓 Sebagai Mahasiswa
- **Autentikasi:** Register dan Login yang aman.
- **Manajemen Profil:** Melihat, memperbarui detail profil, dan menghapus akun.
- **Buat Jadwal:** Membuat permintaan jadwal konsultasi baru dengan memilih dosen, tanggal, jam, dan topik.
- **Lihat Daftar Jadwal:** Melihat riwayat semua jadwal yang pernah dibuat beserta statusnya (Pending, Approved, Rejected).
- **Edit Jadwal:** Mengubah detail jadwal yang masih berstatus "Pending".
- **Batalkan Jadwal:** Menghapus/membatalkan jadwal yang sudah dibuat.

### 👩‍🏫 Sebagai Dosen
- **Autentikasi:** Login aman (akun dibuat oleh admin).
- **Lihat Daftar Permintaan:** Melihat semua permintaan jadwal konsultasi yang masuk dari mahasiswa.
- **Filter Jadwal:** Menyaring daftar jadwal berdasarkan status (Semua, Pending, Approved, Rejected) untuk manajemen yang lebih mudah.
- **Detail Permintaan:** Melihat detail lengkap dari setiap permintaan, termasuk nama mahasiswa, NIM, waktu, dan topik.
- **Persetujuan Jadwal:** Menyetujui (`Approve`) atau menolak (`Reject`) permintaan konsultasi langsung dari aplikasi.

## 🛠️ Tumpukan Teknologi (Tech Stack)

Proyek ini dibagi menjadi dua bagian utama: aplikasi Android (frontend) dan API (backend).

### 📱 Android (Frontend)
- **Bahasa:** Kotlin
- **Arsitektur:** MVVM (Model-View-ViewModel) dengan Repository Pattern
- **UI:** XML Layouts dengan Material Design 3
- **Networking:** Retrofit 2 & OkHttp 3 untuk komunikasi API
- **Asynchronous:** Kotlin Coroutines & `viewModelScope`
- **Manajemen Sesi:** SharedPreferences
- **Lainnya:** View Binding

### 🌐 API (Backend)
- **Framework:** Python Flask
- **Database:** MariaDB (dapat diganti dengan MySQL/PostgreSQL)
- **ORM:** SQLAlchemy
- **Autentikasi:** JWT (JSON Web Tokens) dengan `flask-jwt-extended`
- **Server:** Gunicorn

## 📸 Screenshot

| Halaman Login | Halaman Register | Halaman Utama (Dosen) | Halaman Detail |
| :-----------: | :--------------: | :-------------------: | :------------: |
| [Login](https://i.ibb.co/m5mD1Yw7/Screenshot-20250722-223121-Booking-Konsultasi-App.png) | [Register](https://i.ibb.co/0yNcGPm5/Screenshot-20250722-223128-Booking-Konsultasi-App.png) | [Main](https://i.ibb.co/m5FyQvJc/Screenshot-20250722-223155-Booking-Konsultasi-App.png) | [Detail](https://i.ibb.co/DDXjLZpF/Screenshot-20250722-223204-Booking-Konsultasi-App.png) |

## 🚀 Instalasi & Penyiapan

Untuk menjalankan proyek ini, Anda perlu menyiapkan backend dan frontend secara terpisah.

### Backend (Python Flask)
1.  **Clone repository:**
    ```bash
    git clone [https://github.com/efzynx/booking-konsultasi-dosen.git](https://github.com/efzynx/booking-konsultasi-dosen.git)
    cd booking-konsultasi-dosen/backend # Arahkan ke folder backend
    ```
2.  **Buat virtual environment:**
    ```bash
    python -m venv venv
    source venv/bin/activate  # Untuk Windows: venv\Scripts\activate
    ```
3.  **Install dependencies:**
    ```bash
    pip install -r requirements.txt
    ```
4.  **Konfigurasi Database:**
    - Buat database baru di MariaDB/MySQL.
    - Ubah URI database di file konfigurasi (`config.py` atau sejenisnya).
5.  **Jalankan server:**
    ```bash
    flask run
    ```
    API akan berjalan di `http://127.0.0.1:5000`.

### Frontend (Android)
1.  Buka proyek di Android Studio.
2.  Buka file `RetrofitClient.kt` (atau file konfigurasi API Anda).
3.  Ubah `BASE_URL` menjadi alamat IP lokal dari mesin yang menjalankan backend (contoh: `http://192.168.1.5:5000/`).
4.  Build dan jalankan aplikasi di emulator atau perangkat fisik.

## 📝 Dokumentasi API
[Dokumentasi lengkap](https://github.com/efzynx/api-konsultasi)

| Metode | Endpoint             | Deskripsi                                   | Memerlukan Token? |
| :----- | :------------------- | :------------------------------------------ | :---------------: |
| `POST` | `/login`             | Login untuk mahasiswa dan dosen.            |        Tidak        |
| `POST` | `/register`          | Mendaftarkan akun baru untuk mahasiswa.     |        Tidak        |
| `GET`  | `/profile`           | Mendapatkan detail profil pengguna.         |         Ya          |
| `PUT`  | `/profile`           | Memperbarui profil pengguna.                |         Ya          |
| `GET`  | `/booking`           | Mendapatkan daftar jadwal (sesuai peran).   |         Ya          |
| `POST` | `/booking`           | Membuat jadwal konsultasi baru.             |         Ya          |
| `PUT`  | `/booking/<id>`      | Memperbarui jadwal (status/detail).         |         Ya          |
| `DELETE`| `/booking/<id>`     | Menghapus/membatalkan jadwal.               |         Ya          |

## 🧑‍💻 Author

- **Ahmad Fauzan Adiman** - [efzynx](https://github.com/efzynx)
- **Zainullah Multazam*** - [zainullahmultazam](https://github.com/zainullahmultazam)

---
Terima kasih telah mengunjungi repositori ini!

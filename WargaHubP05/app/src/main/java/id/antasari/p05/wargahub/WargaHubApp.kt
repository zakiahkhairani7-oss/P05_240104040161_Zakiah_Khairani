package id.antasari.p05.wargahub
import android.app.Application
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import id.antasari.p05.wargahub.data.LaporanRepository
import id.antasari.p05.wargahub.data.PengaturanPreferences
import id.antasari.p05.wargahub.data.WargaHubDatabase
class WargaHubApp : Application() {
    private val database by lazy {
        Room.databaseBuilder<WargaHubDatabase>(this, "wargahub-p05.db")
            .setDriver(AndroidSQLiteDriver()).build()
    }
    val repository by lazy { LaporanRepository(database.laporanDao()) }
    val preferences by lazy { PengaturanPreferences(this) }
} 
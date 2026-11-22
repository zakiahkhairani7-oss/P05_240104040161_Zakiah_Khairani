package id.antasari.p05.wargahub.data
import androidx.room3.Database
import androidx.room3.RoomDatabase
@Database(
    entities = [LaporanEntity::class],
    version = 1,
    exportSchema = true
)
abstract class WargaHubDatabase : RoomDatabase() {
    abstract fun laporanDao(): LaporanDao
}
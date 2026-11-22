package id.antasari.p05.wargahub.data
import androidx.room3.Entity
import androidx.room3.PrimaryKey
@Entity(tableName = "laporan")
data class LaporanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val judul: String,
    val kategori: String,
    val deskripsi: String
) 
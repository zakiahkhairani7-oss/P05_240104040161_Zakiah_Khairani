package id.antasari.p05.wargahub.data
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface LaporanDao {
    @Query("SELECT * FROM laporan ORDER BY id DESC")
    fun amatiSemua(): Flow<List<LaporanEntity>>
    @Query("SELECT * FROM laporan WHERE id = :id LIMIT 1")
    suspend fun baca(id: Long): LaporanEntity?
    @Insert
    suspend fun tambah(data: LaporanEntity): Long
    @Update
    suspend fun ubah(data: LaporanEntity): Int
    @Query("DELETE FROM laporan WHERE id = :id")
    suspend fun hapus(id: Long): Int
}

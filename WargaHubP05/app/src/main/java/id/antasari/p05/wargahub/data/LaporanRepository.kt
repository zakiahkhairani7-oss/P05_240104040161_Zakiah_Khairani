package id.antasari.p05.wargahub.data
class LaporanTidakAdaException : Exception("Laporan tidak ditemukan.")
class LaporanRepository(private val dao: LaporanDao) {
    val laporan = dao.amatiSemua()
    private val kategoriValid = setOf(
        "Jalan rusak", "Sampah menumpuk", "Lampu jalan padam",
        "Drainase tersumbat", "Lainnya"
    )
    suspend fun baca(id: Long): LaporanEntity? =
        if (id > 0L) dao.baca(id) else null
    suspend fun simpan(id: Long, j: String, k: String, d: String): Long {
        require(id >= 0L) { "ID laporan tidak valid." }
        val data = LaporanEntity(id, j.trim(), k, d.trim())
        require(data.judul.length >= 5) { "Judul minimal 5 karakter." }
        require(k in kategoriValid) { "Pilih kategori yang valid." }
        require(data.deskripsi.length >= 20) {
            "Deskripsi minimal 20 karakter."
        }
        if (id == 0L) return dao.tambah(data)
        if (dao.ubah(data) != 1) throw LaporanTidakAdaException()
        return id
    }
    suspend fun hapus(id: Long) {
        require(id > 0L) { "ID laporan tidak valid." }
        if (dao.hapus(id) != 1) throw LaporanTidakAdaException()
    }
}
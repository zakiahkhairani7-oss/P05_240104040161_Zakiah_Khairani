package id.antasari.p05.wargahub.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.antasari.p05.wargahub.data.LaporanEntity
import id.antasari.p05.wargahub.data.LaporanRepository
import id.antasari.p05.wargahub.data.LaporanTidakAdaException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
data class FormState(
    val memuat: Boolean = false,
    val data: LaporanEntity? = null,
    val dapatEdit: Boolean = true,
    val sibuk: Boolean = false,
    val pesan: String? = null,
    val selesai: Boolean = false
)
class FormViewModel(
    private val repo: LaporanRepository,
    private val id: Long
) : ViewModel() {
    private val _ui = MutableStateFlow(FormState(
        memuat = id != 0L, dapatEdit = id == 0L
    ))
    val ui = _ui.asStateFlow()
    init {
        if (id != 0L) viewModelScope.launch {
            try {
                val data = repo.baca(id)
                _ui.value = FormState(
                    data = data, dapatEdit = data != null,
                    pesan = if (data == null) "Laporan tidak ditemukan." else null
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _ui.value = FormState(
                    dapatEdit = false, pesan = "Gagal membaca laporan."
                )
            }
        }
    }
    fun simpan(j: String, k: String, d: String) = jalankan {
        repo.simpan(id, j, k, d)
    }
    fun hapus() {
        if (id > 0L) jalankan { repo.hapus(id) }
    }
    private fun jalankan(blok: suspend () -> Unit) {
        if (!_ui.value.dapatEdit || _ui.value.sibuk) return
        _ui.update { it.copy(sibuk = true, pesan = "Memproses...") }
        viewModelScope.launch {
            try {
                blok()
                _ui.update { it.copy(selesai = true) }
            } catch (e: CancellationException) {
                throw e   } catch (e: IllegalArgumentException) {
                _ui.update { it.copy(pesan = e.message ?: "Periksa isian.") }
            } catch (e: LaporanTidakAdaException) {
                _ui.update { it.copy(pesan = "Laporan tidak ditemukan.") }
            } catch (e: Exception) {
                _ui.update { it.copy(pesan = "Operasi gagal. Coba lagi.") }
            } finally {
                _ui.update { it.copy(sibuk = false) }
            }
        }
    }
}
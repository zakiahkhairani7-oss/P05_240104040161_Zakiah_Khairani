package id.antasari.p05.wargahub.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.antasari.p05.wargahub.data.LaporanEntity
import id.antasari.p05.wargahub.data.LaporanRepository
import id.antasari.p05.wargahub.data.PengaturanPreferences
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
data class DaftarState(
    val memuat: Boolean = true,
    val data: List<LaporanEntity> = emptyList(),
    val terbaru: Boolean = true,
    val preferensiSibuk: Boolean = false,
    val pesan: String? = null,
    val gagal: Boolean = false
)
class MainViewModel(
    private val repo: LaporanRepository,
    private val prefs: PengaturanPreferences
) : ViewModel() {
    private val _ui = MutableStateFlow(DaftarState())
    val ui = _ui.asStateFlow()
    private val terbaru = MutableStateFlow(true)
    init {
        viewModelScope.launch {
            try {
                terbaru.value = prefs.bacaTerbaru()
                combine(repo.laporan, terbaru) { data, baru ->
                    if (baru) data else data.sortedBy { it.id }
                }.collect { data ->
                    _ui.update { it.copy(
                        memuat = false, data = data,
                        terbaru = terbaru.value, gagal = false
                    ) }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _ui.update { it.copy(
                    memuat = false, gagal = true,
                    pesan = "Gagal membaca data. Buka ulang aplikasi."
                ) }
            }
        }
    }
    fun aturUrutan(aktif: Boolean) {
        if (_ui.value.memuat || _ui.value.preferensiSibuk) return
        _ui.update { it.copy(preferensiSibuk = true, pesan = null) }
        viewModelScope.launch {
            try {
                check(prefs.simpanTerbaru(aktif))
                terbaru.value = aktif
                _ui.update { it.copy(
                    terbaru = aktif, pesan = "Preferensi urutan disimpan."
                ) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _ui.update { it.copy(pesan = "Gagal menyimpan preferensi.") }
            } finally {
                _ui.update { it.copy(preferensiSibuk = false) }
            }
        }
    }
}
package id.antasari.p05.wargahub.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.antasari.p05.wargahub.WargaHubApp
class AppViewModelFactory(
    private val app: WargaHubApp,
    private val idLaporan: Long = 0L
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass == MainViewModel::class.java ->
                MainViewModel(app.repository, app.preferences) as T
            modelClass == FormViewModel::class.java ->
                FormViewModel(app.repository, idLaporan) as T
            else -> throw IllegalArgumentException("ViewModel tidak dikenal.")
        }
    }
} 
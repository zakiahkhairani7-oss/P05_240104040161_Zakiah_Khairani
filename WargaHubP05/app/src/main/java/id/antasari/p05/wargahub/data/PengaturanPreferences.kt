package id.antasari.p05.wargahub.data
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
class PengaturanPreferences(context: Context) {
    private val appContext = context.applicationContext
    private fun prefs() = appContext.getSharedPreferences(
        "id.antasari.p05.wargahub.pengaturan", Context.MODE_PRIVATE
    )
    suspend fun bacaTerbaru(): Boolean = withContext(Dispatchers.IO) {
        prefs().getBoolean("urut_terbaru", true)
    }
    suspend fun simpanTerbaru(aktif: Boolean): Boolean =
        withContext(Dispatchers.IO) {
            prefs().edit().putBoolean("urut_terbaru", aktif).commit()
        }
}
package id.antasari.p05.wargahub.ui
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
fun aturAreaAman(root: View) {
    ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
        val bars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or
                    WindowInsetsCompat.Type.displayCutout()
        )
        val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
        view.setPadding(bars.left, bars.top, bars.right, maxOf(bars.bottom, ime.bottom))
        insets
    }
    ViewCompat.requestApplyInsets(root)
} 
package id.antasari.p05.wargahub
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.view.isVisible
import id.antasari.p05.wargahub.adapter.LaporanAdapter
import id.antasari.p05.wargahub.databinding.ActivityMainBinding
import id.antasari.p05.wargahub.ui.AppViewModelFactory
import id.antasari.p05.wargahub.ui.MainViewModel
import id.antasari.p05.wargahub.ui.aturAreaAman
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vm: MainViewModel by viewModels {
        AppViewModelFactory(application as WargaHubApp)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        aturAreaAman(binding.mainRoot)
        val adapter = LaporanAdapter { id -> bukaForm(id) }
        binding.rvLaporan.layoutManager = LinearLayoutManager(this)
        binding.rvLaporan.adapter = adapter
        binding.btnTambah.setOnClickListener { bukaForm(0L) }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.ui.collect { state ->
                    adapter.submitList(state.data)
                    binding.tvStatus.text = when {
                        state.memuat -> getString(R.string.memuat)
                        state.gagal -> state.pesan
                        state.preferensiSibuk -> "Menyimpan preferensi..."
                        state.data.isEmpty() -> getString(R.string.daftar_kosong)
                        else -> getString(R.string.jumlah_laporan, state.data.size)
                    }
                    binding.tvPreferensi.text = state.pesan.orEmpty()
                    binding.tvPreferensi.isVisible = !state.gagal &&
                            state.pesan != null
                    binding.cbTerbaru.setOnCheckedChangeListener(null)
                    binding.cbTerbaru.isChecked = state.terbaru
                    binding.cbTerbaru.isEnabled = !state.memuat &&
                            !state.preferensiSibuk && !state.gagal
                    binding.cbTerbaru.setOnCheckedChangeListener { _, aktif ->
                        vm.aturUrutan(aktif)
                    }
                }
            }
        }
    }
    private fun bukaForm(id: Long) {
        startActivity(Intent(this, FormActivity::class.java)
            .putExtra(FormActivity.EXTRA_ID, id))
    }
}
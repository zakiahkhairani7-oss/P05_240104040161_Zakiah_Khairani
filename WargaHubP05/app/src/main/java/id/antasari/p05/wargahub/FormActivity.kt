package id.antasari.p05.wargahub
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import id.antasari.p05.wargahub.databinding.ActivityFormBinding
import id.antasari.p05.wargahub.ui.AppViewModelFactory
import id.antasari.p05.wargahub.ui.FormState
import id.antasari.p05.wargahub.ui.FormViewModel
import id.antasari.p05.wargahub.ui.aturAreaAman
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
class FormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormBinding
    private var sudahIsi = false
    private val idLaporan get() = intent.getLongExtra(EXTRA_ID, 0L)
    private val vm: FormViewModel by viewModels {
        AppViewModelFactory(application as WargaHubApp, idLaporan)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        aturAreaAman(binding.formRoot)
        sudahIsi = savedInstanceState?.getBoolean(KEY_TERISI) ?: false
        binding.tvPetunjuk.setText(
            if (idLaporan == 0L) R.string.info_tambah else R.string.info_ubah
        )
        binding.btnSimpan.setOnClickListener { validasiDanSimpan() }
        binding.btnKembali.setOnClickListener { finish() }
        onBackPressedDispatcher.addCallback(this) {
            if (!vm.ui.value.sibuk) finish()
        }
        binding.btnHapus.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(R.string.konfirmasi_hapus)
                .setPositiveButton(R.string.ya_hapus) { _, _ -> vm.hapus() }
                .setNegativeButton(R.string.batal, null).show()
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.ui.collect { state -> tampilkan(state) }
            }
        }
    }
    private fun tampilkan(state: FormState) {
        if (state.selesai) { finish(); return }
        if (!state.memuat && state.dapatEdit && !sudahIsi) {
            state.data?.let { data ->
                binding.etJudul.setText(data.judul)
                binding.etDeskripsi.setText(data.deskripsi)
                val opsi = resources.getStringArray(R.array.kategori_laporan)
                binding.spinnerKategori.setSelection(
                    opsi.indexOf(data.kategori).coerceAtLeast(0)
                )
            }
            sudahIsi = true
        }
        val aktif = !state.memuat && state.dapatEdit && !state.sibuk
        binding.etJudul.isEnabled = aktif
        binding.etDeskripsi.isEnabled = aktif
        binding.spinnerKategori.isEnabled = aktif
        binding.btnSimpan.isEnabled = aktif
        binding.btnHapus.visibility = if (idLaporan > 0L) View.VISIBLE else View.GONE
        binding.btnHapus.isEnabled = aktif
        binding.btnKembali.isEnabled = !state.sibuk
        binding.tvStatus.text = when {
            state.memuat -> getString(R.string.memuat)
            else -> state.pesan ?: getString(R.string.catatan_lokal)
        }
    }
    private fun validasiDanSimpan() {
        binding.etJudul.error = null
        binding.etDeskripsi.error = null
        val j = binding.etJudul.text.toString().trim()
        val d = binding.etDeskripsi.text.toString().trim()
        val posisi = binding.spinnerKategori.selectedItemPosition
        when {
            j.length < 5 -> {
                binding.etJudul.error = getString(R.string.error_judul)
                binding.etJudul.requestFocus()
            }
            posisi <= 0 -> {
                binding.tvStatus.setText(R.string.error_kategori)
                binding.spinnerKategori.requestFocus()
            }
            d.length < 20 -> {
                binding.etDeskripsi.error = getString(R.string.error_deskripsi)
                binding.etDeskripsi.requestFocus()
            }
            else -> vm.simpan(j, binding.spinnerKategori.selectedItem.toString(), d)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_TERISI, sudahIsi)
        super.onSaveInstanceState(outState)
    }
    companion object {
        const val EXTRA_ID = "laporan_id"
        private const val KEY_TERISI = "form_terisi"
    }
}
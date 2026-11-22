package id.antasari.p05.wargahub.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.antasari.p05.wargahub.R
import id.antasari.p05.wargahub.data.LaporanEntity
import id.antasari.p05.wargahub.databinding.ItemLaporanBinding
class LaporanAdapter(private val pilih: (Long) -> Unit) :
    ListAdapter<LaporanEntity, LaporanAdapter.Holder>(Pembanding) {
    class Holder(private val b: ItemLaporanBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun bind(data: LaporanEntity, pilih: (Long) -> Unit) {
            b.tvJudul.text = data.judul
            b.tvInfo.text = b.root.context.getString(
                R.string.row_info, data.id, data.kategori
            )
            b.root.contentDescription = b.root.context.getString(
                R.string.row_a11y, data.judul, data.id, data.kategori
            )
            b.root.setOnClickListener { pilih(data.id) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemLaporanBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), pilih)
    }
    companion object {
        private val Pembanding = object : DiffUtil.ItemCallback<LaporanEntity>() {
            override fun areItemsTheSame(a: LaporanEntity, b: LaporanEntity) =
                a.id == b.id
            override fun areContentsTheSame(a: LaporanEntity, b: LaporanEntity) =
                a == b
        }
    }
}

package com.example.ugd.PesananAdapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd.AddEditPesananActivity
import com.example.ugd.PesananActivity
import com.example.ugd.R
import com.example.ugd.models.Pesanan
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class pesananAdapter (private var pesananList: List<Pesanan>, context: Context) :
    RecyclerView.Adapter<pesananAdapter.ViewHolder> (), Filterable {

    private var filteredPesananList: MutableList<Pesanan>
    private val context: Context

    init {
        filteredPesananList = ArrayList(pesananList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_pesanan, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredPesananList.size
    }

    fun setPesananList(mahasiswaList: Array<Pesanan>){
        this.pesananList = mahasiswaList.toList()
        filteredPesananList = mahasiswaList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val pesanan = filteredPesananList[position]
        holder.tv_namapemesan.text = pesanan.nama
        holder.tv_jenisPesanan.text = pesanan.jenisPesanan
        holder.tv_rincian.text = pesanan.rincian

        holder.btnDelete.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data mahasiswa ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ ->
                    if (context is PesananActivity) pesanan.id?.let { it1 ->
                        context.deletePesanan(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvPesanan.setOnClickListener {
            val i = Intent(context, AddEditPesananActivity::class.java)
            i.putExtra("id", pesanan.id)
            if (context is PesananActivity)
                context.startActivityForResult(i, PesananActivity.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Pesanan> = java.util.ArrayList()
                if (charSequenceString.isEmpty()){
                    filtered.addAll(pesananList)
                } else {
                    for (pesanan in pesananList){
                        if (pesanan.nama.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(pesanan)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredPesananList.clear()
                filteredPesananList.addAll((filterResults.values as List<Pesanan>))
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tv_namapemesan: TextView
        var tv_jenisPesanan: TextView
        var tv_rincian: TextView
        var btnDelete: ImageButton
        var cvPesanan: CardView

        init {
            tv_namapemesan = itemView.findViewById(R.id.tv_namapemesan)
            tv_jenisPesanan = itemView.findViewById(R.id.tv_jenisPesanan)
            tv_rincian = itemView.findViewById(R.id.tv_rincian)
            btnDelete = itemView.findViewById(R.id.btn_delete)
            cvPesanan = itemView.findViewById(R.id.cv_pesanan)
        }
    }
}

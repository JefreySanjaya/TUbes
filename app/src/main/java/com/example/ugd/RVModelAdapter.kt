package com.example.ugd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ugd.entity.Model


class RVModelAdapter (private  val data: Array<Model>) : RecyclerView.Adapter<RVModelAdapter.viewHolder>() {

    class viewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        val tvModel: TextView = itemView.findViewById(R.id.tv_model)
        val tvDetailsModel: TextView = itemView.findViewById(R.id.tv_details_model)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVModelAdapter.viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_model,parent,false)
        return RVModelAdapter.viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVModelAdapter.viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvModel.text=currentItem.name
        holder.tvDetailsModel.text=currentItem.harga
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
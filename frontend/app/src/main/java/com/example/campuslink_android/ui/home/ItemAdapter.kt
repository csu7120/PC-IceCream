package com.example.campuslink_android.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink_android.R
import com.example.campuslink_android.domain.model.Item

class ItemAdapter(
    private var items: List<Item> = emptyList(),
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    fun submitList(newItems: List<Item>) {
        items = newItems
        notifyDataSetChanged()
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        val txtStatus: TextView = view.findViewById(R.id.txtStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_simple, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.txtTitle.text = item.title
        holder.txtPrice.text = "${item.price}원"

        val statusText = when (item.status) {
            "RENTABLE" -> "대여 가능"
            "RENTED"   -> "대여 중"
            else       -> "상태 미정"
        }
        holder.txtStatus.text = statusText

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}

package com.example.campuslink_android.ui.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink_android.R
import com.example.campuslink_android.domain.model.Item

class MyItemAdapter(
    private val onClick: (Item) -> Unit
) : RecyclerView.Adapter<MyItemAdapter.Holder>() {

    private var items: List<Item> = emptyList()

    fun submitList(list: List<Item>) {
        items = list
        notifyDataSetChanged()
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtCategory: TextView = view.findViewById(R.id.txtCategory)
        val txtPrice: TextView = view.findViewById(R.id.txtPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_item, parent, false)
        return Holder(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.txtTitle.text = item.title
        holder.txtCategory.text = item.category
        holder.txtPrice.text = "${item.price}원"

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }
}

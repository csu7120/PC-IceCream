package com.example.campuslink_android.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        // ⭐ XML ID를 txt... 로 맞췄습니다.
        val imgItem: ImageView = view.findViewById(R.id.itemImage) // 이미지는 새로 추가된 ID
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
        holder.txtPrice.text = "${item.price.toInt()}원"

        // 디자인 로직 (색상 변경)
        val (statusText, colorCode) = when (item.status) {
            "RENTABLE" -> Pair("[대여가능]", "#0055A5")
            "RENTED"   -> Pair("[대여중]", "#888888")
            else       -> Pair("[상태미정]", "#888888")
        }
        holder.txtStatus.text = statusText
        holder.txtStatus.setTextColor(Color.parseColor(colorCode))

        // 이미지 로딩
        val imageUrlToLoad = item.thumbnailUrl ?: item.imageUrl
        if (!imageUrlToLoad.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(imageUrlToLoad)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgItem)
        } else {
            holder.imgItem.setImageResource(R.drawable.ic_launcher_background)
        }

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}
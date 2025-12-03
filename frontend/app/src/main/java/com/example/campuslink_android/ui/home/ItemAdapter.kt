package com.example.campuslink_android.ui.home

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.campuslink_android.R
import com.example.campuslink_android.domain.model.Item

class ItemAdapter(
    private var items: List<Item> = emptyList(),
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    fun submitList(newItems: List<Item>) {
        items = newItems
        // ⭐ [수정됨] 데이터 수신 확인 로그 추가
        Log.d("ItemDebug", "Adapter received list of size: ${newItems.size}")
        notifyDataSetChanged()
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgItem: ImageView = view.findViewById(R.id.itemImage)
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

        val (statusText, colorCode) = when (item.status) {
            "RENTABLE" -> Pair("[대여가능]", "#0055A5")
            "RENTED"   -> Pair("[대여중]", "#888888")
            else       -> Pair("[상태미정]", "#888888")
        }
        holder.txtStatus.text = statusText
        holder.txtStatus.setTextColor(Color.parseColor(colorCode))

        // ⭐ 이미지 로딩 (URL 생성 및 로그)
        val rawUrl = item.thumbnailUrl ?: item.imageUrl

        if (!rawUrl.isNullOrEmpty()) {
            val fullUrl = if (rawUrl.startsWith("/uploads/")) {
                "http://10.0.2.2:8080$rawUrl"
            } else {
                rawUrl
            }

            Log.d("ItemImage", "로딩 시도 URL: $fullUrl")

            Glide.with(holder.itemView.context)
                .load(fullUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
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
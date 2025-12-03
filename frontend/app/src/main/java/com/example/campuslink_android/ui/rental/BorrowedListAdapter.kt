package com.example.campuslink_android.ui.rental

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campuslink_android.R
import com.example.campuslink_android.data.dto.RentalResponseDto

class BorrowedListAdapter(
    private val onClick: (RentalResponseDto) -> Unit
) : RecyclerView.Adapter<BorrowedListAdapter.ViewHolder>() {

    private var items: List<RentalResponseDto> = emptyList()

    fun submitList(list: List<RentalResponseDto>) {
        this.items = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgItem: ImageView = view.findViewById(R.id.itemImage)
        private val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        private val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        private val txtStatus: TextView = view.findViewById(R.id.txtStatus)

        fun bind(item: RentalResponseDto) {

            // 제목
            txtTitle.text = item.itemTitle ?: "제목 없음"

            // 🔥 수정됨 — price → itemOriginalPrice
            txtPrice.text = "대여요금: ${(item.itemOriginalPrice ?: 0)}원"

            // 상태
            txtStatus.text = "상태: ${item.status}"

            // 이미지 로딩
            val rawUrl = item.itemImageUrl
            if (!rawUrl.isNullOrEmpty()) {
                val fullUrl = if (rawUrl.startsWith("/uploads/")) {
                    "http://10.0.2.2:8080$rawUrl"
                } else rawUrl

                Glide.with(itemView.context)
                    .load(fullUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(imgItem)
            } else {
                imgItem.setImageResource(R.drawable.ic_launcher_background)
            }

            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_simple, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int = items.size
}

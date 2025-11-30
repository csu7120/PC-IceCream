package com.example.campuslink_android.ui.rental

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink_android.R
import com.example.campuslink_android.data.dto.RentalResponseDto

class BorrowedListAdapter(
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<BorrowedListAdapter.ViewHolder>() {

    private var items: List<RentalResponseDto> = emptyList()

    fun submitList(list: List<RentalResponseDto>) {
        this.items = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        private val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        private val txtStatus: TextView = view.findViewById(R.id.txtStatus)

        fun bind(item: RentalResponseDto) {
            txtTitle.text = "Rental ID: ${item.rentalId} / Item: ${item.itemId}"
            txtPrice.text = "대여요금: ${item.Price}원"
            txtStatus.text = "상태: ${item.status}"

            itemView.setOnClickListener { onClick(item.rentalId) }
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

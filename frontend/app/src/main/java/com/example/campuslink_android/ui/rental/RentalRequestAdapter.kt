package com.example.campuslink_android.ui.rental

import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink_android.R
import com.example.campuslink_android.data.dto.RentalResponseDto

class RentalRequestAdapter(
    private val onAcceptClick: (Int) -> Unit
) : RecyclerView.Adapter<RentalRequestAdapter.Holder>() {

    private var items: List<RentalResponseDto> = emptyList()

    fun submitList(list: List<RentalResponseDto>) {
        items = list
        notifyDataSetChanged()
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val txt: TextView = view.findViewById(R.id.txtRentalItem)
        val btnAccept: Button = view.findViewById(R.id.btnAcceptFromList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rental_request, parent, false)
        return Holder(v)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = items[position]

        holder.txt.text = "Rental ID: ${data.rentalId} / Item: ${data.itemId}"

        holder.btnAccept.setOnClickListener {
            onAcceptClick(data.rentalId)
        }
    }
}

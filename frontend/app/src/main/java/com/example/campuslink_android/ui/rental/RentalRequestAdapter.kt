package com.example.campuslink_android.ui.rental

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink_android.R
import com.example.campuslink_android.data.dto.RentalResponseDto

class RentalRequestAdapter(
    // 1) 수락 버튼 클릭 콜백
    private val onAcceptClick: (Int) -> Unit,
    // 2) 수락 버튼을 보여줄지 여부 (기본 true)
    private val showAcceptButton: Boolean = true
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

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = items[position]

        holder.txt.text =
            "Rental ID: ${data.rentalId}\n" +
                    "Item ID: ${data.itemId}\n" +
                    "기간: ${data.startAt} ~ ${data.endAt}"

        // 🔥 핵심 로직: showAcceptButton 값에 따라 버튼 숨기기
        if (showAcceptButton && data.status == "REQUESTED") {
            holder.btnAccept.visibility = View.VISIBLE
            holder.btnAccept.setOnClickListener {
                Log.d("RentalRequestAdapter", "accept click rentalId=${data.rentalId}")
                onAcceptClick(data.rentalId)
            }
        } else {
            holder.btnAccept.visibility = View.GONE
            holder.btnAccept.setOnClickListener(null)
        }
    }
}

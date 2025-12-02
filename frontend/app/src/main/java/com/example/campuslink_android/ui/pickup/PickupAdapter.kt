package com.example.campuslink_android.ui.pickup

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink_android.R
import com.example.campuslink_android.data.dto.RentalResponseDto

class PickupAdapter(
    private val onClick: (RentalResponseDto) -> Unit
) : RecyclerView.Adapter<PickupAdapter.ViewHolder>() {

    private val items = mutableListOf<RentalResponseDto>()

    fun submitList(list: List<RentalResponseDto>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // ⭐ [핵심 수정] 여기서 item_pickup(옛날 파일) 대신 fragment_pickup_list(새 디자인)을 연결해야 합니다!
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_pickup_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        // ⭐ [수정] 새 디자인 파일에 있는 ID들로 연결
        private val txtRentalItem: TextView = view.findViewById(R.id.txtRentalItem)
        private val txtPickupTime: TextView = view.findViewById(R.id.txtPickupTime)
        private val txtStatus: TextView = view.findViewById(R.id.txtStatus)
        private val btnConfirmPickup: Button = view.findViewById(R.id.btnConfirmPickup)

        fun bind(item: RentalResponseDto) {
            // 데이터 설정
            txtRentalItem.text = "물품 ID: ${item.itemId}" // 나중에 item.title로 변경 가능
            txtStatus.text = item.status
            txtPickupTime.text = "픽업 대기중" // 필요한 경우 실제 날짜 데이터 연결

            // 버튼 클릭 이벤트
            btnConfirmPickup.setOnClickListener { onClick(item) }
        }
    }
}
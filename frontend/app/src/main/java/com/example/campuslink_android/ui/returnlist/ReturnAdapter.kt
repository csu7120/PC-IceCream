package com.example.campuslink_android.ui.returnlist

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.Button // Button import 추가
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink_android.R
import com.example.campuslink_android.data.dto.RentalResponseDto

class ReturnAdapter(
    private val onClick: (RentalResponseDto) -> Unit
) : RecyclerView.Adapter<ReturnAdapter.ViewHolder>() {

    private val items = mutableListOf<RentalResponseDto>()

    fun submitList(list: List<RentalResponseDto>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // item_return.xml을 레이아웃으로 사용
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_return, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        // ⭐ XML의 ID들과 연결 (tvName, btnReturnAction)
        private val tvName: TextView = view.findViewById(R.id.tvName)
        private val btnReturnAction: Button = view.findViewById(R.id.btnReturnAction)

        // 상태 표시용 (필요하면 사용)
        private val txtStatus: TextView = view.findViewById(R.id.txtStatus)

        fun bind(item: RentalResponseDto) {
            // 1. 텍스트 바인딩
            tvName.text = "물품 ID: ${item.itemId}"
            txtStatus.text = "상태: ${item.status}"

            // 2. ⭐ [핵심] 버튼에 클릭 리스너 연결
            // 이제 '반납하기' 버튼을 눌러야만 onClick이 실행됩니다.
            btnReturnAction.setOnClickListener {
                onClick(item)
            }
        }
    }
}
package com.example.campuslink_android.ui.review

import android.app.Dialog
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.campuslink_android.R
import com.example.campuslink_android.data.dto.RentalResponseDto
import com.example.campuslink_android.ui.report.ReportDialog

class ReviewDialog(
    context: Context,
    private val rental: RentalResponseDto,
    private val roleType: String,
    private val viewModel: ReviewViewModel
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_review)

        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val editComment = findViewById<EditText>(R.id.editComment)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val tvReport = findViewById<TextView>(R.id.tvReport)
        val btnClose = findViewById<ImageView>(R.id.btnClose)

        ratingBar.rating = 5.0f
        ratingBar.stepSize = 1.0f
        ratingBar.setIsIndicator(false)

        btnClose?.setOnClickListener {
            dismiss()
        }

        tvReport?.setOnClickListener {
            // ✅ 수정된 부분: getActivity() 함수를 사용해 진짜 액티비티를 찾아냅니다.
            val activity = getActivity(context)

            if (activity != null) {
                val targetUserId = if (roleType == "LENDER") {
                    rental.renterId
                } else {
                    rental.lenderId
                }

                // 이름 정보가 없으므로 "거래 상대방" 등으로 고정
                val targetUserName = "거래 상대방"

                val reportDialog = ReportDialog.newInstance(targetUserId, targetUserName)
                reportDialog.show(activity.supportFragmentManager, "ReportDialog")
            } else {
                // 여전히 못 찾았을 경우 로그 확인용
                Toast.makeText(context, "신고창을 열 수 없습니다. (Activity Not Found)", Toast.LENGTH_SHORT).show()
            }
        }

        btnSubmit?.setOnClickListener {
            val rating = ratingBar.rating.toInt()
            val comment = editComment.text.toString()
            viewModel.submitReview(rental, rating.toDouble(), comment, roleType)
            dismiss()
        }
    }

    // ✅ 핵심: 감싸진 Context 껍질을 벗겨서 진짜 Activity를 찾는 함수
    private fun getActivity(context: Context?): FragmentActivity? {
        if (context == null) return null
        if (context is FragmentActivity) return context
        if (context is ContextWrapper) return getActivity(context.baseContext)
        return null
    }
}
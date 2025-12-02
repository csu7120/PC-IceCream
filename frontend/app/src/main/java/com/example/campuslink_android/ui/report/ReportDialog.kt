package com.example.campuslink_android.ui.report

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.campuslink_android.core.network.TokenStore // TokenStore import 필수
import com.example.campuslink_android.databinding.DialogReportBinding
import com.example.campuslink_android.ui.common.ViewModelFactory

class ReportDialog : DialogFragment() {

    private var _binding: DialogReportBinding? = null
    private val binding get() = _binding!!

    // ViewModel 연결
    private val reportViewModel: ReportViewModel by viewModels { ViewModelFactory(requireContext()) }

    private var targetUserId: Int = -1
    private var targetUserName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogReportBinding.inflate(inflater, container, false)

        // 배경을 투명하게 해서 둥근 모서리가 보이게 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달받은 데이터 꺼내기
        arguments?.let {
            targetUserId = it.getInt("targetUserId")
            targetUserName = it.getString("targetUserName", "사용자")
        }

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        // UI 초기화
        binding.tvTargetUser.text = "'$targetUserName'님을 신고합니다."

        // 닫기 버튼
        binding.btnCloseReport.setOnClickListener {
            dismiss()
        }

        // 신고하기 버튼
        binding.btnSubmitReport.setOnClickListener {
            val reason = binding.editReportReason.text.toString()

            // 1. 사유 입력 확인
            if (reason.isBlank()) {
                Toast.makeText(context, "신고 사유를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. 내 ID 가져오기 (TokenStore 활용)
            val myId = TokenStore.getUserId()

            if (myId != null) {
                // 로그인 정보가 있으면 API 호출
                reportViewModel.reportUser(myId, targetUserId, reason)
            } else {
                // 로그인 정보가 없으면 에러 메시지
                Toast.makeText(context, "로그인 정보가 유효하지 않습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        // 성공 여부 관찰
        reportViewModel.reportResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "신고가 정상적으로 접수되었습니다.", Toast.LENGTH_SHORT).show()
                dismiss() // 다이얼로그 닫기
            }
        }

        // 에러 메시지 관찰
        reportViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 외부에서 이 다이얼로그를 띄울 때 사용할 편의 함수
    companion object {
        fun newInstance(targetUserId: Int, targetUserName: String): ReportDialog {
            val args = Bundle().apply {
                putInt("targetUserId", targetUserId)
                putString("targetUserName", targetUserName)
            }
            return ReportDialog().apply {
                arguments = args
            }
        }
    }
}
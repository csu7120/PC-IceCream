package com.example.campuslink_android.ui.Setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.campuslink_android.R
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.UserApi
import com.example.campuslink_android.data.repository.UserRepositoryImpl
import com.example.campuslink_android.domain.repository.UserRepository
import com.example.campuslink_android.ui.auth.login.LoginActivity

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var viewModel: SettingsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tokenStore = TokenStore
        val userApi = ApiClient.create(UserApi::class.java)
        val userRepository: UserRepository = UserRepositoryImpl(userApi, tokenStore)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModelFactory(userRepository)
        )[SettingsViewModel::class.java]

        // ✔ XML의 카드뷰 아이디와 연결
        val cardWithdraw = view.findViewById<CardView>(R.id.cardWithdraw)

        // ✔ 탈퇴 버튼 클릭 이벤트
        cardWithdraw.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("회원 탈퇴")
                .setMessage("정말 탈퇴하시겠습니까?\n삭제 후 복구가 불가능합니다.")
                .setPositiveButton("탈퇴") { _, _ ->
                    viewModel.deleteAccount()
                }
                .setNegativeButton("취소", null)
                .show()
        }

        // ✔ 탈퇴 결과 처리
        viewModel.deleteResult.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()

            if (msg.contains("완료")) {
                tokenStore.clear()

                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)

                requireActivity().finish()
            }
        }
    }
}

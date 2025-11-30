package com.example.campuslink_android.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.campuslink_android.R
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.ui.auth.login.LoginActivity   // ✅ 이거만 사용

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by lazy {
        ProfileViewModel.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imgProfile = view.findViewById<ImageView>(R.id.imgProfile)
        val txtName = view.findViewById<TextView>(R.id.txtName)
        val txtEmail = view.findViewById<TextView>(R.id.txtEmail)
        val txtUserId = view.findViewById<TextView>(R.id.txtUserId)
        val txtError = view.findViewById<TextView>(R.id.txtError)

        val btnMyItems = view.findViewById<Button>(R.id.btnMyItems)
        val btnRentalRequests = view.findViewById<Button>(R.id.btnRentalRequests)

        val btnBorrowed = view.findViewById<Button>(R.id.btnBorrowed)
        val btnLent = view.findViewById<Button>(R.id.btnLent)
        val btnSettings = view.findViewById<Button>(R.id.btnSettings)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        viewModel.loadMyInfo()

        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                txtName.text = it.name ?: "(이름 없음)"
                txtEmail.text = it.email ?: "(이메일 없음)"
                txtUserId.text = "User ID: ${it.id}"
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { msg ->
            txtError.text = msg ?: ""
        })

        // 내가 올린 물품
        btnMyItems.setOnClickListener {
            findNavController().navigate(R.id.myItemListFragment)
        }

        // 대여 요청 목록 (= 내가 빌려준 목록 화면 재사용)
        btnRentalRequests.setOnClickListener {
            findNavController().navigate(R.id.rentalRequestListFragment)
        }

        // 내가 빌린 목록
        btnBorrowed.setOnClickListener {
            findNavController().navigate(R.id.borrowedListFragment)
        }

        // 내가 빌려준 목록 → 별도 Fragment 안 만들고 요청 목록 화면 재사용
        btnLent.setOnClickListener {
            findNavController().navigate(R.id.rentalRequestListFragment)
        }

        // 설정
        btnSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        // 로그아웃
        btnLogout.setOnClickListener {
            TokenStore.clear()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyInfo()
    }
}

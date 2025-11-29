package com.example.campuslink_android.ui.profile

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

        viewModel.loadMyInfo()

        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                val nameText = it.name ?: "(이름 없음)"
                val emailText = it.email ?: "(이메일 없음)"

                txtName.text = nameText
                txtEmail.text = emailText
                txtUserId.text = "User ID: ${it.id}"

            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { msg ->
            txtError.text = msg ?: ""
        })

        btnMyItems.setOnClickListener {
            // ✅ 내 물품 전용 프래그먼트로 이동
            findNavController().navigate(R.id.myItemListFragment)
        }


        btnRentalRequests.setOnClickListener {
            findNavController().navigate(R.id.rentalRequestListFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyInfo()
    }
}

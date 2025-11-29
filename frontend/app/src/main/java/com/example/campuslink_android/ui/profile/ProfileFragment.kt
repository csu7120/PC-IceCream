package com.example.campuslink_android.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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

        val txtUserInfo = view.findViewById<TextView>(R.id.txtUserInfo)
        val txtError = view.findViewById<TextView>(R.id.txtError)

        viewModel.loadMyInfo()

        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                val emailText = it.email ?: "(이메일 없음)"
                val nameText = it.name ?: "(이름 없음)"

                txtUserInfo.text = "ID: ${it.id}\nName: $nameText\nEmail: $emailText"
            }
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { msg ->
            txtError.text = msg ?: ""
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyInfo()
    }
}

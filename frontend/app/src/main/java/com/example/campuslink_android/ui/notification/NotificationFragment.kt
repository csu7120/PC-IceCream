package com.example.campuslink_android.ui.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campuslink_android.R
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.NotificationApi
import com.example.campuslink_android.data.repository.NotificationRepositoryImpl
import com.example.campuslink_android.databinding.FragmentNotificationsBinding

class NotificationFragment : Fragment(R.layout.fragment_notifications) {

    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var viewModel: NotificationViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationsBinding.bind(view)

        val api = ApiClient.create(NotificationApi::class.java)
        val repo = NotificationRepositoryImpl(api, TokenStore)
        val factory = NotificationViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[NotificationViewModel::class.java]

        val adapter = NotificationAdapter { noti ->
            viewModel.markAsRead(noti.notificationId)
        }

        binding.recyclerViewNotifications.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewNotifications.adapter = adapter

        viewModel.notifications.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.loadNotifications()
        viewModel.loadUnreadCount()
    }
    override fun onPause() {
        super.onPause()
        // 홈 상단 뱃지 리프레시
        viewModel.loadUnreadCount()
    }

}

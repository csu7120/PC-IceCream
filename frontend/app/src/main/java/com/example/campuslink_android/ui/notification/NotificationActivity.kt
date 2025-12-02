package com.example.campuslink_android.ui.notification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campuslink_android.R
import com.example.campuslink_android.data.dto.NotificationResponseDto
import com.example.campuslink_android.databinding.ActivityNotificationsBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsBinding
    private lateinit var viewModel: NotificationViewModel
    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = NotificationViewModel.create()

        adapter = NotificationAdapter { notification ->
            viewModel.markAsRead(notification.notificationId)
        }

        binding.rvNotifications.layoutManager = LinearLayoutManager(this)
        binding.rvNotifications.adapter = adapter

        viewModel.notifications.observe(this) { list ->
            adapter.submitList(list)
        }

        viewModel.loadNotifications()
    }
}

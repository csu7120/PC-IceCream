package com.example.campuslink_android.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.ChatApi
import com.example.campuslink_android.data.repository.ChatRepositoryImpl
import com.example.campuslink_android.databinding.FragmentChatListBinding

class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ChatListAdapter

    private val viewModel: ChatListViewModel by activityViewModels {
        ChatListViewModelFactory(
            ChatRepositoryImpl(
                ApiClient.create(ChatApi::class.java),   // ⭐ AuthInterceptor 포함된 Retrofit
                TokenStore                                // ⭐ 객체 그대로 넘김 (정답)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadChatRooms()
    }

    private fun setupRecyclerView() {
        adapter = ChatListAdapter { chatRoom ->
            ChatRoomActivity.start(requireContext(), chatRoom.chatId)
        }

        binding.rvChatList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChatList.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.chatRooms.observe(viewLifecycleOwner) { rooms ->

            Log.d("ChatListFragment", "🖥 received rooms = $rooms")
            Log.d("ChatListFragment", "🖥 received rooms size = ${rooms?.size}")

            if (rooms.isNullOrEmpty()) {
                binding.tvEmptyMessage.visibility = View.VISIBLE
                binding.rvChatList.visibility = View.GONE
            } else {
                binding.tvEmptyMessage.visibility = View.GONE
                binding.rvChatList.visibility = View.VISIBLE
                adapter.submitList(rooms)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

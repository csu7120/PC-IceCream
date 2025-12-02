package com.example.campuslink_android.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campuslink_android.domain.repository.ChatRepository
import com.example.campuslink_android.data.repository.ChatRepositoryImpl
import com.example.campuslink_android.data.dao.ChatApi
import com.example.campuslink_android.data.dto.ChatRoomResponseDto
import com.example.campuslink_android.databinding.FragmentChatListBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.campuslink_android.core.network.TokenStore
class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ChatListAdapter

    // ▶ ViewModel 생성 (Retrofit + Repository 포함)
    private val viewModel: ChatListViewModel by viewModels {
        ChatListViewModelFactory(
            ChatRepositoryImpl(createChatApi(), TokenStore)
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
        viewModel.loadChatRooms()   // 화면 돌아올 때마다 목록 다시 로딩
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

    private fun createChatApi(): ChatApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")   // ← 현재 백엔드 주소에 맞게 수정
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ChatApi::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

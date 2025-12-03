package com.example.campuslink_android.ui.rental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink_android.R
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.RentalApi
import com.example.campuslink_android.data.repository.RentalRepositoryImpl
import androidx.lifecycle.lifecycleScope   // ← 추가됨
import kotlinx.coroutines.launch         // ← 추가됨
import android.util.Log
import android.widget.Toast
class RentalRequestListFragment : Fragment() {

    private lateinit var viewModel: RentalListViewModel
    private lateinit var adapter: RentalRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_rental_request_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rentalApi = ApiClient.create(RentalApi::class.java)
        val rentalRepository = RentalRepositoryImpl(rentalApi, TokenStore)
        val factory = RentalListViewModelFactory(rentalRepository)
        viewModel = ViewModelProvider(this, factory)[RentalListViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvRentalRequests).apply {
            layoutManager = LinearLayoutManager(requireContext())
        }

        adapter = RentalRequestAdapter(
            onAcceptClick = { rentalId ->
                viewModel.acceptRental(rentalId)
            },
            showAcceptButton = true
        )



        recyclerView.adapter = adapter

        // 리스트 옵저버
        viewModel.list.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        // ⭐⭐⭐ 수락 결과 옵저버 — 반드시 추가해야 UI가 갱신됨!!!
        viewModel.acceptResult.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Toast.makeText(requireContext(), "대여 수락 완료!", Toast.LENGTH_SHORT).show()

                // 목록 새로고침
                viewModel.loadRequestedRentals()
            } else {
                Toast.makeText(requireContext(), "대여 수락 실패", Toast.LENGTH_SHORT).show()
            }
        }

        // 초기 목록 로드
        viewModel.loadRequestedRentals()
    }
}
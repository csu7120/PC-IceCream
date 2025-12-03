package com.example.campuslink_android.ui.rental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink_android.R
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.RentalApi
import com.example.campuslink_android.data.repository.RentalRepositoryImpl

class LentListFragment : Fragment() {

    private lateinit var viewModel: RentalListViewModel
    private lateinit var adapter: RentalRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_lent_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), "LentListFragment", Toast.LENGTH_SHORT).show()

        // ✅ ViewModel / Repository 세팅
        val rentalApi = ApiClient.create(RentalApi::class.java)
        val rentalRepository = RentalRepositoryImpl(rentalApi, TokenStore)
        val factory = RentalListViewModelFactory(rentalRepository)
        viewModel = ViewModelProvider(this, factory)[RentalListViewModel::class.java]

        // ✅ RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvLentRentals).apply {
            layoutManager = LinearLayoutManager(requireContext())
        }

        // ✅ "내가 빌려준 목록"은 수락 버튼 필요 없음
        adapter = RentalRequestAdapter(
            onAcceptClick = { /* 사용 안 함 */ },
            showAcceptButton = false
        )

        recyclerView.adapter = adapter

        // ✅ Repo에서 이미 REQUESTED 제외된 리스트가 오기 때문에 그대로 사용
        viewModel.list.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        // ✅ 내가 빌려준 목록 가져오기
        viewModel.loadLentRentals()
    }
}

package com.example.campuslink_android.ui.pickup

import android.os.Bundle
import android.view.View
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

class PickupListFragment : Fragment(R.layout.fragment_pickup_main) {

    private lateinit var viewModel: PickupViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // ⭐ Repo + API 생성
        val rentalApi = ApiClient.create(RentalApi::class.java)
        val rentalRepo = RentalRepositoryImpl(rentalApi, TokenStore)
        val factory = PickupViewModelFactory(rentalRepo)
        viewModel = ViewModelProvider(this, factory)[PickupViewModel::class.java]

        // ⭐ RecyclerView 설정
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = PickupAdapter { rental ->
            viewModel.pickup(rental.rentalId)
        }
        recycler.adapter = adapter

        // ⭐ 리스트 Observe
        viewModel.items.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // ⭐ 결과 Observe
        viewModel.result.observe(viewLifecycleOwner) { ok ->
            if (ok) Toast.makeText(context, "픽업 완료!", Toast.LENGTH_SHORT).show()
            else Toast.makeText(context, "실패했습니다.", Toast.LENGTH_SHORT).show()
        }

        // ⭐ 처음 로딩할 때 ACCEPTED 목록 불러옴
        viewModel.loadAcceptedItems()
    }
}

package com.example.campuslink_android.ui.rental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink_android.R
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.RentalApi
import com.example.campuslink_android.data.repository.RentalRepositoryImpl

class BorrowedListFragment : Fragment() {

    private lateinit var viewModel: BorrowedListViewModel
    private lateinit var adapter: BorrowedListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_borrowed_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rentalApi = ApiClient.create(RentalApi::class.java)
        val rentalRepository = RentalRepositoryImpl(rentalApi, TokenStore)

        val factory = BorrowedListViewModelFactory(rentalRepository)
        viewModel = ViewModelProvider(this, factory)[BorrowedListViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvBorrowedRentals)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = BorrowedListAdapter { rentalId ->
            findNavController().navigate(
                R.id.action_borrowedListFragment_to_rentalFragment,
                bundleOf("rentalId" to rentalId)
            )
        }
        recyclerView.adapter = adapter

        // 관찰
        viewModel.list.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // ★ 여기가 핵심 — BorrowedListViewModel 의 loadMyRentals() 호출
        viewModel.loadMyRentals()
    }
}

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

    private lateinit var viewModel: RentalListViewModel
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
        val factory = RentalListViewModelFactory(rentalRepository)
        viewModel = ViewModelProvider(this, factory)[RentalListViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvBorrowedRentals)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = BorrowedListAdapter { rentalId ->
            findNavController().navigate(
                R.id.action_borrowedListFragment_to_rentalFragment,
                bundleOf("rentalId" to rentalId)
            )
        }
        recyclerView.adapter = adapter

        viewModel.list.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.loadMyRentals()
    }
}

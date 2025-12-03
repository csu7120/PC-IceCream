package com.example.campuslink_android.ui.rental

import android.content.Intent
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
import com.example.campuslink_android.ui.item.ItemDetailActivity

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

        adapter = BorrowedListAdapter { rental ->
            val intent = Intent(requireContext(), ItemDetailActivity::class.java).apply {

                putExtra("extra_item_id", rental.itemId)
                putExtra("extra_title", rental.itemTitle ?: "")
                putExtra("extra_category", "")
                putExtra("extra_owner_id", rental.lenderId ?: -1)
                putExtra("extra_owner_name", rental.ownerName ?: "")
                putExtra(
                    "extra_price",
                    (rental.itemOriginalPrice ?: 0).toDouble()
                )
                putExtra("extra_status", rental.status)
                putExtra("extra_description", rental.itemTitle ?: "상세 설명이 없습니다.")
                putExtra("extra_image_url", rental.itemImageUrl)
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        viewModel.list.observe(viewLifecycleOwner) { rentals ->
            adapter.submitList(rentals)
        }

        viewModel.loadMyRentals()
    }
}

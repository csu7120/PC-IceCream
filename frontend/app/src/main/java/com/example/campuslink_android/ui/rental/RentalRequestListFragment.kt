package com.example.campuslink_android.ui.rental

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink_android.R
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.data.dao.RentalApi
import com.example.campuslink_android.data.repository.RentalRepositoryImpl

class RentalRequestListFragment : Fragment() {

    private lateinit var viewModel: RentalListViewModel
    private lateinit var adapter: RentalRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_rental_request_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val rentalApi = ApiClient.create(RentalApi::class.java)
        val rentalRepository = RentalRepositoryImpl(rentalApi)
        val factory = RentalListViewModelFactory(rentalRepository)

        viewModel = ViewModelProvider(this, factory)[RentalListViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvRentalRequests)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = RentalRequestAdapter { rentalId ->

            findNavController().navigate(
                R.id.action_rentalRequestListFragment_to_rentalFragment,
                bundleOf("rentalId" to rentalId)
            )
        }

        recyclerView.adapter = adapter

        viewModel.list.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        viewModel.loadRequestedRentals()
    }
}

package com.example.campuslink_android.ui.returnlist

import android.os.Bundle
import android.util.Log
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
import com.example.campuslink_android.ui.review.ReviewDialog
import com.example.campuslink_android.ui.review.ReviewViewModel
import com.example.campuslink_android.ui.review.ReviewViewModelFactory
import com.example.campuslink_android.data.dao.ReviewApi
import com.example.campuslink_android.data.repository.ReviewRepositoryImpl

class ReturnListFragment : Fragment(R.layout.fragment_return_list) {

    private lateinit var viewModel: ReturnViewModel
    private lateinit var reviewViewModel: ReviewViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Rental Repo
        val rentalApi = ApiClient.create(RentalApi::class.java)
        val rentalRepo = RentalRepositoryImpl(rentalApi, TokenStore)
        val factory = ReturnViewModelFactory(rentalRepo)
        viewModel = ViewModelProvider(this, factory)[ReturnViewModel::class.java]

        // Review Repo
        val reviewApi = ApiClient.create(ReviewApi::class.java)
        val reviewRepo = ReviewRepositoryImpl(reviewApi, TokenStore)
        val reviewFactory = ReviewViewModelFactory(reviewRepo)
        reviewViewModel = ViewModelProvider(this, reviewFactory)[ReviewViewModel::class.java]

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ReturnAdapter { rental ->
            viewModel.returnItem(rental)
        }
        recycler.adapter = adapter

        viewModel.items.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.result.observe(viewLifecycleOwner) { ok ->
            if (ok) {
                Toast.makeText(context, "반납 완료!", Toast.LENGTH_SHORT).show()


                val rental = viewModel.lastReturnedRental
                if (rental != null) {
                    Log.d("RETURN", rental.toString())
                    Log.d("RENTAL_CHECK", "borrower=${rental.renterId}, lender=${rental.lenderId}")

                    val roleType =
                        if (TokenStore.getUserId() == rental.lenderId) "LENDER"
                        else "BORROWER"

                    ReviewDialog(
                        requireContext(),
                        rental,
                        roleType,
                        reviewViewModel
                    ).show()
                }

            } else {
                Toast.makeText(context, "반납 실패", Toast.LENGTH_SHORT).show()

            }
        }

        viewModel.loadReturnableItems()
    }
}

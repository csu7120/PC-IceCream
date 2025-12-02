package com.example.campuslink_android.ui.rental

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campuslink_android.databinding.ActivityRentalRequestsBinding
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.repository.RentalRepositoryImpl
import com.example.campuslink_android.data.dao.RentalApi

class RentalRequestListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRentalRequestsBinding
    private lateinit var viewModel: RentalViewModel

    private val rentalApi: RentalApi by lazy {
        ApiClient.create(RentalApi::class.java)
    }

    private val tokenStore = TokenStore

    // 🔥 Adapter 정의 (2개의 인자 rentalId + lenderEmail)
    private val adapter = RentalRequestAdapter { rentalId ->
        viewModel.acceptRental(rentalId)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRentalRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = RentalRepositoryImpl(rentalApi, tokenStore)

        viewModel = ViewModelProvider(
            this,
            RentalViewModelFactory(repository)
        )[RentalViewModel::class.java]

        setupUI()
        setupObservers()

        // 대여 요청 목록 불러오기
        viewModel.loadRequestedRentals()
    }

    private fun setupUI() {
        binding.recyclerRentalRequests.layoutManager = LinearLayoutManager(this)
        binding.recyclerRentalRequests.adapter = adapter
    }

    private fun setupObservers() {

        viewModel.requestedRentals.observe(this) { list ->
            adapter.submitList(list)
        }

        viewModel.acceptResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "대여 요청을 수락했습니다!", Toast.LENGTH_SHORT).show()

                // 목록 갱신
                viewModel.loadRequestedRentals()
            }.onFailure { e ->
                Toast.makeText(
                    this,
                    "수락 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

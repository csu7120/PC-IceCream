package com.example.campuslink_android.ui.rental

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import android.widget.Toast
import kotlinx.coroutines.launch
import com.example.campuslink_android.ui.rental.RentalListViewModel   // 혹시 자동 인식 안되면

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
import android.util.Log
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

        val rentalApi = ApiClient.create(RentalApi::class.java)
        val rentalRepository = RentalRepositoryImpl(rentalApi, TokenStore)
        val factory = RentalListViewModelFactory(rentalRepository)
        viewModel = ViewModelProvider(this, factory)[RentalListViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvLentRentals).apply {
            layoutManager = LinearLayoutManager(requireContext())
        }

        // ⭐ 수정된 부분: 2개 인자(rentalId, lenderEmail) 전달
        adapter = RentalRequestAdapter { rentalId ->
            lifecycleScope.launch {
                try {
                    Log.e("DEBUG_ACCEPT", "수락 실행 rentalId=$rentalId")
                    viewModel.acceptRental(rentalId)              // ⭐ 실제 수락 처리
                    viewModel.loadRequestedRentals()              // ⭐ UI 리스트 즉시 갱신
                    Toast.makeText(requireContext(), "대여 수락 성공!", Toast.LENGTH_SHORT).show()

                } catch (e: Exception) {
                    Log.e("DEBUG_ACCEPT", "에러 발생: ${e.message}")
                    Toast.makeText(requireContext(), "대여 수락 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }


        recyclerView.adapter = adapter

        viewModel.list.observe(viewLifecycleOwner) { list ->
            val lentOnly = list.filter { it.status != "REQUESTED" }
            adapter.submitList(lentOnly)
        }

        viewModel.loadRequestedRentals()
    }
}

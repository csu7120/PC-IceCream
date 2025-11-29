package com.example.campuslink_android.ui.rental

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.campuslink_android.R
import com.example.campuslink_android.core.network.ApiClient
import com.example.campuslink_android.core.network.TokenStore
import com.example.campuslink_android.data.dao.RentalApi
import com.example.campuslink_android.data.repository.RentalRepositoryImpl
import kotlinx.coroutines.launch

class RentalFragment : Fragment() {

    private val args: RentalFragmentArgs by navArgs()
    private lateinit var rentalViewModel: RentalViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rental, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val rentalId = args.rentalId

        val rentalApi = ApiClient.create(RentalApi::class.java)
        val rentalRepository = RentalRepositoryImpl(rentalApi)
        val factory = RentalViewModelFactory(rentalRepository)

        rentalViewModel = ViewModelProvider(this, factory)[RentalViewModel::class.java]

        rentalViewModel.acceptResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "대여 수락 성공", Toast.LENGTH_SHORT).show()
            }.onFailure { e ->
                Toast.makeText(
                    requireContext(),
                    "대여 수락 실패: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val acceptBtn = view.findViewById<Button>(R.id.btnAcceptRental)

        acceptBtn.setOnClickListener {
            val lenderEmail = TokenStore.getEmail() ?: run {
                Toast.makeText(requireContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Log.d("DEBUG", "currentUserEmail = $lenderEmail")

            lifecycleScope.launch {
                rentalViewModel.acceptRental(rentalId)
            }
        }
    }
}

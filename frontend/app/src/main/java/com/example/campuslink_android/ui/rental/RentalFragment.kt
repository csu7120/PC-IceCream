package com.example.campuslink_android.ui.rental

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        // ❌ 기존 테스트용 화면 삭제했으므로 더는 fragment_rental.xml 사용 불가
        // 🔥 사용하려는 실제 프로필 화면 레이아웃으로 반드시 변경해야 함!
        return inflater.inflate(R.layout.fragment_profile, container, false)
        // ↑ 여기를 네가 실제로 사용 중인 레이아웃 이름으로 바꿔줘.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rentalId = args.rentalId
        Log.e("DEBUG_FRAGMENT", "Fragment received rentalId=$rentalId")

        if (rentalId == -1) {
            Toast.makeText(requireContext(), "잘못된 rentalId 입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val rentalApi = ApiClient.create(RentalApi::class.java)
        val rentalRepository = RentalRepositoryImpl(rentalApi, TokenStore)
        val factory = RentalViewModelFactory(rentalRepository)
        rentalViewModel = ViewModelProvider(this, factory)[RentalViewModel::class.java]

        // 🔥 수락 결과 Observe
        rentalViewModel.acceptResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), "대여 수락 성공", Toast.LENGTH_SHORT).show()
            }.onFailure { e ->
                Toast.makeText(requireContext(), "대여 수락 실패: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // 🔥 테스트 버튼(btnAcceptRental) 삭제됨 → 이 Fragment 안에서는 버튼 클릭 없음
        // 수락 버튼은 프로필 화면에만 존재.
    }
}

package com.example.campuslink_android.ui.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.campuslink_android.R

class ItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 그냥 테스트용 화면만 띄우게
        return inflater.inflate(R.layout.fragment_item, container, false)
    }
}

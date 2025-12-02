package com.example.campuslink_android.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campuslink_android.R
import com.example.campuslink_android.ui.item.ItemDetailActivity
import com.example.campuslink_android.ui.item.RegisterItemActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by lazy {
        HomeViewModel.create()
    }

    private lateinit var adapter: ItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvItems = view.findViewById<RecyclerView>(R.id.rvItems)
        val txtError = view.findViewById<TextView>(R.id.txtHomeError)

        adapter = ItemAdapter(onItemClick = { item ->
            ItemDetailActivity.start(requireContext(), item)
        })

        rvItems.layoutManager = LinearLayoutManager(requireContext())
        rvItems.adapter = adapter

        // ⭐ "+ 등록하기" 버튼 클릭 이벤트
        val fab = view.findViewById<FloatingActionButton>(R.id.btnGoRegister)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), RegisterItemActivity::class.java)
            startActivity(intent)
        }

        // 데이터 로드
        viewModel.loadItems()

        viewModel.items.observe(viewLifecycleOwner, Observer { list ->
            adapter.submitList(list)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { msg ->
            txtError.text = msg ?: ""
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadItems()
    }
}

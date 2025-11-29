package com.example.campuslink_android.ui.item

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
import com.example.campuslink_android.domain.model.Item

class MyItemListFragment : Fragment() {

    private val viewModel: MyItemListViewModel by lazy {
        MyItemListViewModel.create()
    }

    private lateinit var adapter: MyItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_my_item_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerMyItems)
        val txtError = view.findViewById<TextView>(R.id.txtMyItemsError)
        val txtEmpty = view.findViewById<TextView>(R.id.txtMyItemsEmpty)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = MyItemAdapter { item ->
            // TODO: 물품 상세로 이동하고 싶으면 여기서 처리
        }
        recycler.adapter = adapter

        viewModel.items.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            txtEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            txtError.text = msg ?: ""
        }

        viewModel.loadMyItems()
    }
}


package com.example.imageserch.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.imageserch.BuildConfig
import com.example.imageserch.databinding.FragmentHomeBinding
import com.example.imageserch.ui.adapter.HomeAdapter
import com.example.imageserch.viewmodel.HomeViewModel
import com.example.imageserch.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by lazy { ViewModelProvider(this, ViewModelFactory())[HomeViewModel::class.java] }
    private val homeAdapter: HomeAdapter by lazy { HomeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initSearchView()
        initRecyclerView()
        dataObserve()
        return binding.root
    }

    private fun initSearchView() {
        binding.homeSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val key = "KakaoAK ${BuildConfig.kakao_key}"
                homeViewModel.getImage(key, query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initRecyclerView() {
        binding.homeRev.adapter = homeAdapter
    }

    private fun dataObserve() {
        homeViewModel.homeData.observe(viewLifecycleOwner) {
            homeAdapter.submitList(it.images)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.imageserch.ui

import android.graphics.fonts.Font
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imageserch.BuildConfig
import com.example.imageserch.data.Image
import com.example.imageserch.databinding.FragmentHomeBinding
import com.example.imageserch.ui.adapter.HomeAdapter
import com.example.imageserch.viewmodel.HomeViewModel
import com.example.imageserch.viewmodel.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by lazy { ViewModelProvider(this, ViewModelFactory())[HomeViewModel::class.java] }
    private val homeAdapter: HomeAdapter by lazy { HomeAdapter() }
    private val key :String by lazy { "KakaoAK ${BuildConfig.kakao_key}" }
    private lateinit var searchQuery: String
    private var page: Int = 1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchView()
        initFloatingButton()
        initRecyclerView()
        dataObserve()
    }

    private fun initSearchView() {
        binding.homeSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchQuery = query ?: ""
                page = 1
                homeViewModel.getImage(key, searchQuery, page)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initFloatingButton() {
        with(binding) {
            homeFloatingButton.setOnClickListener {
                homeRev.smoothScrollToPosition(0)
            }
        }
    }

    private fun initRecyclerView() {
        with(binding.homeRev) {
            adapter = homeAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                var isTop = true
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    binding.homeFloatingButton.onScrollStateChange(
                        newState == RecyclerView.SCROLL_STATE_IDLE, isTop)
                }
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    isTop = !this@with.canScrollVertically(-1)
                    checkLastItem(recyclerView)
                }
            })
        }
    }

    private fun dataObserve() {
        with(homeViewModel) {
            homeData.observe(viewLifecycleOwner) {
                val updateList = homeAdapter.currentList + it.images
                homeAdapter.submitList(updateList)
            }
        }
    }

    private fun checkLastItem(rev: RecyclerView) {
        val layoutManager = rev.layoutManager as GridLayoutManager
        val lastItemPosition = layoutManager.findLastVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount

        if (lastItemPosition == totalItemCount - 1) {
            // TODO: 여기서 다음페이지 요청 보내기
            homeViewModel.getImage(key, searchQuery ?: "", ++page)
        }
    }

    private fun FloatingActionButton.buttonAnimate(isVisible: Boolean) {
        var isAnimating = false

        if (isVisible && !isAnimating && this.visibility != View.VISIBLE) {
            this.animate().alpha(1f).setDuration(500).start()
            isAnimating = true
            this.visibility = View.VISIBLE
        } else if (!isVisible && !isAnimating && this.visibility == View.VISIBLE) {
            this.animate().alpha(0f).setDuration(500).withEndAction {
                isAnimating = false
                this.visibility = View.GONE
            }.start()
            isAnimating = true
        }
    }

    private fun FloatingActionButton.onScrollStateChange(isIdle: Boolean, isTop: Boolean) {
        if (isIdle) {
            if (!isTop) this@onScrollStateChange.buttonAnimate(true)
            return
        }
        this@onScrollStateChange.buttonAnimate(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
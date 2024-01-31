package com.example.imageserch.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.imageserch.BuildConfig
import com.example.imageserch.MyApp
import com.example.imageserch.R
import com.example.imageserch.SharedViewModel
import com.example.imageserch.data.SearchItem
import com.example.imageserch.databinding.FragmentHomeBinding
import com.example.imageserch.extention.fadeAnimation
import com.example.imageserch.ui.adapter.HomeAdapter
import com.example.imageserch.viewmodel.HomeViewModel
import com.example.imageserch.viewmodel.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels { ViewModelFactory() }
    private val sharedViewModel: SharedViewModel by viewModels { ViewModelFactory() }
    private val homeAdapter: HomeAdapter by lazy { HomeAdapter() }
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireContext()) }
    private val key: String by lazy { "KakaoAK ${BuildConfig.kakao_key}" }
    private lateinit var searchQuery: String
    private var isLoading = false


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
        initViews()
        dataObserve()
    }

    private fun initViews() {
        initSearchView()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        val likeList = MyApp.pref.loadLikeItems()
        homeAdapter.currentList.forEach {
            if (!likeList.contains(it)) it.like = false
        }
        homeAdapter.notifyDataSetChanged()
//        homeAdapter.submitList(homeAdapter.currentList.toList())
    }

    private fun initSearchView() {
        val previousQuery = MyApp.pref.getString("FirstQuery", " ")

        with(binding.homeSearch) {
            setQuery(previousQuery, false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchQuery = query ?: ""
                    MyApp.pref.setString("FirstQuery", searchQuery)
                    homeViewModel.loadFirstPage()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun initFloatingButton(isIdle: Boolean, isTop: Boolean) {
        with(binding.homeFloatingButton) {
            if (!isIdle || !isTop) {
                fadeAnimation(false)
                return
            }
            fadeAnimation(true)
            setOnClickListener {
                binding.homeRev.smoothScrollToPosition(0)
            }
        }
    }

    private fun addScroll(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val isTop = recyclerView.canScrollVertically(-1)
                val isStop = newState == RecyclerView.SCROLL_STATE_IDLE
                initFloatingButton(isStop, isTop)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 스크롤을 아래로 땡기면 dy<0 스크롤을 위로 땡기면 dy>0
                checkLastItem(recyclerView)
            }
        })
    }

    private fun initRecyclerView() {
        with(binding.homeRev) {
            adapter = homeAdapter
            addScroll(this)
        }
        initLikeButton()
    }
    private fun initLikeButton () {
        object : HomeAdapter.OnItemClickListener {
            override fun onLikeClick(pos: Int, data: SearchItem, iv: ImageView) {
                iv.setLikeImage(!data.like)
                data.like = !data.like
                if (data.like) {
                    sharedViewModel.addLikeItem(data)
                } else {
                    sharedViewModel.removeLikeItem(data)
                }
            }
        }.also { homeAdapter.listener = it }
    }

    private fun dataObserve() {
        with(homeViewModel) {
            searchList.observe(viewLifecycleOwner) { searchList ->
                homeAdapter.submitList(searchList.toList())
            }

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                this@HomeFragment.isLoading = isLoading
            }

            page.observe(viewLifecycleOwner) { page ->
                val query = MyApp.pref.getString("FirstQuery", " ")
                homeViewModel.getHomeData(key, query, page)
            }

        }
    }

    private fun checkLastItem(rev: RecyclerView) {
        val layoutManager = rev.layoutManager as GridLayoutManager
        val lastItemPosition = layoutManager.findLastVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount

        if (!isLoading && lastItemPosition == totalItemCount - 1) {
            isLoading = true
            homeViewModel.loadNextPage()
//            showLoading()
//            viewLifecycleOwner.lifecycleScope.launch {

//                delay(2000)
//                dismissLoading()
//            }
        }
    }

    fun ImageView.setLikeImage(state: Boolean) {
        if (state) {
            this.setImageResource(R.drawable.like_fill)
        } else {
            this.setImageResource(R.drawable.like)
        }
    }

    fun showLoading() {
        loadingDialog.show()
    }

    fun dismissLoading() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
            binding.homeSearch.clearFocus()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
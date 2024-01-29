package com.example.imageserch.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imageserch.BuildConfig
import com.example.imageserch.MyApp
import com.example.imageserch.R
import com.example.imageserch.data.SearchItem
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
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireContext()) }
    private val key :String by lazy { "KakaoAK ${BuildConfig.kakao_key}" }
    private lateinit var searchQuery: String
    private var page: Int = 1
    private var updateList = mutableListOf<SearchItem>()


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
        val previousQuery = MyApp.pref.getString("FirstQuery", " ")
        with(binding.homeSearch) {
            setQuery(previousQuery, false)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchQuery = query ?: ""
                    page = 1
                    updateList.clear()
                    homeViewModel.getHomeData(key, searchQuery, page)
                    MyApp.pref.setString("FirstQuery", searchQuery)
                    homeViewModel.loadLikeItems()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
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
            homeAdapter.listener = object : HomeAdapter.OnItemClickListener {
                override fun onLikeClick(pos: Int, data: SearchItem, iv:ImageView) {
                    iv.setLikeImage(!data.like)
                    data.like = !data.like
                    if (data.like) {
                        homeViewModel.addLikeItem(data)
                    } else {
                        homeViewModel.removeLikeItem(data)
                    }
                }

            }
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
            searchList.observe(viewLifecycleOwner) {searchList ->
                updateList = (homeAdapter.currentList + searchList).toMutableList()
                homeAdapter.submitList(updateList)
            }
            likeList.observe(viewLifecycleOwner) { likeList ->
                Log.d("likeList:","likeList::$likeList")
            }
        }
    }

    private fun checkLastItem(rev: RecyclerView) {
        val layoutManager = rev.layoutManager as GridLayoutManager
        val lastItemPosition = layoutManager.findLastVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount

        if (lastItemPosition == totalItemCount - 1) {
            showLoading()
            viewLifecycleOwner.lifecycleScope.launch {
                delay(2000) // Loading창을 보여주기 해서 원래 로딩 시간이 길어진다면 뷰모델에서 데이터를 받아올때 까지 기다려야하는건가? 변수를 만들어서?
                homeViewModel.getHomeData(key, searchQuery ?: "", ++page)
                dismissLoading()
            }
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
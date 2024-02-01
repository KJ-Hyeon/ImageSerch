package com.example.imageserch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.imageserch.data.SearchItem
import com.example.imageserch.databinding.FragmentStorageBinding
import com.example.imageserch.ui.adapter.HomeAdapter
import com.example.imageserch.viewmodel.SharedViewModel
import com.example.imageserch.viewmodel.ViewModelFactory

class StorageFragment : Fragment() {

    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!
    private val homeAdapter: HomeAdapter by lazy { HomeAdapter() }
    private val sharedViewModel: SharedViewModel by activityViewModels { ViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStorageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.getLikeList()
        sharedViewModel.setBadgeInvisible()
        initRecyclerView()
        dataObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        with(binding.revStorage) {
            adapter = homeAdapter
            homeAdapter.listener = object : HomeAdapter.OnItemClickListener {
                override fun onLikeClick(pos: Int, data: SearchItem, iv: ImageView) {
                    data.like = !data.like
                    sharedViewModel.removeLikeItem(data)
                    sharedViewModel.getLikeList()
                }

            }
        }
    }

    private fun dataObserve() {
        with(sharedViewModel) {
            likeList.observe(viewLifecycleOwner) {
                homeAdapter.submitList(it)
            }
        }
    }
}
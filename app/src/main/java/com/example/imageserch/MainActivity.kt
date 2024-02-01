package com.example.imageserch

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.imageserch.databinding.ActivityMainBinding
import com.example.imageserch.viewmodel.SharedViewModel
import com.example.imageserch.viewmodel.ViewModelFactory
import com.google.android.material.badge.BadgeDrawable

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val storageBadge: BadgeDrawable by lazy { binding.navView.getOrCreateBadge(R.id.navigation_storage) }
    private val sharedViewModel: SharedViewModel by viewModels { ViewModelFactory() }
    private var badgeVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHost.findNavController()
        binding.navView.setupWithNavController(navController)


        storageBadge.apply {
            isVisible = MyApp.pref.getBadgeVisible()
            backgroundColor = ContextCompat.getColor(this@MainActivity, R.color.bottom_badge)
            text = "NEW"
            horizontalPadding = resources.getDimensionPixelOffset(R.dimen.badge_padding)
            verticalPadding = resources.getDimensionPixelOffset(R.dimen.badge_padding)
            horizontalOffset = resources.getDimensionPixelOffset(R.dimen.badge_horizontal_offset)
        }

        sharedViewModel.isBadgeVisible.observe(this) { visible ->
            storageBadge.isVisible = visible
            badgeVisible = visible
        }
    }


    override fun onStop() {
        super.onStop()
        Log.d("onStop:","onStop")
        MyApp.pref.setBadgeVisible(badgeVisible)
    }
}
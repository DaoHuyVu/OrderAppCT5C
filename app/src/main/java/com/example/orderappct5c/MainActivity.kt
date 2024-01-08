package com.example.orderappct5c

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.orderappct5c.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),OnLoginListener,OnLogoutListener{
    private lateinit var binding : ActivityMainBinding
    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var toolBar : Toolbar
    private val viewModel by viewModels<ActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_activity_container_view) as NavHostFragment
        navController = navHostFragment.navController

        viewModel.activityUiState.observe(this){
            bottomNavView.visibility = if(it.isShowBottomNavView) View.VISIBLE else View.GONE
            toolBar.visibility = if(it.isShowToolbar) View.VISIBLE else View.GONE
            navController.graph = navController.navInflater.inflate(it.navigation)
        }

        setContentView(binding.root)
        bottomNavView = binding.bottomNavView
        toolBar = binding.toolBar
        setSupportActionBar(toolBar)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.categoryFragment,R.id.cartFragment,R.id.personalFragment)
        )
        toolBar.setupWithNavController(navController,appBarConfiguration)
        bottomNavView.setupWithNavController(navController)
    }

    override fun toHomeScreen() {
        viewModel.toHomeScreen()

    }

    override fun toAuthScreen() {
        viewModel.toAuthScreen()

    }
}

interface OnLoginListener {
    fun toHomeScreen()
}
interface OnLogoutListener {
    fun toAuthScreen()
}
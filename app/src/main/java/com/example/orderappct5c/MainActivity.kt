package com.example.orderappct5c

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.orderappct5c.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),OnLoginListener,OnLogoutListener{
    private lateinit var binding : ActivityMainBinding
    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var toolBar : Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_activity_container_view) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavView = binding.bottomNavView
        toolBar = binding.toolBar
        setSupportActionBar(toolBar)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.categoryFragment,R.id.cartFragment,R.id.personalFragment)
        )
        toolBar.setupWithNavController(navController,appBarConfiguration)
        bottomNavView.setupWithNavController(navController)
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navController.graph = navGraph
        setContentView(binding.root)
    }
    override fun login() {
        bottomNavView.visibility = View.VISIBLE
        toolBar.visibility = View.VISIBLE
    }

    override fun logout() {
        bottomNavView.visibility = View.GONE
        toolBar.visibility = View.GONE
    }
}
interface OnLoginListener {
    fun login()
}
interface OnLogoutListener {
    fun logout()
}
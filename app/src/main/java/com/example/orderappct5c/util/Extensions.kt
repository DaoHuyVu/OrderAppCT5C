package com.example.orderappct5c.util

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.orderappct5c.R
import com.example.orderappct5c.ui.EntryFragment
import com.example.orderappct5c.ui.LogoutFragment

fun Fragment.showToast(message : String){
    Toast.makeText(requireActivity(),message,Toast.LENGTH_SHORT).show()
}
fun EntryFragment.replaceNavGraph(){
    val navController = findNavController()
    val navGraph = navController.navInflater.inflate(R.navigation.home_nav_graph)
    navController.graph = navGraph
}
fun LogoutFragment.changeToAuthGraph(){
    val navController = findNavController()
    val navGraph = navController.navInflater.inflate(R.navigation.auth_nav_graph)
    navController.graph = navGraph
}
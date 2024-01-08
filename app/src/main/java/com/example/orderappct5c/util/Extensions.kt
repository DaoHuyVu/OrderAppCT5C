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

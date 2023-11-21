package com.example.orderappct5c.util

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(message : String){
    Toast.makeText(requireActivity(),message,Toast.LENGTH_LONG).show()
}
package com.example.orderappct5c.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.orderappct5c.OnLogoutListener
import com.example.orderappct5c.R

open class LogoutFragment : Fragment() {
    private lateinit var onLogoutListener: OnLogoutListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            onLogoutListener = context as OnLogoutListener
        }catch(ex : ClassCastException){
            Log.e("Test","Must implement the interface first")
        }
    }
    protected fun logout(){
        onLogoutListener.logout()
    }
}
package com.example.orderappct5c.ui

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.orderappct5c.OnLogoutListener

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
    protected fun toAuthScreen(){
        onLogoutListener.toAuthScreen()
    }
}
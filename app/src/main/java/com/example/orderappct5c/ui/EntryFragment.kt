package com.example.orderappct5c.ui

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.orderappct5c.OnLoginListener


open class EntryFragment : Fragment(){
    private lateinit var onLoginListener: OnLoginListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            onLoginListener =  context as OnLoginListener
        }catch (ex : ClassCastException){
            Log.e("Exception","Must implement the interface first")
        }
    }
    protected fun toHomeScreen(){
        onLoginListener.toHomeScreen()
    }
}
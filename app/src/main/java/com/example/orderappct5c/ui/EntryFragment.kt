package com.example.orderappct5c.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    protected fun login(){
        onLoginListener.login()
    }
}
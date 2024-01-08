package com.example.orderappct5c

import android.graphics.BitmapFactory
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.orderappct5c.ui.EntryFragment
import com.example.orderappct5c.util.DataStoreUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject
@AndroidEntryPoint
class SplashFragment : EntryFragment(){
    @Inject lateinit var dataStoreUtil: DataStoreUtil
    private lateinit var job : Job
    private var token : String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            token = dataStoreUtil.getToken()

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
    override fun onStart() {
        super.onStart()
        job = lifecycleScope.launch {
            delay(1000L)
            if(token != null){
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeNavGraph())
                toHomeScreen()
            }
            else {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToAuthNavGraph())
            }
        }
    }


    override fun onStop() {
        super.onStop()
        job.cancel()
    }
}


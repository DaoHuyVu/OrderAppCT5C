package com.example.orderappct5c.ui.home.personal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.orderappct5c.R
import com.example.orderappct5c.databinding.FragmentPersonalBinding
import com.example.orderappct5c.ui.LogoutFragment
import com.example.orderappct5c.util.DataStoreUtil
import com.example.orderappct5c.util.changeToAuthGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class PersonalFragment : LogoutFragment() {
    @Inject lateinit var dataStoreUtil : DataStoreUtil
    private var _binding : FragmentPersonalBinding? = null
    private val binding : FragmentPersonalBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentPersonalBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logoutButton.setOnClickListener{
            dataStoreUtil.deleteUserInfo()
            logout()
            findNavController().navigate(PersonalFragmentDirections.actionPersonalFragmentToAuthNavGraph())
            changeToAuthGraph()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
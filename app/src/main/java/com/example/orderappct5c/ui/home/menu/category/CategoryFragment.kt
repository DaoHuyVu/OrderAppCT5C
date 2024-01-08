package com.example.orderappct5c.ui.home.menu.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.orderappct5c.Message
import com.example.orderappct5c.R
import com.example.orderappct5c.databinding.FragmentCategoryBinding
import com.example.orderappct5c.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var _binding : FragmentCategoryBinding? = null
    private val binding : FragmentCategoryBinding get() = _binding!!
    private val viewModel : CategoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryAdapter = CategoryAdapter{ category ->
            val action = CategoryFragmentDirections.actionCategoryFragmentToMenuItemsFragment(category)
            findNavController().navigate(action)
        }
        binding.apply {
            recyclerView.adapter = categoryAdapter
            recyclerView.setHasFixedSize(true)
        }
        viewModel.categoryUiState.observe(viewLifecycleOwner){
            binding.progressBar.visibility = if(it.isLoading) View.VISIBLE else View.GONE
            it.errorMessage?.let{ message->
                when(message){
                    Message.SERVER_BREAKDOWN -> showToast(getString(R.string.server_breakdown))
                    Message.NO_INTERNET_CONNECTION -> showToast(getString(R.string.no_internet_connection))
                    Message.LOAD_ERROR -> showToast(getString(R.string.load_error))
                    else -> throw IllegalStateException()
                }
                viewModel.errorMessageShown()
            }
            if(!it.isLoading){
                categoryAdapter.submitList(it.categories)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
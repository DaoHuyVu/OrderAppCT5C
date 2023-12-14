package com.example.orderappct5c.ui.home.cart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
import com.example.orderappct5c.R
import com.example.orderappct5c.databinding.FragmentCartBinding
import com.example.orderappct5c.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private var _binding : FragmentCartBinding? = null
    private val binding : FragmentCartBinding get() = _binding!!
    private val viewModel by viewModels<CartViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cartAdapter = CartAdapter (
            {id,quantity -> viewModel.modifyQuantity(id,quantity)},
        ) {
            shownDialog {
                viewModel.removeItem(it)
            }
        }
        binding.apply {
            cartList.adapter = cartAdapter
            startOrderingButton.setOnClickListener {
                findNavController().navigate(CartFragmentDirections.actionCartFragmentToOrderFragment())
            }
        }
        viewModel.cartUiState.observe(viewLifecycleOwner){
            if(it.isLoading){
                binding.progressBar.visibility = View.VISIBLE
            }
            else{
                binding.apply {
                    if(it.message != null){
                        showToast(it.message)
                        viewModel.errorMessageShown()
                    }
                    else{
                        progressBar.visibility = View.GONE
                        cartAdapter.submitList(it.cartList)
                    }
                }
            }
        }
        viewModel.totalPrice.observe(viewLifecycleOwner){
            binding.totalPrice.text = getString(R.string.total,it)
        }
    }
    private fun shownDialog(callback : () -> Unit){
        AlertDialog
            .Builder(requireActivity())
            .setTitle(R.string.dialog_title)
            .setMessage(R.string.dialog_message)
            .setNegativeButton(R.string.dialog_negative){ _,_ ->
               callback.invoke()
            }
            .setPositiveButton(R.string.dialog_positive){ dialog,_ ->
                dialog.dismiss()
            }
            .create().show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
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
import com.example.orderappct5c.Message
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
        viewModel.cartUiState.observe(viewLifecycleOwner){
            binding.apply {
                progressBar.visibility = if(it.isLoading) View.VISIBLE else View.GONE
                it.message?.let{message ->
                    when(message){
                        Message.SERVER_BREAKDOWN -> showToast(getString(R.string.server_breakdown))
                        Message.NO_INTERNET_CONNECTION -> showToast(getString(R.string.no_internet_connection))
                        Message.MODIFIED_FAIL -> showToast(getString(R.string.modify_fail))
                        Message.REMOVED_FAIL -> showToast(getString(R.string.removed_fail))
                        Message.LOAD_ERROR -> showToast(getString(R.string.load_error))
                        else -> throw IllegalStateException()
                    }
                    viewModel.messageShown()
                }
                if(!it.isLoading){
                    it.cartList.let { list ->
                        noOrderItem.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                        startOrderingButton.isEnabled = list.isNotEmpty()
                        cartAdapter.submitList(list)
                    }
                }
            }
        }
        binding.apply {
            cartList.adapter = cartAdapter
            startOrderingButton.setOnClickListener {
                findNavController().navigate(CartFragmentDirections.actionCartFragmentToOrderFragment())
            }
        }
        viewModel.totalPrice.observe(viewLifecycleOwner){
            binding.totalPrice.text = getString(R.string.total,it)
        }
        viewModel.getList()
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
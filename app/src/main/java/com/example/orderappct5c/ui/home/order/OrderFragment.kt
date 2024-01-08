package com.example.orderappct5c.ui.home.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.example.orderappct5c.Message
import com.example.orderappct5c.R
import com.example.orderappct5c.databinding.FragmentOrderBinding
import com.example.orderappct5c.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment : Fragment(){
    private var _binding : FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private val viewModel : OrderViewModel by hiltNavGraphViewModels(R.id.home_nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            binding.apply{
                usernameEditText.setText(viewModel.userName)
                usernameEditText.doOnTextChanged{userName,_,_,_ -> viewModel.userNameChange(userName.toString())}
                phoneEditText.setText(viewModel.phoneNumber)
                phoneEditText.doOnTextChanged{phoneNumber,_,_,_ -> viewModel.phoneNumberChange(phoneNumber.toString())}
                addressEditText.setText(viewModel.address)
                addressEditText.doOnTextChanged{address,_,_,_ -> viewModel.addressChange(address.toString())}
                location.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.branchChange(position)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
                orderButton.setOnClickListener {
                    viewModel.makeOrder()
                }
        }
        viewModel.store.observe(viewLifecycleOwner){
            it?.let{
                val adapter = ArrayAdapter(
                    requireActivity(),
                    android.R.layout.simple_spinner_item,
                    it)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.location.adapter = adapter
                binding.location.setSelection(viewModel.selectedStorePosition)
            }
        }
        viewModel.orderUiState.observe(viewLifecycleOwner){
            binding.apply {
                progressBar.visibility = if(it.isLoading) View.VISIBLE else View.GONE
                progressBarContainer.visibility = if(it.isLoading) View.VISIBLE else View.GONE
                it.message?.let{message ->
                    when(message){
                        Message.SERVER_BREAKDOWN -> showToast(getString(R.string.server_breakdown))
                        Message.NO_INTERNET_CONNECTION -> showToast(getString(R.string.no_internet_connection))
                        Message.LOAD_ERROR -> showToast(getString(R.string.load_error))
                        Message.ORDER_SUCCESSFULLY -> showToast(getString(R.string.order_successfully))
                        Message.ORDER_FAIL -> showToast(getString(R.string.order_fail))
                        else -> throw IllegalStateException()
                    }
                    viewModel.messageShown()
                }
                usernameInputInvalid.visibility = if(it.isUserNameValid) View.INVISIBLE else View.VISIBLE
                phoneInputInvalid.visibility = if(it.isPhoneNumberValid) View.INVISIBLE else View.VISIBLE
                addressInputInvalid.visibility = if(it.isAddressValid) View.INVISIBLE else View.VISIBLE
                orderButton.isEnabled = it.isAddressValid && it.isUserNameValid && it.isPhoneNumberValid
                if(it.isOrderSuccessFully){
                    findNavController().popBackStack(R.id.cartFragment,true)
                    viewModel.orderedSuccessfully()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
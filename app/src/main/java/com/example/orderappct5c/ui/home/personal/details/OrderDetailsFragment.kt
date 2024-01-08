package com.example.orderappct5c.ui.home.personal.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.orderappct5c.Message
import com.example.orderappct5c.R
import com.example.orderappct5c.databinding.FragmentOrderDetailsBinding
import com.example.orderappct5c.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class OrderDetailsFragment: Fragment() {
    private val args by navArgs<OrderDetailsFragmentArgs>()
    private var _binding : FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var factory : OrderDetailsViewModel.OrderDetailsFactory
    private val viewModel by viewModels<OrderDetailsViewModel>(
        factoryProducer = { OrderDetailsViewModel.provideFactory(args.id,factory) }
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = OrderDetailsAdapter()
        binding.apply {
            button.setOnClickListener{
                viewModel.cancelOrder()
            }
            recyclerView.adapter = adapter
        }
        viewModel.orderDetails.observe(viewLifecycleOwner){
            binding.progressBar.visibility = if(it.isLoading) View.VISIBLE else View.GONE
            it.message?.let{message ->
               when(message){
                   Message.SERVER_BREAKDOWN -> showToast(getString(R.string.server_breakdown))
                   Message.NO_INTERNET_CONNECTION -> showToast(getString(R.string.no_internet_connection))
                   Message.CANCELLED_FAIL -> showToast(getString(R.string.cancel_fail))
                   else -> throw IllegalStateException()
               }
                viewModel.messageShown()
            }
            if(!it.isLoading){
                it.orderDetails?.apply {
                    binding.orderDetails.apply {
                        orderId.text = getString(R.string.order_id,id)
                        orderAddress.text = getString(R.string.order_destination,address)
                        orderLocation.text = getString(R.string.order_location,location)
                        orderPrice.text = getString(R.string.total,price)
                        orderCreatedAt.text = getString(R.string.order_created_at,createdAt)
                        orderStatus.text = getString(R.string.order_status,status)
                        if(status != "CREATED")
                            binding.button.isEnabled = false
                    }
                    adapter.submitList(orderItemDtoList)
                }
            }
            if(it.isSuccessful){
                showToast(getString(R.string.cancelled))
                findNavController().popBackStack()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
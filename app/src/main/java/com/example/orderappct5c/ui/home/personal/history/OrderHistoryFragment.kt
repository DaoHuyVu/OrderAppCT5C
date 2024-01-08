package com.example.orderappct5c.ui.home.personal.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.orderappct5c.Message
import com.example.orderappct5c.R
import com.example.orderappct5c.databinding.FragmentOrderHistoryBinding
import com.example.orderappct5c.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderHistoryFragment : Fragment() {
    private var _binding : FragmentOrderHistoryBinding? = null
    private val binding : FragmentOrderHistoryBinding get() = _binding!!
    private val viewModel : OrderHistoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderHistoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = OrderDetailAdapter{
            findNavController().navigate(
                OrderHistoryFragmentDirections.actionOrderHistoryFragmentToOrderDetailsFragment(it))
            }
        binding.apply {
            recyclerView.adapter = adapter
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshOrderHistory()
            }
        }
        viewModel.orderHistoryUiState.observe(viewLifecycleOwner){
            binding.apply {
                progressBar.visibility = if(it.isLoading) View.VISIBLE else View.GONE
                it.message?.let{message ->
                    when(message){
                        Message.SERVER_BREAKDOWN -> showToast(getString(R.string.server_breakdown))
                        Message.NO_INTERNET_CONNECTION -> showToast(getString(R.string.no_internet_connection))
                        Message.LOAD_ERROR -> showToast(getString(R.string.load_error))
                        else -> throw IllegalStateException()
                    }
                    viewModel.messageShown()
                }
                if(!it.isLoading){
                    noOrder.visibility = if(it.historyOrderList.isEmpty()) View.VISIBLE else View.GONE
                    adapter.submitList(it.historyOrderList)
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}
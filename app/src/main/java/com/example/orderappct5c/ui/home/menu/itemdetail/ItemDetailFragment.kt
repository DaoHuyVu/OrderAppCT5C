package com.example.orderappct5c.ui.home.menu.itemdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.orderappct5c.Message
import com.example.orderappct5c.R
import com.example.orderappct5c.databinding.FragmentItemDetailBinding
import com.example.orderappct5c.util.API_PREFIX
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class ItemDetailFragment : Fragment() {
    private var _binding : FragmentItemDetailBinding? = null
    private val binding get() = _binding!!
    private val itemIdArgs by navArgs<ItemDetailFragmentArgs>()
    @Inject lateinit var factory: ItemDetailViewModel.ItemDetailViewModelFactory
    private val viewModel by viewModels<ItemDetailViewModel>(
        factoryProducer = {
            ItemDetailViewModel.provideItemDetailFactory(factory,itemIdArgs.itemId)
        }
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            addQuantity.setOnClickListener{
                viewModel.addItem()
            }
            removeQuantity.setOnClickListener{
                viewModel.removeItem()
            }
            addToCart.setOnClickListener {
                viewModel.addToCart()
            }
        }
        viewModel.item.observe(viewLifecycleOwner){
            it?.let{
                binding.apply{
                    Glide
                        .with(root)
                        .load(API_PREFIX + it.imageUrl)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(android.R.drawable.stat_notify_error)
                        .into(itemDetailImage)
                    itemName.text = it.name
                    itemDetailDescription.text = it.description
                    totalPrice.text = getString(R.string.total,it.price)
                }
            }
        }
        viewModel.itemDetailUiState.observe(viewLifecycleOwner){
            it?.let {
                if(it.isLoading){
                    binding.progressBar.visibility = View.VISIBLE
                }
                else{
                    if(it.message != null){
                        when(it.message){
                            Message.ADD_SUCCESSFULLY -> Toast.makeText(requireActivity(),getString(R.string.added),Toast.LENGTH_SHORT).show()
                            Message.ADDED_FAIL -> Toast.makeText(requireActivity(),getString(R.string.add_failed),Toast.LENGTH_SHORT).show()
                            Message.SOMETHING_WRONG -> Toast.makeText(requireActivity(),getString(R.string.something_wrong),Toast.LENGTH_SHORT).show()
                            else -> {}
                        }
                        viewModel.errorMessageShown()
                    }
                    else{
                        binding.apply {
                            progressBar.visibility = View.GONE
                            quantity.text = it.quantity.toString()
                            totalPrice.text = getString(R.string.total,it.price)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
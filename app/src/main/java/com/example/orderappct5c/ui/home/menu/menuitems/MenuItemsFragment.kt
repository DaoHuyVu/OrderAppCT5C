package com.example.orderappct5c.ui.home.menu.menuitems

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.orderappct5c.databinding.FragmentMenuItemsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MenuItemsFragment : Fragment() {
    private var _binding : FragmentMenuItemsBinding? = null
    private val binding : FragmentMenuItemsBinding get() = _binding!!
    @Inject lateinit var factory : MenuItemViewModel.MenuItemViewModelFactory
    private val categoryArgs by navArgs<MenuItemsFragmentArgs>()
    private val viewModel by viewModels<MenuItemViewModel>(
        factoryProducer = {
            MenuItemViewModel.provideMenuItemViewModelFactory(factory,categoryArgs.category)
        }
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentMenuItemsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuItemAdapter = MenuItemAdapter{
            val action = MenuItemsFragmentDirections.actionMenuItemsFragmentToItemDetailFragment(it)
            findNavController().navigate(action)
        }
        binding.menuItems.apply{
            adapter = menuItemAdapter
            setHasFixedSize(true)
        }
        viewModel.menuItemsUiState.observe(viewLifecycleOwner){
            if(it.isLoading){
                binding.menuItemsProgressBar.visibility = View.VISIBLE
            }
            else{
                binding.menuItemsProgressBar.visibility = View.GONE
                menuItemAdapter.submitList(it.menuItems)
                if(it.errorMessage!=null){
                    Toast.makeText(requireActivity(),it.errorMessage,Toast.LENGTH_SHORT).show()
                    viewModel.errorMessageShown()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
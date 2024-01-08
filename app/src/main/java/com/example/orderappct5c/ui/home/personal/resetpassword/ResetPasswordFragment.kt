package com.example.orderappct5c.ui.home.personal.resetpassword

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.orderappct5c.Message
import com.example.orderappct5c.R
import com.example.orderappct5c.databinding.FragmentResetPasswordBinding
import com.example.orderappct5c.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {
    private var _binding : FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ResetPasswordViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResetPasswordBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            oldPassword.setText(viewModel.oldPassword)
            oldPassword.doOnTextChanged { text, _, _, _ -> viewModel.oldPasswordChange(text.toString())}
            newPassword.setText(viewModel.newPassword)
            newPassword.doOnTextChanged { text, _, _, _ -> viewModel.newPasswordChange(text.toString())}
            resetPassword.setOnClickListener {
                viewModel.changePassword()
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner){
            binding.progressBar.visibility = if(it.isLoading) View.VISIBLE else View.GONE
            it.message?.let {message ->
                when(message){
                    Message.SERVER_BREAKDOWN -> showToast(getString(R.string.server_breakdown))
                    Message.NO_INTERNET_CONNECTION -> showToast(getString(R.string.no_internet_connection))
                    Message.RESET_FAIL -> showToast(getString(R.string.reset_fail))
                    else -> throw IllegalStateException()
                }
                viewModel.messageShown()
            }
            binding.resetPassword.isEnabled = it.canReset
            if(it.isSuccessful){
                showToast(getString(R.string.reset))
                findNavController().popBackStack()
            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
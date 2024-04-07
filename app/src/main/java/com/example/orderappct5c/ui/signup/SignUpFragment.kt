package com.example.orderappct5c.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.orderappct5c.Message
import com.example.orderappct5c.R
import com.example.orderappct5c.databinding.FragmentSignupBinding
import com.example.orderappct5c.util.showToast
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding : FragmentSignupBinding? = null
    private val binding : FragmentSignupBinding get() = _binding!!
    private val signUpViewModel : SignUpViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply{
            usernameInputSignup.apply {
                setText(signUpViewModel.username)
                doOnTextChanged{ userName,_,_,_ ->
                    signUpViewModel.userNameChange(userName.toString())
                }
            }
            passwordInputSignup.apply{
                setText(signUpViewModel.password)
                doOnTextChanged{ password,_,_,_ ->
                    signUpViewModel.passwordChange(password.toString())
                }
            }
            emailInputSignup.apply{
                setText(signUpViewModel.email)
                doOnTextChanged{ email,_,_,_ ->
                    signUpViewModel.emailChange(email.toString())
                }
            }
            signupButton.apply {
                setOnClickListener{
                    signUpViewModel.signUp()
                }
            }
        }
        signUpViewModel.signUpUiState.observe(viewLifecycleOwner){
            it?.let{
                if(it.isLoading){
                    binding.apply{
                        signupButton.isEnabled = false
                        signupProgressbar.visibility = View.VISIBLE
                    }
                }
                else{
                    binding.apply{
                        signupButton.isEnabled = true
                        signupProgressbar.visibility = View.GONE
                    }
                    if(it.isSignUpSuccessfully){
                        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToAfterSignUpFragment())
                    }
                    else{
                        it.message?.let{ errorMessage ->
                            when(errorMessage){
                                Message.NO_INTERNET_CONNECTION -> showToast(getString(R.string.no_internet_connection))
                                Message.SIGN_UP_FAIL -> showToast(getString(R.string.sign_up_fail))
                                Message.SERVER_BREAKDOWN -> showToast(getString(R.string.server_breakdown))
                                else -> throw IllegalStateException()
                            }
                            signUpViewModel.errorMessageShown()
                        }
                    }
                }
            }
        }
        signUpViewModel.signUpUiFormState.observe(viewLifecycleOwner){
            it?.let{
                binding.apply {
                    invalidUsername.visibility = if(it.userNameInvalid) View.VISIBLE else View.INVISIBLE
                    invalidEmail.visibility = if(it.emailInvalid) View.VISIBLE else View.INVISIBLE
                    invalidPassword.visibility = if(it.passwordInvalid) View.VISIBLE else View.INVISIBLE
                }
                binding.signupButton.isEnabled = !it.passwordInvalid && !it.emailInvalid && !it.userNameInvalid
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
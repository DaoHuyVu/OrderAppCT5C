package com.example.orderappct5c.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.orderappct5c.Message
import com.example.orderappct5c.R
import com.example.orderappct5c.databinding.FragmentLoginBinding
import com.example.orderappct5c.ui.EntryFragment
import com.example.orderappct5c.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : EntryFragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            usernameInput.apply {
                setText(loginViewModel.userName)
                addTextChangedListener(
                    EditTextWatcher{
                        usernameInput -> loginViewModel.onUserNameChange(usernameInput)
                    }
                )
            }
            passwordInput.apply{
                setText(loginViewModel.password)
                addTextChangedListener(
                    EditTextWatcher{
                        passwordInput -> loginViewModel.onPasswordChange(passwordInput)
                    }
                )
            }
            loginButton.apply {
                setOnClickListener {
                    loginViewModel.login()
                }
            }
            signUpTextNavigation.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }
        }
        loginViewModel.loginUiState.observe(viewLifecycleOwner){
                if(it.isLoggingIn){
                    binding.apply {
                        loginProgressbar.visibility = View.VISIBLE
                        loginButton.isEnabled = false
                        signUpTextNavigation.isEnabled = false
                    }
                }
                else{
                    binding.apply{
                        loginProgressbar.visibility = View.INVISIBLE
                        loginButton.isEnabled = true
                        signUpTextNavigation.isEnabled = true
                    }
                    if(it.isUserLoggedIn){
                        toHomeScreen()
                    }
                    else{
                        it.message?.let{ msg ->
                            when(msg){
                                Message.USERNAME_EMPTY -> showToast(getString(R.string.username_empty))
                                Message.NO_INTERNET_CONNECTION -> showToast(getString(R.string.no_internet_connection))
                                Message.PASSWORD_EMPTY -> showToast(getString(R.string.password_empty))
                                Message.LOGIN_FAIL -> showToast(getString(R.string.login_fail))
                                Message.SERVER_BREAKDOWN -> showToast(getString(R.string.server_breakdown))
                                else -> throw IllegalStateException()
                            }
                            loginViewModel.errorMessageShown()
                        }
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
    private class EditTextWatcher(private val onChange : (String) -> Unit) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onChange.invoke(s!!.toString())
        }
        override fun afterTextChanged(s: Editable?) {}
    }
}

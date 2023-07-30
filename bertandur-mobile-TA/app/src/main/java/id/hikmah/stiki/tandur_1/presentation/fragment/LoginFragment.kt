package id.hikmah.stiki.tandur_1.presentation.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.FragmentLoginBinding
import id.hikmah.stiki.tandur_1.helper.Status
import id.hikmah.stiki.tandur_1.presentation.viewmodel.DatastoreViewModel
import id.hikmah.stiki.tandur_1.presentation.viewmodel.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()
    private val dataStore: DatastoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

        binding.buttonLogin.setOnClickListener {
            Toast.makeText(requireContext(), "Hello", Toast.LENGTH_SHORT).show()
        }

//        btnLoginClicked()
//        textWatcherEditText()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

//    private fun btnLoginClicked() {
//        binding.btnLogin.setOnClickListener {
//            val username = binding.editEmail.text.toString()
//            val password = binding.editPassword.text.toString()
//
//            if (validateEditText(username, password)) {
//                viewModel.loginAccount(username, password).observe(viewLifecycleOwner) { result ->
//                    when (result.status) {
//                        Status.LOADING -> {}
//                        Status.SUCCESS -> {
//
//                            dataStore.saveAccessToken(result.data!!.data.jwt)
//                            dataStore.saveLoginState(true)
//                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
//                        }
//                        Status.ERROR -> {
//                            Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//                }
//            }
//            Toast.makeText(
//                requireContext(),
//                "Welcome", Toast.LENGTH_SHORT
//            ).show()
//        }
//    }

    private fun textWatcherEditText() {
        binding.apply {

            editEmail.apply {
                doAfterTextChanged {
                    if(!isValidEmail(text.toString())) {
                        editEmail.error = "Wrong Email"
                    }
                }
            }

            editPassword.apply {
                if(isValidPassword(text.toString())){
                    editPassword.error = "Password must be in 6 or more characters"
                }
            }


        }
    }

    private fun validateEditText(email: String, password: String): Boolean {
        return if(!isValidEmail(email) && !isValidPassword(password)) {
            binding.apply {

                editEmail.error = "Wrong Email"
                editPassword.error = "Password must be in 6 or more characters"
            }
            false
        } else
            true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return !TextUtils.isEmpty(password) && password.length >= 6
    }


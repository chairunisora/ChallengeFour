package com.chairunissa.challengefour.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chairunissa.challengefour.R
import com.chairunissa.challengefour.database.local.AppLocalData
import com.chairunissa.challengefour.databinding.FragmentLoginBinding
import com.chairunissa.challengefour.datauser.Login
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvGoToRegisterScreen.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            val password = binding.etPassword.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()

            val getRegisteredUser = AppLocalData.getRegisteredUser(requireContext())

            if (email != getRegisteredUser?.email || password != getRegisteredUser.password){
                Snackbar.make(it, "Login failed", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val loggedInUser = Login(true, getRegisteredUser.username)
            AppLocalData.setUserLoggedIn(requireContext(), loggedInUser)

            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
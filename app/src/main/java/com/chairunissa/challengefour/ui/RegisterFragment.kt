package com.chairunissa.challengefour.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chairunissa.challengefour.R
import com.chairunissa.challengefour.database.local.AppLocalData
import com.chairunissa.challengefour.databinding.FragmentRegisterBinding
import com.chairunissa.challengefour.datauser.Register
import com.google.android.material.snackbar.Snackbar


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()

            if (password != confirmPassword) {
                Snackbar.make(it, "The confirm password doesn't match", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val registeredUser = Register(username,email,password)
            AppLocalData.setRegisteredUser(requireContext(), registeredUser)

            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

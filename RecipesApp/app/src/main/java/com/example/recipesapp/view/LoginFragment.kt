package com.example.recipesapp.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.model.utils.Snackbar
import com.example.recipesapp.view_model.FirebaseViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var firebaseViewModel: FirebaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        firebaseViewModel = ViewModelProvider(requireActivity()).get(FirebaseViewModel::class.java)

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide bottom navigation in login/register fragment
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation?.visibility = View.GONE

        // Click 'sign in' button
        signIn_button.setOnClickListener {
            signIn()
        }

        // Click 'sign up' button and move to 'register' fragment
        signUp_button.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun signIn() {
        val email = email.text?.toString()
        val password = password.text?.toString()

        // Validation of login and password
        // If input is right, then user is logged and move to 'home' fragment
        firebaseViewModel.login(email, password, requireActivity())
            .observe(viewLifecycleOwner, Observer {
                Snackbar(requireView(), it, getString(R.string.sign_in_successfufly), R.id.action_loginFragment_to_home)
            })
    }
}
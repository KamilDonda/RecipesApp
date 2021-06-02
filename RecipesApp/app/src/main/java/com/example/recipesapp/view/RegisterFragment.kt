package com.example.recipesapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.R
import com.example.recipesapp.model.utils.Snackbar
import com.example.recipesapp.view_model.FirebaseViewModel
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private lateinit var firebaseViewModel: FirebaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        firebaseViewModel = ViewModelProvider(requireActivity()).get(FirebaseViewModel::class.java)

        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Click 'signUp' button
        signUp_button.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        val email = email.text?.toString()
        val password = password.text?.toString()
        val repeatPassword = repeat_password.text?.toString()

        // Validation of login, password and repeated password
        // If input is right, then account is created and move to 'home' fragment
        firebaseViewModel.createAccount(email, password, repeatPassword, requireActivity())
            .observe(viewLifecycleOwner, Observer {
                Snackbar(requireView(), it, getString(R.string.sign_up_successfufly), R.id.action_registerFragment_to_home)
            })
    }
}
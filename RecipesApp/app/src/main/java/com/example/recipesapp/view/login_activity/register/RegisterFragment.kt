package com.example.recipesapp.view.login_activity.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.view.base.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseFragment() {

    private lateinit var registerViewModel: RegisterViewModel
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerViewModel = ViewModelProvider(requireActivity()).get(RegisterViewModel::class.java)
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSignUpClick()
    }

    private fun setupSignUpClick() {
        signUp_button.setOnClickListener {
            val email = email.text?.trim().toString()
            val password = password.text?.trim().toString()
            val repeatPassword = repeat_password.text?.trim().toString()

            when {
                email.isEmpty() -> showSnackbar(getString(R.string.email_is_empty))
                password.isEmpty() -> showSnackbar(getString(R.string.password_is_empty))
                password != repeatPassword -> showSnackbar(getString(R.string.different_passwords))
                else -> {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            if (it.user != null) {
                                val user = User(it.user!!.uid)
                                registerViewModel.createUser(user)
                                startApp()
                            }
                        }
                        .addOnFailureListener {
                            showSnackbar(it.message.toString())
                        }
                }
            }
        }
    }
}
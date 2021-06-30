package com.example.recipesapp.view.login_activity.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.view.login_activity.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    private val RC_SIGN_IN = 9001
    private lateinit var loginViewModel: LoginViewModel
    private val auth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginViewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSignInClick()
        setupSignInWithGoogleClick()
        setupSignUpClick()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val token = task.getResult(ApiException::class.java)!!.idToken
                val credential = GoogleAuthProvider.getCredential(token, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            if (it.result!!.user != null) {
                                // If user is not exist, then his account is created
                                val user = User(it.result!!.user!!.uid)
                                loginViewModel.createUserWithGoogle(user)
                                startApp()
                            }
                        } else {
                            showSnackbar(it.exception?.message.toString())
                        }
                    }
            } catch (e: ApiException) {
                showSnackbar(e.message.toString())
            }
        }
    }

    private fun setupSignInClick() {
        signIn_button.setOnClickListener {
            val email = email.text?.trim().toString()
            val password = password.text?.trim().toString()

            when {
                email.isEmpty() -> showSnackbar(getString(R.string.email_is_empty))
                password.isEmpty() -> showSnackbar(getString(R.string.password_is_empty))
                else -> {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            if (it.user != null) startApp()
                        }
                        .addOnFailureListener {
                            showSnackbar(it.message.toString())
                        }
                }
            }

        }
    }

    private fun setupSignInWithGoogleClick() {
        sign_in_button_with_google.setOnClickListener {
            googleSignInClient.signOut()
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(
                signInIntent,
                RC_SIGN_IN
            )
        }
    }

    private fun setupSignUpClick() {
        signUp_button.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment2_to_registerFragment2)
        }
    }
}
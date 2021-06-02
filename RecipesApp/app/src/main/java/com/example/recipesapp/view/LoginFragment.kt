package com.example.recipesapp.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.model.utils.Snackbar
import com.example.recipesapp.view_model.FirebaseViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private val RC_SIGN_IN = 9001

    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var googleSignInClient: GoogleSignInClient

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

        // Click 'sign in with Google' button
        sign_in_button_with_google.setOnClickListener {
            signInWithGoogle()
        }

        // Click 'sign up' button and move to 'register' fragment
        signUp_button.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onStart() {
        super.onStart()

        // If the user is remembered, he goes to the 'Home' fragment without logging in
        if (firebaseViewModel.auth.currentUser != null)
            findNavController().navigate(R.id.action_loginFragment_to_home)
    }

    private fun signIn() {
        val email = email.text?.toString()
        val password = password.text?.toString()

        // Validation of login and password
        // If input is right, then user is logged and move to 'home' fragment
        firebaseViewModel.login(email, password, requireActivity())
            .observe(viewLifecycleOwner, Observer {
                goToHome(it)
            })
    }

    private fun signInWithGoogle() {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(
            signInIntent,
            RC_SIGN_IN
        )
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
                val account = task.getResult(ApiException::class.java)!!
                firebaseViewModel.login(account.idToken!!)
                    .observe(viewLifecycleOwner, Observer {
                        goToHome(it)
                    })
            } catch (e: ApiException) {}
        }
    }

    private fun goToHome(message: String?) {
        Snackbar(
            requireView(),
            message,
            getString(R.string.sign_in_successfufly),
            R.id.action_loginFragment_to_home,
            com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
        )
    }

    //TODO Add logout button and remove this function
    override fun onStop() {
        super.onStop()
        firebaseViewModel.auth.signOut()
    }
}
package com.example.recipesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.view_model.FirebaseViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {

    private lateinit var firebaseViewModel: FirebaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Set bottom navigation as visible after logging in
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)!!
        bottomNavigation.visibility = View.VISIBLE

        firebaseViewModel = ViewModelProvider(requireActivity()).get(FirebaseViewModel::class.java)

        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Logout from the app
        logout_button.setOnClickListener {
            logout()
        }
    }

    // TODO move user to Login fragment after logout
    private fun logout() {
        firebaseViewModel.logout(requireActivity()).observe(viewLifecycleOwner, Observer {
            goToLogout(it)
        })
    }

    private fun goToLogout(message: String?) {
        com.example.recipesapp.utils.Snackbar(
            requireView(),
            message,
            getString(R.string.sign_in_successfufly),
            R.id.action_account_to_loginFragment,
            Snackbar.LENGTH_SHORT
        )

        findNavController().navigate(R.id.action_account_to_loginFragment)
    }
}
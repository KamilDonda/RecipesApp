package com.example.recipesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.R
import com.example.recipesapp.view_model.FirebaseViewModel
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {

    private lateinit var firebaseViewModel: FirebaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

    // TODO move to Login fragment after logout
    private fun logout() {
        firebaseViewModel.auth.signOut()
    }
}
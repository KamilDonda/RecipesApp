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
import com.example.recipesapp.view_model.RecipesViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_login.*

class AccountFragment : Fragment() {

    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Set bottom navigation as visible after logging in
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigation)!!
        bottomNavigation.visibility = View.VISIBLE

        firebaseViewModel = ViewModelProvider(requireActivity()).get(FirebaseViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)

        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailTextView.text = firebaseViewModel.auth.currentUser!!.email
        //nameTextView.text = firebaseViewModel.firestore.

        // Logout from the app
        logout.setOnClickListener {
            logout()
        }

        add_recipe.setOnClickListener {
            recipesViewModel.resetCurrentRecipe()
            findNavController().navigate(R.id.action_account_to_editRecipeFragment)
        }

        my_recipes.setOnClickListener {

        }

        edit_profile.setOnClickListener {

        }
    }

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
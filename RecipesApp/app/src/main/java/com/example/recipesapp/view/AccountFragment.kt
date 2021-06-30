package com.example.recipesapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.view_model.AddRecipeViewModel
import com.example.recipesapp.view_model.FirebaseViewModel
import com.example.recipesapp.view_model.RecipesViewModel
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {

    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var addRecipesViewModel: AddRecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        firebaseViewModel = ViewModelProvider(requireActivity()).get(FirebaseViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
        addRecipesViewModel =
            ViewModelProvider(requireActivity()).get(AddRecipeViewModel::class.java)

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
            addRecipesViewModel.clearData()
            findNavController().navigate(R.id.action_account_to_editRecipeFragment)
        }

        my_recipes.setOnClickListener {

        }

        edit_profile.setOnClickListener {

        }
    }

    private fun logout() {
        firebaseViewModel.logout(requireActivity()).observe(viewLifecycleOwner, Observer {
            requireActivity().finish()
        })
    }
}
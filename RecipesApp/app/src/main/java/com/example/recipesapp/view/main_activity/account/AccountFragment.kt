package com.example.recipesapp.view.main_activity.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.view.main_activity.edit_recipe.EditRecipeViewModel
import com.example.recipesapp.view_model.AddRecipeViewModel
import com.example.recipesapp.view_model.FirebaseViewModel
import com.example.recipesapp.view_model.RecipesViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {

    private lateinit var editRecipeViewModel: EditRecipeViewModel
    private lateinit var accountViewModel: AccountViewModel

    val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        editRecipeViewModel =
            ViewModelProvider(requireActivity()).get(EditRecipeViewModel::class.java)
        accountViewModel =
            ViewModelProvider(requireActivity()).get(AccountViewModel::class.java)

        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        setupAddRecipeClick()
        setupMyRecipesClick()
        setupEditProfileClick()
        setupLogoutClick()
        
    }

    private fun setupData() {
        User.currentUser.observe(viewLifecycleOwner) {
            my_recipes_textView.text = it.recipes.size.toString()
            favourites_textView.text = it.favourites.size.toString()
            emailTextView.text = auth.currentUser!!.email
        }
    }

    private fun setupAddRecipeClick() {
        add_recipe.setOnClickListener {
            Recipe.resetCurrentRecipe()
            editRecipeViewModel.resetRecipe()
            findNavController().navigate(R.id.action_account_to_editRecipeFragment)
        }
    }

    private fun setupMyRecipesClick() {
        my_recipes.setOnClickListener {

        }
    }

    private fun setupEditProfileClick() {
        edit_profile.setOnClickListener {

        }
    }

    private fun setupLogoutClick() {
        logout.setOnClickListener {
            auth.signOut()
            requireActivity().finish()
        }
    }
}
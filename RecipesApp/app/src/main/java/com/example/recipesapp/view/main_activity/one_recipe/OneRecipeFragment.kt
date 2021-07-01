package com.example.recipesapp.view.main_activity.one_recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.adapter.ListAdapter
import com.example.recipesapp.model.entity.Level
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.utils.Photo
import com.example.recipesapp.utils.RatingSystem
import com.example.recipesapp.utils.TimeConverter
import com.example.recipesapp.view.main_activity.edit_recipe.EditRecipeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_one_recipe.*

class OneRecipeFragment : Fragment() {

    private lateinit var oneRecipeViewModel: OneRecipeViewModel
    private lateinit var editRecipeViewModel: EditRecipeViewModel

    private val ingredientsListAdapter = ListAdapter()
    private val preparationListAdapter = ListAdapter()

    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        oneRecipeViewModel =
            ViewModelProvider(requireActivity()).get(OneRecipeViewModel::class.java)
        editRecipeViewModel =
            ViewModelProvider(requireActivity()).get(EditRecipeViewModel::class.java)

        return inflater.inflate(R.layout.fragment_one_recipe, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        setupPublicClick()
        setupEditClick()
        setupIngredientsClick()
        setupPreparationClick()
        setupIngredientsObserver()
        setupPreparationObserver()

        ingredients_recyclerView.adapter = ingredientsListAdapter
        preparation_recyclerView.adapter = preparationListAdapter
    }

    private fun setupData() {
        Recipe.currentRecipe.observe(viewLifecycleOwner) {
            name_textView.text = it.name
            author_textView.text = it.author
            level_textView.text =
                getString(Level.values().find { level -> level.number == it.level }!!.id)
            time_textView.text = TimeConverter().longToString(it.time)
            meals_textView.text = it.meals.toString()
            RatingSystem().displayStars(requireContext(), rating_linearLayout, it.rating)
            Photo().setPhoto(it.image, requireContext(), imageView_one_recipe)
            publicRecipe_button.isEnabled = !it.public

            ingredientsListAdapter.setList(it.ingredients)
            preparationListAdapter.setList(it.preparation)

            oneRecipeViewModel.getIdOfRecipes(auth.currentUser!!.uid)
                .observe(viewLifecycleOwner) { list ->
                    buttons_constraintLayout.visibility =
                        if (list.contains(it.id)) View.VISIBLE else View.GONE
                }
        }
    }

    private fun setupPublicClick() {
        publicRecipe_button.setOnClickListener {
            oneRecipeViewModel.setRecipeAsPublic(Recipe.currentRecipe.value!!)
            findNavController().popBackStack()
        }
    }

    private fun setupEditClick() {
        editRecipeViewModel.resetRecipe()
        editRecipe_button.setOnClickListener {
            findNavController().navigate(R.id.action_oneRecipeFragment_to_editRecipeFragment)
        }
    }

    private fun setupIngredientsClick() {
        ingredients_constraintLayout.setOnClickListener {
            oneRecipeViewModel.changeVisibilityOfIngredients()
        }
    }

    private fun setupPreparationClick() {
        preparation_constraintLayout.setOnClickListener {
            oneRecipeViewModel.changeVisibilityOfPreparation()
        }
    }

    private fun setupIngredientsObserver() {
        oneRecipeViewModel.visibleIngredients.observe(viewLifecycleOwner) {
            ingredients_recyclerView.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setupPreparationObserver() {
        oneRecipeViewModel.visiblePreparation.observe(viewLifecycleOwner) {
            preparation_recyclerView.visibility = if (it) View.VISIBLE else View.GONE
        }
    }
}
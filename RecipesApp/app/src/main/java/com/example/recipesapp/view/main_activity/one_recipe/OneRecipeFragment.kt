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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipesapp.R
import com.example.recipesapp.adapter.IngredientsAdapter
import com.example.recipesapp.adapter.ListAdapter
import com.example.recipesapp.model.entity.Level
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.utils.Photo
import com.example.recipesapp.utils.RatingSystem
import com.example.recipesapp.utils.TimeConverter
import com.example.recipesapp.view.main_activity.edit_recipe.EditRecipeViewModel
import kotlinx.android.synthetic.main.fragment_one_recipe.*


class OneRecipeFragment : Fragment() {

    private lateinit var oneRecipeViewModel: OneRecipeViewModel
    private lateinit var editRecipeViewModel: EditRecipeViewModel

    private lateinit var ingredientsListAdapter: IngredientsAdapter
    private val preparationListAdapter =
        ListAdapter { oneRecipeViewModel.changeVisibilityOfPreparation() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        oneRecipeViewModel =
            ViewModelProvider(requireActivity()).get(OneRecipeViewModel::class.java)
        editRecipeViewModel =
            ViewModelProvider(requireActivity()).get(EditRecipeViewModel::class.java)

        ingredientsListAdapter = IngredientsAdapter(requireContext(), oneRecipeViewModel)

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
        setupSelectAllClick()

        ingredients_recyclerView.adapter = ingredientsListAdapter
        preparation_recyclerView.adapter = preparationListAdapter

        DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
            preparation_recyclerView.addItemDecoration(this)
        }
    }

    private fun setupData() {
        Recipe.currentRecipe.observe(viewLifecycleOwner) {
            name_textView.text = it.name
            author_textView.text = it.author
            level_textView.text =
                getString(Level.values().find { level -> level.number == it.level }!!.id)
            time_textView.text = TimeConverter.longToString(it.time)
            meals_textView.text = it.meals.toString()
            RatingSystem.displayStars(requireContext(), rating_linearLayout, it.rating)
            Photo.setPhoto(it.image, requireContext(), imageView_one_recipe)
            publicRecipe_button.isEnabled = !it.public

            ingredientsListAdapter.setList(it.ingredients)
            preparationListAdapter.setList(it.preparation)

            User.currentUser.observe(viewLifecycleOwner) { user ->
                buttons_constraintLayout.visibility =
                    if (user.recipes.contains(it.id)) View.VISIBLE else View.GONE
            }
        }

        oneRecipeViewModel.isSelectedMode.observe(viewLifecycleOwner) {
            select_all_button.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }

        oneRecipeViewModel.allSelected.observe(viewLifecycleOwner) {
            select_all_button.text =
                if (it) getString(R.string.unselect_all) else getString(R.string.select_all)
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
            if (!oneRecipeViewModel.isSelectedMode.value!!)
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

    private fun setupSelectAllClick() {
        select_all_button.setOnClickListener {
            ingredientsListAdapter.selectAll()
        }
    }
}
package com.example.recipesapp.view.main_activity.one_recipe

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.adapter.IngredientsAdapter
import com.example.recipesapp.adapter.PreparationAdapter
import com.example.recipesapp.model.entity.Level
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.utils.Photo
import com.example.recipesapp.utils.RatingSystem
import com.example.recipesapp.utils.TimeConverter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_edit_recipe.*
import kotlinx.android.synthetic.main.fragment_one_recipe.*
import kotlinx.android.synthetic.main.fragment_one_recipe.buttons_constraintLayout
import kotlinx.android.synthetic.main.fragment_one_recipe.ingredients_constraintLayout
import kotlinx.android.synthetic.main.fragment_one_recipe.ingredients_recyclerView
import kotlinx.android.synthetic.main.fragment_one_recipe.level_imageView
import kotlinx.android.synthetic.main.fragment_one_recipe.level_textView
import kotlinx.android.synthetic.main.fragment_one_recipe.meals_textView
import kotlinx.android.synthetic.main.fragment_one_recipe.preparation_constraintLayout
import kotlinx.android.synthetic.main.fragment_one_recipe.preparation_recyclerView
import kotlinx.android.synthetic.main.fragment_one_recipe.time_textView


class OneRecipeFragment : Fragment() {

    private lateinit var oneRecipeViewModel: OneRecipeViewModel

    private lateinit var ingredientsListAdapter: IngredientsAdapter
    private val preparationListAdapter = PreparationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        oneRecipeViewModel =
            ViewModelProvider(requireActivity()).get(OneRecipeViewModel::class.java)

        ingredientsListAdapter = IngredientsAdapter(requireContext(), oneRecipeViewModel)

        return inflater.inflate(R.layout.fragment_one_recipe, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        setupPublicClick()
        setupEditClick()
        setupFavouritesClick()
        setupVisibilityClick()
        setupVisibilityObservers()
        setupSelectAllClick()
        setupFabClick()
        setupTooltipClick()
        setupCopyClick()

        ingredients_recyclerView.adapter = ingredientsListAdapter
        preparation_recyclerView.adapter = preparationListAdapter
    }

    private fun setupData() {
        Recipe.currentRecipe.observe(viewLifecycleOwner) {
            name_textView.text = it.name
            author_textView.text = it.author
            val lvl = Level.values().find { lvl -> lvl.number == it.level }!!
            level_textView.text = getString(lvl.text_id)
            level_imageView.setImageDrawable(
                AppCompatResources.getDrawable(requireContext(), lvl.icon_id)
            )
            time_textView.text = TimeConverter.longToString(it.time)
            meals_textView.text = it.meals.toString()
            RatingSystem.displayStars(requireContext(), rating_linearLayout, it.rating, 25f)
            rating_textView.text = it.rating.toString()
            Photo.setPhoto(it.image, requireContext(), imageView_one_recipe)
            publicRecipe_button.isEnabled = !it.public

            ingredientsListAdapter.setList(it.ingredients)
            preparationListAdapter.setList(it.preparation)

            User.currentUser.observe(viewLifecycleOwner) { user ->
                buttons_constraintLayout.visibility =
                    if (user.recipes.contains(it.id)) View.VISIBLE else View.GONE
                favourites_checkBox.isChecked = user.favourites.contains(it.id)
            }
        }

        oneRecipeViewModel.isSelectedMode.observe(viewLifecycleOwner) {
            select_all_button.visibility = if (it) View.VISIBLE else View.INVISIBLE
            addToBasket_fab.visibility = if (it) View.VISIBLE else View.GONE
            ingredientsInfo_imageView.visibility = if (it) View.GONE else View.VISIBLE
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
        editRecipe_button.setOnClickListener {
            val recipe = Recipe.currentRecipe.value!!.copy(
                ingredients = ArrayList(Recipe.currentRecipe.value!!.ingredients),
                preparation = ArrayList(Recipe.currentRecipe.value!!.preparation)
            )
            Recipe.setEditRecipe(recipe)
            findNavController().navigate(R.id.action_oneRecipeFragment_to_editRecipeFragment)
        }
    }

    private fun setupFavouritesClick() {
        favourites_checkBox.setOnClickListener {
            oneRecipeViewModel.changeFavouritesStatus(Recipe.currentRecipe.value!!)
        }
    }

    private fun setupVisibilityClick() {
        ingredients_constraintLayout.setOnClickListener {
            if (!oneRecipeViewModel.isSelectedMode.value!!)
                oneRecipeViewModel.changeVisibilityOfIngredients()
        }
        preparation_constraintLayout.setOnClickListener {
            oneRecipeViewModel.changeVisibilityOfPreparation()
        }
    }

    private fun setupVisibilityObservers() {
        oneRecipeViewModel.visibleIngredients.observe(viewLifecycleOwner) {
            ingredients_recyclerView.visibility = if (it) View.VISIBLE else View.GONE
        }
        oneRecipeViewModel.visiblePreparation.observe(viewLifecycleOwner) {
            preparation_recyclerView.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setupSelectAllClick() {
        select_all_button.setOnClickListener {
            ingredientsListAdapter.selectAll()
        }
    }

    private fun setupFabClick() {
        addToBasket_fab.setOnClickListener {
            // TODO
            Log.v("test", ingredientsListAdapter.getSelectedList().toString())
        }
    }

    private fun setupTooltipClick() {
        val views = listOf(
            ingredientsInfo_imageView, level_layout, time_layout, meals_layout,
            author_textView, rating_linearLayout
        )
        for (v in views)
            v.setOnClickListener { it.performLongClick() }
    }

    private fun setupCopyClick() {
        copyIngredients_button.setOnClickListener {
            context?.copyToClipboard(
                Recipe.currentRecipe.value!!.ingredients.joinToString(
                    prefix = "[",
                    postfix = "]"
                )
            )
        }
        copyPreparation_button.setOnClickListener {
            context?.copyToClipboard(Recipe.currentRecipe.value!!.preparation.mapIndexed { index, s ->
                "${index + 1}. $s"
            }.joinToString(separator = "\n"))
        }
    }

    private fun Context.copyToClipboard(text: CharSequence) {
        val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
        clipboard?.setPrimaryClip(ClipData.newPlainText("", text))
        Snackbar.make(requireView(), getString(R.string.copied), Snackbar.LENGTH_SHORT).show()
    }
}
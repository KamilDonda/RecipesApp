package com.example.recipesapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.adapter.ListAdapter
import com.example.recipesapp.model.entity.Level
import com.example.recipesapp.utils.RatingSystem
import com.example.recipesapp.utils.TimeConverter
import com.example.recipesapp.view_model.AddRecipeViewModel
import com.example.recipesapp.view_model.OneRecipeViewModel
import com.example.recipesapp.view_model.RecipesViewModel
import kotlinx.android.synthetic.main.fragment_one_recipe.*

class OneRecipeFragment : Fragment() {

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var addRecipesViewModel: AddRecipeViewModel
    private lateinit var oneRecipeViewModel: OneRecipeViewModel

    private lateinit var ingredientsListAdapter: ListAdapter
    private lateinit var ingredientsRecyclerView: RecyclerView

    private lateinit var preparationListAdapter: ListAdapter
    private lateinit var preparationRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
        addRecipesViewModel =
            ViewModelProvider(requireActivity()).get(AddRecipeViewModel::class.java)
        oneRecipeViewModel =
            ViewModelProvider(requireActivity()).get(OneRecipeViewModel::class.java)

        recipesViewModel.getIngredients().observe(viewLifecycleOwner, Observer {
            ingredientsListAdapter.notifyDataSetChanged()
        })

        recipesViewModel.getPreparation().observe(viewLifecycleOwner, Observer {
            preparationListAdapter.notifyDataSetChanged()
        })

        ingredientsListAdapter = ListAdapter(recipesViewModel.ingredients)
        preparationListAdapter = ListAdapter(recipesViewModel.preparation)

        return inflater.inflate(R.layout.fragment_one_recipe, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ingredientsRecyclerView =
            ingredients_recyclerView.apply { adapter = ingredientsListAdapter }
        preparationRecyclerView =
            preparation_recyclerView.apply { adapter = preparationListAdapter }

        recipesViewModel.currentRecipe.observe(viewLifecycleOwner, Observer {
            oneRecipeViewModel.setRecipe(it)
        })

        oneRecipeViewModel.recipe.observe(viewLifecycleOwner, Observer {
            name_textView.text = it.name
            author_textView.text = it.author
            level_textView.text =
                getString(Level.values().find { level -> level.number == it.level }!!.id)
            time_textView.text = TimeConverter().longToString(it.time)
            meals_textView.text = it.meals.toString()
            RatingSystem().displayStars(requireContext(), rating_linearLayout, it.rating)

            recipesViewModel.my_recipes.observe(viewLifecycleOwner, Observer { list ->
                buttons_constraintLayout.visibility =
                    if (list.contains(it)) View.VISIBLE else View.GONE
            })

            publicRecipe_button.isEnabled = !it.public
        })

        editRecipe_button.setOnClickListener {
            addRecipesViewModel.clearData()
            findNavController().navigate(R.id.action_oneRecipeFragment_to_editRecipeFragment)
        }

        ingredients_constraintLayout.setOnClickListener {
            oneRecipeViewModel.changeVisibilityOfIngredients()
        }
        oneRecipeViewModel.visibleIngredients.observe(viewLifecycleOwner, Observer {
            ingredients_recyclerView.visibility = if (it) View.VISIBLE else View.GONE
        })

        preparation_constraintLayout.setOnClickListener {
            oneRecipeViewModel.changeVisibilityOfPreparation()
        }
        oneRecipeViewModel.visiblePreparation.observe(viewLifecycleOwner, Observer {
            preparation_recyclerView.visibility = if (it) View.VISIBLE else View.GONE
        })
    }
}
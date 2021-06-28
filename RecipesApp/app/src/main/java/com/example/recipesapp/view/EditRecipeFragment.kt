package com.example.recipesapp.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.adapter.EditTextAdapter
import com.example.recipesapp.model.entity.Level
import com.example.recipesapp.utils.Photo
import com.example.recipesapp.utils.RecipeMenu
import com.example.recipesapp.utils.Snackbar
import com.example.recipesapp.utils.TimeConverter
import com.example.recipesapp.view_model.AddRecipeViewModel
import com.example.recipesapp.view_model.FirebaseViewModel
import com.example.recipesapp.view_model.RecipesViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.fragment_edit_recipe.*

class EditRecipeFragment : Fragment() {

    private lateinit var firebaseViewModel: FirebaseViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var addRecipesViewModel: AddRecipeViewModel

    private lateinit var levelMenu: RecipeMenu
    private lateinit var mealsMenu: RecipeMenu

    private lateinit var ingredientsListAdapter: EditTextAdapter
    private lateinit var ingredientsRecyclerView: RecyclerView

    private lateinit var preparationListAdapter: EditTextAdapter
    private lateinit var preparationRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        firebaseViewModel =
            ViewModelProvider(requireActivity()).get(FirebaseViewModel::class.java)
        recipesViewModel =
            ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
        addRecipesViewModel =
            ViewModelProvider(requireActivity()).get(AddRecipeViewModel::class.java)

        return inflater.inflate(R.layout.fragment_edit_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenus()

        name_textInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!name_textInput.text.isNullOrEmpty()) {
                    addRecipesViewModel.recipe.value?.copy(name = name_textInput.text.toString())
                        ?.let { addRecipesViewModel.setRecipe(it) }
                }
            }
        })

        save_button.setOnClickListener {
            addOrUpdateRecipe(it)
        }

        level_button.setOnClickListener {
            levelMenu.showMenu()
        }

        time_button.setOnClickListener {
            selectTime()
        }

        meals_button.setOnClickListener {
            mealsMenu.showMenu()
        }

        addIngredient_button.setOnClickListener {
            addIngredientRow()
        }
        ingredients_constraintLayout.setOnClickListener {
            addRecipesViewModel.changeVisibilityOfIngredients()
        }
        addRecipesViewModel.visibleIngredients.observe(viewLifecycleOwner, Observer {
            ingredients_recyclerView.visibility = if (it) View.VISIBLE else View.GONE
        })

        addPreparation_button.setOnClickListener {
            addPreparationRow()
        }
        preparation_constraintLayout.setOnClickListener {
            addRecipesViewModel.changeVisibilityOfPreparation()
        }
        addRecipesViewModel.visiblePreparation.observe(viewLifecycleOwner, Observer {
            preparation_recyclerView.visibility = if (it) View.VISIBLE else View.GONE
        })

        recipesViewModel.currentRecipe.observe(viewLifecycleOwner, Observer {
            if (it != null)
                addRecipesViewModel.fetchData(it)
        })

        ingredientsListAdapter =
            EditTextAdapter(addRecipesViewModel.ingredients,
                { p: Int, s: String ->
                    addRecipesViewModel.updateIngredients(p, s)
                }, {
                    deleteIngredient(it)
                })
        preparationListAdapter =
            EditTextAdapter(addRecipesViewModel.preparation,
                { p: Int, s: String ->
                    addRecipesViewModel.updatePreparation(p, s)
                }, {
                    deletePreparation(it)
                })

        ingredientsRecyclerView =
            ingredients_recyclerView.apply { adapter = ingredientsListAdapter }
        preparationRecyclerView =
            preparation_recyclerView.apply { adapter = preparationListAdapter }

        displayData()
    }

    private fun setupMenus() {
        levelMenu = RecipeMenu(
            requireContext(),
            level_button,
            Level.values().map { getString(it.id) },
            addRecipesViewModel,
            "level"
        )
        mealsMenu = RecipeMenu(
            requireContext(),
            meals_button,
            (1..10).toList(),
            addRecipesViewModel,
            "meals"
        )
    }

    private fun selectTime() {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(1)
                .setMinute(0)
                .setTitleText(R.string.select_time_desc)
                .build()

        picker.addOnPositiveButtonClickListener {
            val h = picker.hour
            val m = picker.minute
            val time = TimeConverter().hourAndMinuteToLong(h, m)

            addRecipesViewModel.recipe.value?.copy(time = time)?.let {
                addRecipesViewModel.setRecipe(it)
            }
        }

        picker.show(requireFragmentManager(), null)
    }

    private fun displayData() {
        addRecipesViewModel.recipe.observe(viewLifecycleOwner, Observer {
            if (name_textInput.text.toString() != it.name)
                name_textInput.setText(it.name)

            level_textView.text =
                getString((Level.values().find { level -> level.number == it.level })!!.id)

            time_textView.text = TimeConverter().longToString(it.time)

            meals_textView.text = it.meals.toString()

            Photo().setPhoto(it.image, requireContext(), imageView_edit_recipe)

            addRecipesViewModel.setIngredients(it.ingredients)
            addRecipesViewModel.setPreparation(it.preparation)
            ingredientsListAdapter.notifyDataSetChanged()
        })
    }

    private fun addOrUpdateRecipe(view: View) {
        if (addRecipesViewModel.recipe.value!!.name.length > 6) {
            when {
                addRecipesViewModel.recipe.value!!.ingredients.isEmpty() ->
                    Snackbar(view, getString(R.string.empty_ingredients_list))
                addRecipesViewModel.recipe.value!!.preparation.isEmpty() ->
                    Snackbar(view, getString(R.string.empty_preparation_list))
                addRecipesViewModel.recipe.value!!.ingredients.contains("") ->
                    Snackbar(view, getString(R.string.empty_ingredients))
                addRecipesViewModel.recipe.value!!.preparation.contains("") ->
                    Snackbar(view, getString(R.string.empty_preparation))
                else -> {
                    firebaseViewModel.addOrUpdateRecipe(addRecipesViewModel.recipe.value!!)
                    Snackbar(view, getString(R.string.saved_successfully))
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                }
            }
        } else Snackbar(view, getString(R.string.name_too_short))
    }

    private fun addIngredientRow() {
        addRecipesViewModel.setIngredients(addRecipesViewModel.ingredients.value!!.apply {
            if (this.isEmpty() || this.last().isNotEmpty())
                this.add("")
        })
        ingredientsListAdapter.notifyDataSetChanged()
    }

    private fun addPreparationRow() {
        addRecipesViewModel.setPreparation(addRecipesViewModel.preparation.value!!.apply {
            if (this.isEmpty() || this.last().isNotEmpty())
                this.add("")
        })
        preparationListAdapter.notifyDataSetChanged()
    }

    private fun deleteIngredient(position: Int) {
        addRecipesViewModel.setIngredients(addRecipesViewModel.ingredients.value!!.apply {
            this.removeAt(position)
        })
        ingredientsListAdapter.notifyDataSetChanged()
    }

    private fun deletePreparation(position: Int) {
        addRecipesViewModel.setPreparation(addRecipesViewModel.preparation.value!!.apply {
            this.removeAt(position)
        })
        preparationListAdapter.notifyDataSetChanged()
    }
}
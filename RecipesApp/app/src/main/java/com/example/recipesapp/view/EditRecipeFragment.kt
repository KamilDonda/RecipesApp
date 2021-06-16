package com.example.recipesapp.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Level
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

        recipesViewModel.currentRecipe.observe(viewLifecycleOwner, Observer {
            if (it != null && addRecipesViewModel.isDataDefault)
                addRecipesViewModel.fetchData(it)
        })

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
        })
    }

    private fun addOrUpdateRecipe(view: View) {
        if (addRecipesViewModel.recipe.value!!.name.length > 6) {
            firebaseViewModel.addOrUpdateRecipe(addRecipesViewModel.recipe.value!!)
            Snackbar(view, getString(R.string.saved_successfully))
            findNavController().popBackStack()
        } else Snackbar(view, getString(R.string.name_too_short))
    }
}
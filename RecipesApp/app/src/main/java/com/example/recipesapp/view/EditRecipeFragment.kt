package com.example.recipesapp.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Level
import com.example.recipesapp.utils.RecipeMenu
import com.example.recipesapp.utils.TimeConverter
import com.example.recipesapp.view_model.AddRecipeViewModel
import com.example.recipesapp.view_model.RecipesViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.fragment_edit_recipe.*

class EditRecipeFragment : Fragment() {

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var addRecipesViewModel: AddRecipeViewModel

    private lateinit var levelMenu: RecipeMenu
    private lateinit var mealsMenu: RecipeMenu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
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
                    addRecipesViewModel.setName(name_textInput.text.toString())
                }
            }
        })

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

            addRecipesViewModel.setTime(time)
        }

        picker.show(requireFragmentManager(), null)
    }

    private fun displayData() {
        addRecipesViewModel.name.observe(
            viewLifecycleOwner,
            Observer {
                if (name_textInput.text.toString() != it)
                    name_textInput.setText(it)
            })

        addRecipesViewModel.level.observe(
            viewLifecycleOwner,
            Observer {
                level_textView.text =
                    getString((Level.values().find { level -> level.number == it })!!.id)
            })

        addRecipesViewModel.time.observe(
            viewLifecycleOwner,
            Observer { time_textView.text = TimeConverter().longToString(it) })

        addRecipesViewModel.meals.observe(
            viewLifecycleOwner,
            Observer { meals_textView.text = it.toString() })
    }
}
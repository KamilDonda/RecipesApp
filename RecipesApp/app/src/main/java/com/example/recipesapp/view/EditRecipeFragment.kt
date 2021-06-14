package com.example.recipesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipesapp.R
import com.example.recipesapp.utils.RecipeMenu
import com.example.recipesapp.utils.TimeConverter
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.fragment_edit_recipe.*

class EditRecipeFragment : Fragment() {

    private lateinit var levelMenu: RecipeMenu
    private lateinit var mealsMenu: RecipeMenu

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_edit_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenus()

        level_button.setOnClickListener {
            levelMenu.showMenu()
        }

        time_button.setOnClickListener {
            selectTime()
        }

        meals_button.setOnClickListener {
            mealsMenu.showMenu()
        }
    }

    private fun setupMenus() {
        levelMenu = RecipeMenu(
            requireContext(),
            level_button,
            listOf("Easy", "Medium", "Hard"),
            level_textView
        )
        mealsMenu = RecipeMenu(requireContext(), meals_button, (1..10).toList(), meals_textView)
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

            time_textView.text = TimeConverter().longToString(time)
        }

        picker.show(requireFragmentManager(), null)
    }
}
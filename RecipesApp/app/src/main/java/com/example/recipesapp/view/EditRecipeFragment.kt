package com.example.recipesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.recipesapp.R
import com.example.recipesapp.utils.RecipeMenu
import com.google.android.material.button.MaterialButton
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
            levelMenu.click()
        }

        time_button.setOnClickListener {

        }

        meals_button.setOnClickListener {
            mealsMenu.click()
        }
    }

    private fun setupMenus() {
        levelMenu = RecipeMenu(requireContext(), level_button, listOf("Easy", "Medium", "Hard"))
        mealsMenu = RecipeMenu(requireContext(), meals_button, (1..10).toList())
    }
}
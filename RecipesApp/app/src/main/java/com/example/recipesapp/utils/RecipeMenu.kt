package com.example.recipesapp.utils

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Level
import com.example.recipesapp.model.entity.Recipe
import com.google.android.material.button.MaterialButton

class RecipeMenu(
    context: Context,
    button: MaterialButton,
    private val list: List<Any>,
    element: String
) {

    private var listPopupWindow: ListPopupWindow =
        ListPopupWindow(context, null, R.attr.listPopupWindowStyle)

    init {
        listPopupWindow.anchorView = button
        val adapter = ArrayAdapter(context, R.layout.item_list_popup_window, list)
        listPopupWindow.setAdapter(adapter)

        listPopupWindow.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            clickItem(position, element)
            listPopupWindow.dismiss()
        }
    }

    fun showMenu() {
        listPopupWindow.show()
    }

    private fun clickItem(position: Int, element: String) {
        when (element) {
            "level" -> setLevel(Level.values()[position].number)
            "meals" -> setMeals(list[position].toString().toInt())
        }
    }

    private fun setLevel(level: Int) {
        Recipe.editRecipe.value?.copy(level = level)?.let { Recipe.setEditRecipe(it) }
    }

    private fun setMeals(meals: Int) {
        Recipe.editRecipe.value?.copy(meals = meals)?.let { Recipe.setEditRecipe(it) }
    }
}
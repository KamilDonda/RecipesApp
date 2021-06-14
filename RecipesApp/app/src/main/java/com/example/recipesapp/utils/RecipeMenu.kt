package com.example.recipesapp.utils

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import com.example.recipesapp.R
import com.google.android.material.button.MaterialButton

class RecipeMenu(context: Context, button: MaterialButton, list: List<Any>) {

    private var listPopupWindow: ListPopupWindow =
        ListPopupWindow(context, null, R.attr.listPopupWindowStyle)

    init {
        listPopupWindow.anchorView = button
        val adapter = ArrayAdapter(context, R.layout.list_popup_window_item, list)
        listPopupWindow.setAdapter(adapter)

        listPopupWindow.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.

            // Dismiss popup.
            listPopupWindow.dismiss()
        }
    }

    fun click() {
        listPopupWindow.show()
    }
}
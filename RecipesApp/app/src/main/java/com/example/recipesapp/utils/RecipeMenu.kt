package com.example.recipesapp.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import com.example.recipesapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class RecipeMenu(
    context: Context,
    button: MaterialButton,
    list: List<Any>,
    private val textView: MaterialTextView
) {

    private var listPopupWindow: ListPopupWindow =
        ListPopupWindow(context, null, R.attr.listPopupWindowStyle)

    init {
        listPopupWindow.anchorView = button
        val adapter = ArrayAdapter(context, R.layout.list_popup_window_item, list)
        listPopupWindow.setAdapter(adapter)

        listPopupWindow.setOnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            clickItem(list[position])
            listPopupWindow.dismiss()
        }
    }

    fun showMenu() {
        listPopupWindow.show()
    }

    private fun clickItem(item: Any) {
        Log.v("TEST", item.toString())
        textView.text = item.toString()
    }
}
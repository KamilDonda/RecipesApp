package com.example.recipesapp.utils

import android.view.View
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class Snackbar(
    view: View,
    null_message: String?,
    message: String = "",
    id: Int = 0,
    length: Int = Snackbar.LENGTH_LONG
) {
    // If the first messege (null_message) is null, then second messege is shown and navigate to fragment
    init {
        Snackbar.make(view, null_message ?: message, length).show()
        if (null_message == null) {
            view.findNavController().navigate(id)
        }
    }
}
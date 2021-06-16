package com.example.recipesapp.utils

import android.view.View
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class Snackbar(view: View, null_message: String?, message: String, id: Int, length: Int = Snackbar.LENGTH_LONG) {
    // If the first messege is null, then second messege is shown and callback is added
    init {
        var mess = null_message
        if (mess == null)
            mess = message
        val snackbar = Snackbar.make(view, mess, length)
        if (null_message == null) {
            view.findNavController().navigate(id)
        }
        snackbar.show()
    }
}
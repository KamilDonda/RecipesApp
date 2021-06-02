package com.example.recipesapp.model.utils

import android.view.View
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class Snackbar(view: View, null_message: String?, message: String, id: Int) {
    // If the first messege is null, then second messege is shown and callback is added
    init {
        var mess = null_message
        if (mess == null)
            mess = message
        val snackbar = Snackbar.make(view, mess, Snackbar.LENGTH_LONG)
        if (null_message == null) {
            snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    view.findNavController().navigate(id)
                }
            })
        }
        snackbar.show()
    }
}
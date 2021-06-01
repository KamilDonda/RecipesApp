package com.example.recipesapp.model.utils

import android.app.Activity
import android.view.View
import androidx.navigation.findNavController
import com.example.recipesapp.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class Snackbar(activity: Activity, view: View, message: String?, id: Int) {
    init {
        var mess = message
        if (mess == null)
            mess = activity.getString(R.string.sign_up_successfufly)
        val snackbar = Snackbar.make(view, mess, Snackbar.LENGTH_LONG)
        if (message == null) {
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
package com.example.recipesapp.view.login_activity

import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.recipesapp.view.MainActivity
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {
    protected fun startApp() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    protected fun showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(requireView(), message, length).show()
    }
}
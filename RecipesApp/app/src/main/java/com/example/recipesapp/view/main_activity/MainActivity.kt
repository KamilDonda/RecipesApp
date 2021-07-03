package com.example.recipesapp.view.main_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.model.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val repository = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUser()
        setupNavigation()
    }

    // Setup current User
    private fun setupUser() {
        repository.getUser(auth.uid!!).observe(this) {
            if (it != null) User.setCurrentUser(it)
        }
    }

    // Setup navigation between fragments
    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        NavigationUI.setupWithNavController(bottomNavigation, navHostFragment!!.navController)
    }
}
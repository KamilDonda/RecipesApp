package com.example.recipesapp.view.main_activity.recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.recipesapp.model.repository.FirebaseRepository

class RecipesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FirebaseRepository()

    val publicRecipes = repository.getPublicRecipes()
}
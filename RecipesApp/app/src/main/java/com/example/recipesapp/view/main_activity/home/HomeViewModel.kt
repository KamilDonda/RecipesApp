package com.example.recipesapp.view.main_activity.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.recipesapp.model.repository.FirebaseRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FirebaseRepository()

    val mostPopular = repository.getMostPopular()
    fun myRecipes(list: ArrayList<String>) = repository.getUserRecipes(list)
}
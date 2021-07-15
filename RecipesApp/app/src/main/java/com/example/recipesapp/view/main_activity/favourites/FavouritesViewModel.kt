package com.example.recipesapp.view.main_activity.favourites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.recipesapp.model.repository.FirebaseRepository

class FavouritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FirebaseRepository()

    fun favourites(list: ArrayList<String>) = repository.getRecipesByIDs(list)
}
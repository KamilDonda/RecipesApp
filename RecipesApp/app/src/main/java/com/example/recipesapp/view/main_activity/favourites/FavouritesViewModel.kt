package com.example.recipesapp.view.main_activity.favourites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.recipesapp.model.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth

class FavouritesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FirebaseRepository()
    private val auth = FirebaseAuth.getInstance()

    fun favourites(list: ArrayList<String>) = repository.getRecipesByIDs(list)

    fun updateUserFavourites(list: ArrayList<String>) {
        repository.updateUserFavourites(auth.uid!!, list)
    }
}
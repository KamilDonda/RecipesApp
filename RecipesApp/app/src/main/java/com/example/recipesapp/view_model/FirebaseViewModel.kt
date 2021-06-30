package com.example.recipesapp.view_model

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.model.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository = FirebaseRepository()
    val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun logout(activity: Activity): LiveData<String?> {
        val message = MutableLiveData<String>()
        auth.signOut()
        if (auth.currentUser == null)
            message.value = activity.getString(R.string.logout_successfully)
        else
            message.value = activity.getString(R.string.logout_not_successfully)
        return message

    }

    var currentUser: MutableLiveData<User> = MutableLiveData()

    fun updateUser(user: User) {
        firebaseRepository.updateUser(user)
    }

    // Add recipe to firebase or update if it exists
    fun addOrUpdateRecipe(recipe: Recipe) {
        if (recipe.id.isEmpty()) {
            val recipe_id = UUID.randomUUID().toString()
            firebaseRepository.addOrUpdateRecipe(
                recipe.copy(id = recipe_id, author = currentUser.value!!.nickname)
            )
            currentUser.value!!.recipes.add(recipe_id)
            updateUser(currentUser.value!!.copy(recipes = currentUser.value!!.recipes))
        } else
            firebaseRepository.addOrUpdateRecipe(recipe.copy(public = false))
    }

    fun setRecipeAsPublic(recipe: Recipe) {
        firebaseRepository.addOrUpdateRecipe(recipe.copy(public = true))
    }

    fun uploadPhoto(id: String, bytes: ByteArray) {
        firebaseRepository.uploadPhoto(storage, id, bytes)
    }
}
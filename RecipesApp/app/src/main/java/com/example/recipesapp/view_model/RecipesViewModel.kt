package com.example.recipesapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot

class RecipesViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository = FirebaseRepository()

    private var _currentRecipe: MutableLiveData<Recipe> = MutableLiveData()
    val currentRecipe: LiveData<Recipe> get() = _currentRecipe
    fun setCurrentRecipe(recipe: Recipe) {
        _currentRecipe.value = recipe
    }

    var recipes: MutableLiveData<List<Recipe>> = MutableLiveData()
    // Get public recipes
    fun getPublicRecipes(): LiveData<List<Recipe>> {
        firebaseRepository.getPublicRecipes().addSnapshotListener(EventListener<QuerySnapshot>{ value, e ->
            if (e != null) {
                recipes.value = null
                return@EventListener
            }

            val recipeList : MutableList<Recipe> = mutableListOf()
            for (doc in value!!) {
                val recipe = doc.toObject(Recipe::class.java)
                recipeList.add(recipe)
            }
            recipes.value = recipeList
        })
        return recipes
    }

    var ingredients: MutableLiveData<ArrayList<String>> = MutableLiveData()
    // Get ingredients from current recipe
    fun getIngredients(): LiveData<ArrayList<String>> {
        ingredients.value = _currentRecipe.value?.ingredients
        return ingredients
    }

    var preparation: MutableLiveData<ArrayList<String>> = MutableLiveData()
    // Get preparation from current recipe
    fun getPreparation(): LiveData<ArrayList<String>> {
        preparation.value = _currentRecipe.value?.preparation
        return preparation
    }
}
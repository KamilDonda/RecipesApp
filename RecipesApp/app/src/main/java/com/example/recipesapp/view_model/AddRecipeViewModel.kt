package com.example.recipesapp.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.model.entity.Level
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.utils.TimeConverter

class AddRecipeViewModel(application: Application) : AndroidViewModel(application) {

    var isDataDefault = true

    private var _recipe = MutableLiveData<Recipe>()
    val recipe: LiveData<Recipe> get() = _recipe
    fun setRecipe(recipe: Recipe) {
        _recipe.value = recipe
    }

    fun fetchData(recipe: Recipe) {
        isDataDefault = false
        setRecipe(recipe)
    }

    fun clearData() {
        isDataDefault = true
        setRecipe(Recipe().apply {
            level = Level.EASY.number
            time = TimeConverter().hourAndMinuteToLong(0, 30)
            meals = 1
        })
    }
}
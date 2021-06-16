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

    private var _ingredients = MutableLiveData<ArrayList<String>>()
    val ingredients: LiveData<ArrayList<String>> get() = _ingredients
    fun setIngredients(ingredients: ArrayList<String>) {
        _ingredients.value = ingredients
    }

    private var _preparation = MutableLiveData<ArrayList<String>>()
    val preparation: LiveData<ArrayList<String>> get() = _preparation
    fun setPreparation(preparation: ArrayList<String>) {
        _preparation.value = preparation
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

    private var _visibleIngredients: MutableLiveData<Boolean> = MutableLiveData()
    val visibleIngredients: LiveData<Boolean> get() = _visibleIngredients
    fun changeVisibilityOfIngredients() {
        _visibleIngredients.value = !_visibleIngredients.value!!
    }

    private var _visiblePreparation: MutableLiveData<Boolean> = MutableLiveData()
    val visiblePreparation: LiveData<Boolean> get() = _visiblePreparation
    fun changeVisibilityOfPreparation() {
        _visiblePreparation.value = !_visiblePreparation.value!!
    }

    init {
        _visibleIngredients.value = true
        _visiblePreparation.value = true
    }
}
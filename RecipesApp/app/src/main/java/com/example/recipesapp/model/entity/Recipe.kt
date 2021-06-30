package com.example.recipesapp.model.entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.Serializable

data class Recipe(
    var id: String,
    var name: String,
    var author: String = "",
    var level: Int = 0,
    var time: Long = 0,
    var meals: Int = 0,
    var ingredients: ArrayList<String> = ArrayList(),
    var preparation: ArrayList<String> = ArrayList(),
    var public: Boolean = false,
    var ratingCount: Int = 0,
    var ratingSum: Int = 0,
    var rating: Float,
    var image: String = ""
) : Serializable {

    constructor() : this("", "", "", 0, 0, 0, ArrayList(), ArrayList(), false, 0, 0, 0f, "")

    companion object {
        private var _currentRecipe: MutableLiveData<Recipe> = MutableLiveData()
        val currentRecipe: LiveData<Recipe> get() = _currentRecipe
        fun setCurrentRecipe(recipe: Recipe) {
            _currentRecipe.value = recipe
        }
        fun resetCurrentRecipe() {
            _currentRecipe.value = Recipe()
        }
    }
}

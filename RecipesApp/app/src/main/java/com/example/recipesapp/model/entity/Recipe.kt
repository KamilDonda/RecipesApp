package com.example.recipesapp.model.entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.utils.TimeConverter
import java.io.Serializable

data class Recipe(
    var id: String,
    var name: String,
    var author: String = "",
    var level: Int = Level.EASY.number,
    var time: Long = TimeConverter.hourAndMinuteToLong(0, 30),
    var meals: Int = 1,
    var ingredients: ArrayList<String> = ArrayList(),
    var preparation: ArrayList<String> = ArrayList(),
    var public: Boolean = false,
    var ratingCount: Int = 0,
    var ratingSum: Int = 0,
    var rating: Float,
    var image: String = ""
) : Serializable {

    constructor() : this("", "", "", Level.EASY.number, TimeConverter.hourAndMinuteToLong(0, 30), 1, ArrayList(), ArrayList(), false, 0, 0, 0f, "")

    companion object {
        private var _currentRecipe: MutableLiveData<Recipe> = MutableLiveData()
        val currentRecipe: LiveData<Recipe> get() = _currentRecipe
        fun setCurrentRecipe(recipe: Recipe) {
            _currentRecipe.value = recipe
        }

        private var _editRecipe: MutableLiveData<Recipe> = MutableLiveData()
        val editRecipe: LiveData<Recipe> get() = _editRecipe
        fun setEditRecipe(recipe: Recipe) {
            _editRecipe.value = recipe
        }
        fun resetEditRecipe() {
            _editRecipe.value = Recipe()
        }
    }
}

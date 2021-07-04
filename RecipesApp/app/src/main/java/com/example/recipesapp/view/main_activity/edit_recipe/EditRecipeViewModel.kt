package com.example.recipesapp.view.main_activity.edit_recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.model.entity.Level
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.model.repository.FirebaseRepository
import com.example.recipesapp.utils.TimeConverter
import com.google.firebase.auth.FirebaseAuth

class EditRecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FirebaseRepository()
    val auth = FirebaseAuth.getInstance()

    private var _recipe = MutableLiveData<Recipe>(Recipe.currentRecipe.value)
    val recipe: LiveData<Recipe> get() = _recipe
    fun setRecipe(recipe: Recipe) {
        _recipe.value = recipe
    }

    fun resetRecipe() {
        _recipe = MutableLiveData<Recipe>(Recipe.currentRecipe.value?.apply {
            level = Level.EASY.number
            time = TimeConverter.hourAndMinuteToLong(0, 30)
            meals = 1
        })
    }

    fun addOrUpdateRecipe(recipe: Recipe, updateRecipes: Boolean) {
        if (updateRecipes) {
            val recipes = User.currentUser.value!!.recipes
            recipes.add(recipe.id)
            repository.updateUserRecipes(auth.uid!!, recipes)
            repository.addOrUpdateRecipe(recipe)
        }
        else repository.addOrUpdateRecipe(recipe.copy(public = false))
    }

    fun uploadPhoto(id: String, bytes: ByteArray) {
        repository.uploadPhoto(id, bytes)
    }

    fun updateIngredients(position: Int, text: String) {
        setRecipe(_recipe.value!!.apply {
            this.ingredients[position] = text
        })
    }

    fun updatePreparation(position: Int, text: String) {
        setRecipe(_recipe.value!!.apply {
            this.preparation[position] = text
        })
    }

    fun deleteIngredient(position: Int) {
        setRecipe(_recipe.value!!.apply {
            this.ingredients.removeAt(position)
        })
    }

    fun deletePreparation(position: Int) {
        setRecipe(_recipe.value!!.apply {
            this.preparation.removeAt(position)
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
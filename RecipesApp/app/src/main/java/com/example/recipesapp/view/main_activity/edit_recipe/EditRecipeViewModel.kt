package com.example.recipesapp.view.main_activity.edit_recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.model.entity.Ingredient
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.model.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth

class EditRecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FirebaseRepository()
    private val auth = FirebaseAuth.getInstance()

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

    fun deletePhoto(id: String) {
        repository.deletePhoto(id)
    }

    fun updatePreparation(position: Int, text: String) {
        Recipe.setEditRecipe(Recipe.editRecipe.value!!.apply {
            this.preparation[position] = text
        })
    }

    fun deleteIngredient(position: Int) {
        Recipe.setEditRecipe(Recipe.editRecipe.value!!.apply {
            this.ingredients.removeAt(position)
        })
    }

    fun deletePreparation(position: Int) {
        Recipe.setEditRecipe(Recipe.editRecipe.value!!.apply {
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

    private var _ingredientPosition = -1
    val ingredientPosition get() = _ingredientPosition

    private var _currentIngredient = Ingredient()
    val currentIngredient: Ingredient get() = _currentIngredient
    fun setCurrentIngredient(ingredient: Ingredient, position: Int) {
        _currentIngredient = ingredient
        _ingredientPosition = position
    }
    fun resetIngredient() {
        _currentIngredient = Ingredient()
        _ingredientPosition = -1
    }

    init {
        _visibleIngredients.value = true
        _visiblePreparation.value = true
    }
}
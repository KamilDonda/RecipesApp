package com.example.recipesapp.view.main_activity.one_recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.repository.FirebaseRepository

class OneRecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FirebaseRepository()

    fun setRecipeAsPublic(recipe: Recipe) {
        repository.addOrUpdateRecipe(recipe.copy(public = true))
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

    private var _isSelectedMode: MutableLiveData<Boolean> = MutableLiveData()
    val isSelectedMode: LiveData<Boolean> get() =_isSelectedMode
    fun setSelectedMode(mode: Boolean) {
        _isSelectedMode.value = mode
    }

    private var _allSelected: MutableLiveData<Boolean> = MutableLiveData()
    val allSelected: LiveData<Boolean> get() =_allSelected
    fun setAllSelected(selected: Boolean) {
        _allSelected.value = selected
    }

    init {
        _visibleIngredients.value = true
        _visiblePreparation.value = true
    }
}
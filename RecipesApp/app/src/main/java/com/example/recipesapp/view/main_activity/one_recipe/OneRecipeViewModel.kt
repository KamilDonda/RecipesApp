package com.example.recipesapp.view.main_activity.one_recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipesapp.model.entity.Ingredient
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.entity.User
import com.example.recipesapp.model.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth

class OneRecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FirebaseRepository()
    private val auth = FirebaseAuth.getInstance()

    fun setRecipeAsPublic(recipe: Recipe) {
        repository.addOrUpdateRecipe(recipe.copy(public = true))
    }

    fun deleteRecipe(recipe: Recipe) {
        repository.deleteRecipe(recipe)
        val user = User.currentUser.value!!
        user.recipes.apply {
            this.remove(recipe.id)
        }
        repository.updateUser(user)
    }

    fun changeFavouritesStatus(recipe: Recipe) {
        val user = User.currentUser.value!!
        val favourites = user.favourites

        if (user.favourites.contains(recipe.id)) {
            favourites.remove(recipe.id)    // delete from Favourites
        } else {
            favourites.add(recipe.id)       // add to Favourites
        }
        repository.updateUserFavourites(auth.uid!!, favourites)
    }

    fun updateBasket(items: ArrayList<Ingredient>) {
        val user = User.currentUser.value!!
        val basket = user.basket
        items.forEach {
            if (!basket.contains(it)) basket.add(it)
        }
        repository.updateUserBasket(auth.currentUser!!.uid, basket)
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
    val isSelectedMode: LiveData<Boolean> get() = _isSelectedMode
    fun setSelectedMode(mode: Boolean) {
        _isSelectedMode.value = mode
    }

    private var _allSelected: MutableLiveData<Boolean> = MutableLiveData()
    val allSelected: LiveData<Boolean> get() = _allSelected
    fun setAllSelected(selected: Boolean) {
        _allSelected.value = selected
    }

    init {
        _visibleIngredients.value = true
        _visiblePreparation.value = true
    }
}
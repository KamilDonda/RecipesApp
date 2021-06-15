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

    private var _name: MutableLiveData<String> = MutableLiveData()
    val name: LiveData<String> get() = _name
    fun setName(name: String) {
        _name.value = name
    }

    private var _level: MutableLiveData<Int> = MutableLiveData()
    val level: LiveData<Int> get() = _level
    fun setLevel(level: Int) {
        _level.value = level
    }

    private var _time: MutableLiveData<Long> = MutableLiveData()
    val time: LiveData<Long> get() = _time
    fun setTime(time: Long) {
        _time.value = time
    }

    private var _meals: MutableLiveData<Int> = MutableLiveData()
    val meals: LiveData<Int> get() = _meals
    fun setMeals(meals: Int) {
        _meals.value = meals
    }

    fun fetchData(recipe: Recipe) {
        isDataDefault = false
        setName(recipe.name)
        setLevel(recipe.level)
        setTime(recipe.time)
        setMeals(recipe.meals)
    }

    fun clearData() {
        isDataDefault = true
        setName("")
        setLevel(Level.EASY.number)
        setTime(TimeConverter().hourAndMinuteToLong(0, 30))
        setMeals(1)
    }
}
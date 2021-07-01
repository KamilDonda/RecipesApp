package com.example.recipesapp.model.entity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.Serializable

data class User(
    val uid: String,
    var nickname: String = "",
    val recipes: ArrayList<String> = ArrayList(),
    val favourites: ArrayList<String> = ArrayList()
): Serializable {

    constructor() : this("", "", ArrayList(), ArrayList())

    companion object {
        private var _currentUser: MutableLiveData<User> = MutableLiveData()
        val currentUser: LiveData<User> get() = _currentUser
        fun setCurrentUser(user: User) {
            _currentUser.value = user
        }
    }
}
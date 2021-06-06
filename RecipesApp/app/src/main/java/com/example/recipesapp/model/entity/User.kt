package com.example.recipesapp.model.entity

import java.io.Serializable

data class User(
    val uid: String,
    var nickname: String = "",
    val recipes: ArrayList<String> = ArrayList()
): Serializable {

    constructor() : this("", "", ArrayList())
}
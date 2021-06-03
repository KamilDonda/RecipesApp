package com.example.recipesapp.model.entity

data class Recipe(
    val id: String,
    var name: String,
    var author: String = "",
    var level: Int = 0,
    var time: Long = 0,
    var meals: Int = 0,
    var ingredients: ArrayList<String> = ArrayList(),
    var preparation: ArrayList<String> = ArrayList(),
    var public: Boolean = false,
    var ratingCount: Int = 0,
    var ratingSum: Int = 0
)

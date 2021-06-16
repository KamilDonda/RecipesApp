package com.example.recipesapp.model.entity

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
    var rating: Float
) : Serializable {

    constructor() : this("", "", "", 0, 0, 0, ArrayList(), ArrayList(), false, 0, 0, 0f)
}

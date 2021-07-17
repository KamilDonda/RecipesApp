package com.example.recipesapp.model.entity

data class Ingredient(
    var name: String = "",
    var number: Float = 0f,
    var unit: Int = Unit.GRAM.number
)

package com.example.recipesapp.model.entity

data class Ingredient(
    var name: String = "",
    var amount: Float = 0f,
    var unit: Int = Unit.GRAM.number,
    var isSelected: Boolean = false
)
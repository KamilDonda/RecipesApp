package com.example.recipesapp.model.entity

import com.example.recipesapp.R

enum class Unit(val number: Int, val name_id: Int, val abbr_id: Int) {
    GRAM(1, R.string.gram, R.string.g),
    MILLILITRE(2, R.string.millilitre, R.string.ml),
    PIECE(3, R.string.piece, R.string.pc)
}
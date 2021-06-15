package com.example.recipesapp.model.entity

import com.example.recipesapp.R

enum class Level(val number: Int, val id: Int) {
    EASY(1, R.string.easy),
    MEDIUM(2, R.string.medium),
    HARD(3, R.string.hard)
}
package com.example.recipesapp.model.entity

import com.example.recipesapp.R

enum class Level(val number: Int, val text_id: Int, val icon_id: Int) {
    EASY(1, R.string.easy, R.drawable.ic_lvl_easy),
    MEDIUM(2, R.string.medium, R.drawable.ic_lvl_medium),
    HARD(3, R.string.hard, R.drawable.ic_lvl_hard)
}
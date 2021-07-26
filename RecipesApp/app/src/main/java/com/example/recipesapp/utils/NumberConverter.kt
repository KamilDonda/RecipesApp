package com.example.recipesapp.utils

class NumberConverter {
    companion object {
        fun isWhole(value: Float): String {
            if (value - value.toInt() == 0f)
                return value.toInt().toString()
            return value.toString()
        }
    }
}
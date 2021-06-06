package com.example.recipesapp.model.utils

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.recipesapp.R

class RatingSystem {
    fun displayStars(context: Context, layout: LinearLayout, mean: Float) {
        val filled = R.drawable.ic_filled_star
        val half = R.drawable.ic_half_star
        val empty = R.drawable.ic_empty_star

        for (i in 1..5) {
            val star = ImageView(context)
            star.layoutParams = LinearLayout.LayoutParams(25, 25)
            star.setImageResource(filled)
            layout.addView(star)
        }
    }
}
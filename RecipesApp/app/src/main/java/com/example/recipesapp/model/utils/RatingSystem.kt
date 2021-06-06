package com.example.recipesapp.model.utils

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.recipesapp.R
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.roundToInt

class RatingSystem {
    fun displayStars(context: Context, layout: LinearLayout, mean: Float) {
        val stars = listOf(R.drawable.ic_filled_star, R.drawable.ic_half_star, R.drawable.ic_empty_star)
        val list = calculateStars(mean)

        list.forEachIndexed { index, element ->
            for (i in 0 until element) {
                createStar(context, layout, stars[index])
            }
        }
    }

    private fun calculateStars(mean: Float): Array<Int> {
        val max = 5

        val h_count: Int
        val e_count: Int
        val f_count: Int

        val temp = round(mean * 10) / 10
        val difference = temp - temp.roundToInt()

        if (difference <= 0.25 || difference >= 0.75) {
            f_count = mean.roundToInt()
            h_count = 0
            e_count = max - f_count
        } else {
            f_count = floor(mean).toInt()
            h_count = 1
            e_count = max - f_count - h_count
        }
        return arrayOf(f_count, h_count, e_count)
    }

    private fun createStar(context: Context, layout: LinearLayout, image: Int) {
        val star = ImageView(context)
        star.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        star.setImageResource(image)
        layout.addView(star)
    }
}
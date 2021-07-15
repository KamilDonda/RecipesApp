package com.example.recipesapp.utils

import android.content.Context
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.VisibleForTesting
import androidx.core.view.allViews
import com.example.recipesapp.R
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.roundToInt


class RatingSystem {
    companion object {
        fun displayStars(context: Context, layout: LinearLayout, mean: Float, size: Float? = null) {
            val stars =
                listOf(R.drawable.ic_star_fill, R.drawable.ic_star_half, R.drawable.ic_star_empty)
            val list = calculateStars(mean)

            if (layout.allViews.toList().size == 1) // Prevents duplicate stars
                list.forEachIndexed { index, element ->
                    for (i in 0 until element) {
                        createStar(context, layout, stars[index], size)
                    }
                }
        }

        @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
        fun calculateStars(mean: Float): Array<Int> {
            val max = 5

            val h_count: Int
            val e_count: Int
            val f_count: Int

            val temp = round(mean * 100) / 100
            val difference = temp - temp.toInt()
            if (difference < 0.25 || difference >= 0.75) {
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

        private fun createStar(
            context: Context,
            layout: LinearLayout,
            image: Int,
            size: Float? = null
        ) {
            val star = ImageView(context)
            star.setImageResource(image)

            if (size != null) {
                val dimensionInDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    size,
                    context.resources.displayMetrics
                ).toInt()
                star.layoutParams = LinearLayout.LayoutParams(dimensionInDp, dimensionInDp)
                star.requestLayout()
            }

            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT
            )
            params.weight = 1.0f
            val nestedLayout = LinearLayout(context).apply {
                layoutParams = params
            }
            nestedLayout.addView(star)
            layout.addView(nestedLayout)
        }
    }
}
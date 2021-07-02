package com.example.recipesapp.utils

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.recipesapp.R

class Photo {
    companion object {
        fun setPhoto(image: String, context: Context, imageView: ImageView) {
            if (image.isEmpty()) imageView.setImageDrawable(
                ContextCompat.getDrawable(context, R.drawable.meal_example)!!
            )
            else
                Glide.with(context).asBitmap().load(image).into(
                    BitmapImageViewTarget(imageView)
                )
        }
    }
}
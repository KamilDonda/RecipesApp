package com.example.recipesapp.view.main_activity.edit_recipe.ingredient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.recipesapp.R
import kotlinx.android.synthetic.main.dialog_fragment_ingredient.*

class IngredientDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_ingredient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ingredientName_textInput.setOnClickListener {

        }

        ingredientCount_textInput.setOnClickListener {

        }

        ingredientUnit_button.setOnClickListener {

        }

        cancel_button.setOnClickListener {
            dismiss()
        }

        submit_button.setOnClickListener {
            dismiss()
        }
    }
}
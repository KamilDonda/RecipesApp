package com.example.recipesapp.view.main_activity.edit_recipe.ingredient

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Unit
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_fragment_ingredient.*


class IngredientDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_ingredient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRadioButtons()
        setupDismissButtons()
    }

    @SuppressLint("SetTextI18n")
    private fun setupRadioButtons() {
        Unit.values().forEach {
            val radio = MaterialRadioButton(requireContext())
            radio.text = "${getString(it.name_id)} [${getString(it.abbr_id)}]"
            radio.id = it.number
            val colorStateList =
                AppCompatResources.getColorStateList(requireContext(), R.color.green)
            radio.buttonTintList = colorStateList
            unit_radioGroup.addView(radio)
        }
        unit_radioGroup.check(Unit.GRAM.number)
    }

    private fun setupDismissButtons() {
        cancel_button.setOnClickListener {
            dismiss()
        }

        submit_button.setOnClickListener {
            when {
                ingredientName_textInput.text.isNullOrEmpty() -> showSnackbar(getString(R.string.empty_ingredient_name))
                ingredientCount_textInput.text.isNullOrEmpty() -> showSnackbar(getString(R.string.empty_ingredient_count))
                else -> dismiss()
            }
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }
}
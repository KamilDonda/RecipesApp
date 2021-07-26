package com.example.recipesapp.view.main_activity.edit_recipe.ingredient

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Ingredient
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.model.entity.Unit
import com.example.recipesapp.utils.NumberConverter
import com.example.recipesapp.view.main_activity.edit_recipe.EditRecipeViewModel
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_fragment_ingredient.*


class IngredientDialogFragment : DialogFragment() {

    private lateinit var editRecipeViewModel: EditRecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        editRecipeViewModel =
            ViewModelProvider(requireActivity()).get(EditRecipeViewModel::class.java)

        return inflater.inflate(R.layout.dialog_fragment_ingredient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRadioButtons()
        setupData()
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

    private fun setupData() {
        val ingredient = editRecipeViewModel.currentIngredient
        if (ingredient != Ingredient()) { // check if Ingredient is not default
            ingredientName_textInput.setText(ingredient.name)
            ingredientAmount_textInput.setText(NumberConverter.isWhole(ingredient.amount))
            unit_radioGroup.check(ingredient.unit)
            materialTextView.text = getString(R.string.edit_ingredient)
        }
    }

    private fun setupDismissButtons() {
        cancel_button.setOnClickListener {
            dismiss()
        }

        submit_button.setOnClickListener {
            when {
                ingredientName_textInput.text.isNullOrEmpty() -> showSnackbar(getString(R.string.empty_ingredient_name))
                ingredientAmount_textInput.text.isNullOrEmpty() -> showSnackbar(getString(R.string.empty_ingredient_count))
                else -> addIngredient()
            }
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun addIngredient() {
        val ingredients = Recipe.editRecipe.value!!.ingredients

        val radio: RadioButton =
            requireView().findViewById(unit_radioGroup.checkedRadioButtonId)
        val ingredient = Ingredient(
            ingredientName_textInput.text.toString(),
            ingredientAmount_textInput.text.toString().toFloat(),
            radio.id
        )

        val position = editRecipeViewModel.ingredientPosition

        if (position == -1) ingredients.add(ingredient)
        else ingredients[position] = ingredient

        Recipe.setEditRecipe(Recipe.editRecipe.value!!.copy(ingredients = ingredients))

        dismiss()
    }

    override fun dismiss() {
        super.dismiss()
        editRecipeViewModel.resetIngredient()
    }

//    override fun onStart() {
//        super.onStart()
//        dialog?.window?.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//    }
}
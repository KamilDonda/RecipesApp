package com.example.recipesapp.view.main_activity.edit_recipe

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.recipesapp.R
import com.example.recipesapp.adapter.EditTextAdapter
import com.example.recipesapp.model.entity.Level
import com.example.recipesapp.model.entity.Recipe
import com.example.recipesapp.utils.Photo
import com.example.recipesapp.utils.RecipeMenu
import com.example.recipesapp.utils.TimeConverter
import com.example.recipesapp.view.base.BaseFragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.auth.FirebaseAuth
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.fragment_edit_recipe.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*

class EditRecipeFragment : BaseFragment() {

    private lateinit var editRecipeViewModel: EditRecipeViewModel

    private lateinit var levelMenu: RecipeMenu
    private lateinit var mealsMenu: RecipeMenu

    private lateinit var ingredientsListAdapter: EditTextAdapter
    private lateinit var preparationListAdapter: EditTextAdapter

    private val auth = FirebaseAuth.getInstance()

    private var photo: Bitmap? = null
    private val GALLERY_REQUEST_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        editRecipeViewModel =
            ViewModelProvider(requireActivity()).get(EditRecipeViewModel::class.java)

        ingredientsListAdapter =
            EditTextAdapter(
                { p: Int, s: String -> editRecipeViewModel.updateIngredients(p, s) },
                { editRecipeViewModel.deleteIngredient(it) }
            )
        preparationListAdapter =
            EditTextAdapter(
                { p: Int, s: String -> editRecipeViewModel.updatePreparation(p, s) },
                { editRecipeViewModel.deletePreparation(it) }
            )

        return inflater.inflate(R.layout.fragment_edit_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        setupMenus()

        setupPickClick()
        setupResetClick()
        setupSaveButtonClick()
        setupNameTextChange()
        setupLevelClick()
        setupTimeClick()
        setupMealsClick()
        setupIngredientsAddClick()
        setupPreparationAddClick()

        setupIngredientsClick()
        setupPreparationClick()
        setupIngredientsObserver()
        setupPreparationObserver()

        ingredients_recyclerView.adapter = ingredientsListAdapter
        preparation_recyclerView.adapter = preparationListAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK)
                    data?.data?.let {
                        launchImageCrop(it)
                    }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == RESULT_OK) {
                    result.uri?.let {
                        setPhoto(it)
                    }
                }
            }
        }
    }

    private fun launchImageCrop(uri: Uri) {
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(150, 100)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(requireContext(), this);
    }

    private fun setPhoto(uri: Uri) {
        photo = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        if (photo!!.width > 1125)
            photo = Bitmap.createScaledBitmap(photo!!, 1125, 750, true)
        imageView_edit_recipe.setImageBitmap(photo)
    }

    private fun setupData() {
        Recipe.editRecipe.observe(viewLifecycleOwner) {
            try {
                if (name_textInput.text.toString() != it.name)
                    name_textInput.setText(it.name)
                val lvl = Level.values().find { lvl -> lvl.number == it.level }!!
                level_textView.text = getString(lvl.text_id)
                level_imageView.setImageDrawable(
                    AppCompatResources.getDrawable(requireContext(), lvl.icon_id)
                )
                time_textView.text = TimeConverter.longToString(it.time)
                meals_textView.text = it.meals.toString()
                if (photo == null)
                    Photo.setPhoto(it.image, requireContext(), imageView_edit_recipe)

                ingredientsListAdapter.setList(it.ingredients)
                preparationListAdapter.setList(it.preparation)
            } catch (e: Exception) {
                Log.v("Error", e.toString())
            }
        }
    }

    private fun setupMenus() {
        levelMenu = RecipeMenu(
            requireContext(),
            level_button,
            Level.values().map { getString(it.text_id) },
            "level"
        )
        mealsMenu = RecipeMenu(
            requireContext(),
            meals_button,
            (1..10).toList(),
            "meals"
        )
    }

    private fun setupPickClick() {
        addImage_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            intent.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }
    }

    private fun setupResetClick() {
        resetImage_button.setOnClickListener {
            imageView_edit_recipe.setImageResource(R.drawable.meal_example)
            Recipe.setEditRecipe(Recipe.editRecipe.value!!.copy(image = ""))
        }
    }

    private fun setupSaveButtonClick() {
        save_button.setOnClickListener {
            var recipe = Recipe.editRecipe.value!!

            if (recipe.name.length >= 6) {
                when {
                    recipe.ingredients.isEmpty() ->
                        showSnackbar(getString(R.string.empty_ingredients_list))
                    recipe.preparation.isEmpty() ->
                        showSnackbar(getString(R.string.empty_preparation_list))
                    recipe.ingredients.contains("") ->
                        showSnackbar(getString(R.string.empty_ingredients))
                    recipe.preparation.contains("") ->
                        showSnackbar(getString(R.string.empty_preparation))
                    else -> {
                        var updateRecipes = false

                        // The condition is true when the recipe is new
                        if (recipe.id.isEmpty()) {
                            // Generate random uid
                            val recipe_id = UUID.randomUUID().toString()
                            val author = (auth.currentUser?.email ?: "anonym")
                            recipe = recipe.copy(id = recipe_id, author = author)
                            updateRecipes = true
                        }
                        editRecipeViewModel.addOrUpdateRecipe(recipe, updateRecipes)

                        // Update/Add photo
                        if (photo != null) {
                            val stream = ByteArrayOutputStream()
                            val result = photo!!.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                            val byteArray = stream.toByteArray()
                            if (result) editRecipeViewModel.uploadPhoto(recipe.id, byteArray)
                        }

                        // Delete photo
                        if (recipe.image.isEmpty()) {
                            editRecipeViewModel.deletePhoto(recipe.id)
                        }

                        showSnackbar(getString(R.string.saved_successfully))
                        findNavController().popBackStack()
                    }
                }
            } else showSnackbar(getString(R.string.name_too_short))
        }
    }

    private fun setupNameTextChange() {
        name_textInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!name_textInput.text.isNullOrEmpty()) {
                    Recipe.editRecipe.value?.copy(name = name_textInput.text.toString())
                        ?.let { Recipe.setEditRecipe(it) }
                }
            }
        })
    }

    private fun setupLevelClick() {
        level_button.setOnClickListener {
            levelMenu.showMenu()
        }
    }

    private fun setupTimeClick() {
        time_button.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(1)
                    .setMinute(0)
                    .setTitleText(R.string.select_time_desc)
                    .build()

            picker.addOnPositiveButtonClickListener {
                val h = picker.hour
                val m = picker.minute
                val time = TimeConverter.hourAndMinuteToLong(h, m)

                Recipe.editRecipe.value?.copy(time = time)?.let {
                    Recipe.setEditRecipe(it)
                }
            }

            picker.show(requireFragmentManager(), null)
        }
    }

    private fun setupMealsClick() {
        meals_button.setOnClickListener {
            mealsMenu.showMenu()
        }
    }

    private fun setupIngredientsAddClick() {
        addIngredient_button.setOnClickListener {
            val temp = Recipe.editRecipe.value!!.ingredients
            temp.add("")
            Recipe.setEditRecipe(Recipe.editRecipe.value!!.copy(ingredients = temp))
            ingredientsListAdapter.notifyItemInserted(temp.size - 1)
        }
    }

    private fun setupPreparationAddClick() {
        addPreparation_button.setOnClickListener {
            val temp = Recipe.editRecipe.value!!.preparation
            temp.add("")
            Recipe.setEditRecipe(Recipe.editRecipe.value!!.copy(preparation = temp))
            preparationListAdapter.notifyItemInserted(temp.size - 1)
        }
    }

    private fun setupIngredientsClick() {
        ingredients_constraintLayout.setOnClickListener {
            editRecipeViewModel.changeVisibilityOfIngredients()
        }
    }

    private fun setupPreparationClick() {
        preparation_constraintLayout.setOnClickListener {
            editRecipeViewModel.changeVisibilityOfPreparation()
        }
    }

    private fun setupIngredientsObserver() {
        editRecipeViewModel.visibleIngredients.observe(viewLifecycleOwner, Observer {
            ingredients_recyclerView.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun setupPreparationObserver() {
        editRecipeViewModel.visiblePreparation.observe(viewLifecycleOwner, Observer {
            preparation_recyclerView.visibility = if (it) View.VISIBLE else View.GONE
        })
    }
}
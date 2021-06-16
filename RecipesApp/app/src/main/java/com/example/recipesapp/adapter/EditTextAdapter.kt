package com.example.recipesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.example.recipesapp.model.entity.Recipe
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class EditTextAdapter(private val recipe: LiveData<Recipe>) :
    RecyclerView.Adapter<EditTextAdapter.EditTextHolder>() {

    inner class EditTextHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditTextHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.edit_text_row, parent, false)
        return EditTextHolder(view)
    }

    override fun getItemCount() = recipe.value?.ingredients?.size ?: 0

    override fun onBindViewHolder(holder: EditTextHolder, position: Int) {
        val text = holder.itemView.findViewById<TextInputEditText>(R.id.textInput)
        val number = holder.itemView.findViewById<MaterialTextView>(R.id.number_textView)
        val dragButton = holder.itemView.findViewById<ImageButton>(R.id.drag_button)

        number.text = (position + 1).toString()
        text.setText(recipe.value?.ingredients?.get(position) ?: "")
    }
}
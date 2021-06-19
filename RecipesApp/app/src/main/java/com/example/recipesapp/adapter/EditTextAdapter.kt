package com.example.recipesapp.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class EditTextAdapter(
    private val list: LiveData<ArrayList<String>>,
    private val updateItem: (Int, String) -> Unit
) :
    RecyclerView.Adapter<EditTextAdapter.EditTextHolder>() {

    inner class EditTextHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditTextHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.edit_text_row, parent, false)
        return EditTextHolder(view)
    }

    override fun getItemCount() = list.value?.size ?: 0

    override fun onBindViewHolder(holder: EditTextHolder, position: Int) {
        val text = holder.itemView.findViewById<TextInputEditText>(R.id.textInput)
        val number = holder.itemView.findViewById<MaterialTextView>(R.id.number_textView)
        val dragButton = holder.itemView.findViewById<ImageButton>(R.id.drag_button)

        number.text = (position + 1).toString()
        text.setText(list.value?.get(position))

        text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateItem(position, s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
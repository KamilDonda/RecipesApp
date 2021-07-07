package com.example.recipesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipesapp.R
import com.google.android.material.textview.MaterialTextView

class PreparationAdapter : RecyclerView.Adapter<PreparationAdapter.ListHolder>() {

    inner class ListHolder(view: View) : RecyclerView.ViewHolder(view)

    private val stringList = ArrayList<String>()

    fun setList(list: List<String>) {
        stringList.clear()
        stringList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_preparation, parent, false)
        return ListHolder(view)
    }

    override fun getItemCount() = stringList.size

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val number = holder.itemView.findViewById<MaterialTextView>(R.id.number_preparation_textView)
        val text = holder.itemView.findViewById<MaterialTextView>(R.id.materialTextView)

        number.text = (position + 1).toString()
        text.text = stringList[position]
    }
}
package com.example.recipesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(private val list: LiveData<ArrayList<String>>) :
    RecyclerView.Adapter<ListAdapter.ListHolder>() {

    inner class ListHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ListHolder(view)
    }

    override fun getItemCount() = list.value?.size ?: 0

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val text = holder.itemView.rootView as TextView
        text.text = list.value?.get(position)
    }
}
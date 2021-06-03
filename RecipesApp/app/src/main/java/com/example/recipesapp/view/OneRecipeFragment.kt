package com.example.recipesapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.recipesapp.R
import kotlinx.android.synthetic.main.fragment_one_recipe.*

class OneRecipeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_one_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ingredientList = arrayListOf("Jeden", "Dwa", "Trzy")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, ingredientList)

        ingredients_listView.adapter = adapter
        
    }
}
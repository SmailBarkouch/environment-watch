package com.tensors.environment_watch.ui.mainscreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tensors.environment_watch.R
import com.tensors.environment_watch.api.SpeciesAdapter
import com.tensors.environment_watch.api.species
import com.tensors.environment_watch.ui.speciesscreen.SpeciesActivity
import kotlinx.android.synthetic.main.species_main_layout.*

class TabFragment : Fragment() {
    private fun setUpListView() {
        species_list.adapter = SpeciesAdapter(requireContext())
        species_list.visibility = View.VISIBLE
        species_list.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(activity, SpeciesActivity::class.java)
            intent.putExtra("name", species[position].name)
            intent.putExtra("fullName", species[position].fullName)
            intent.putExtra("httpRequestName", species[position].httpRequestName)
            intent.putExtra("state", species[position].state)
            intent.putExtra("fullDescription", "Description: " + species[position].description + " " + species[position].extendedDescription)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        setUpListView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.species_main_layout, container, false)
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): TabFragment {
            return TabFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}
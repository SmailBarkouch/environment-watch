package com.tensors.environment_watch.ui.mainscreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.tensors.environment_watch.R
import com.tensors.environment_watch.api.SpeciesAdapter
import com.tensors.environment_watch.ui.speciesscreen.SpeciesActivity
import kotlinx.android.synthetic.main.species_main_layout.*

class TabFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java)
    }

    private fun setUpListView() {
        species_list.adapter = SpeciesAdapter(requireContext())
        species_list.visibility = View.VISIBLE
        species_list.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(activity, SpeciesActivity::class.java)
            val speciesList = (species_list.adapter as SpeciesAdapter).species
            intent.putExtra("name", speciesList[position].name)
            intent.putExtra("fullName", speciesList[position].fullName)
            intent.putExtra("state", speciesList[position].state)
            intent.putExtra("fullDescription", "Description: " + speciesList[position].description + " " + speciesList[position].extendedDescription)
            startActivity(intent)
        }
    }

    private fun setUpMap() {

    }

    override fun onStart() {
        super.onStart()

        if(arguments?.getInt(ARG_SECTION_NUMBER) == 1) {
            setUpListView()
        } else {
            setUpMap()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(arguments?.getInt(ARG_SECTION_NUMBER) == 1) {
            return inflater.inflate(R.layout.species_main_layout, container, false)
        }

        return inflater.inflate(R.layout.species_loc_main_layout, container, false)
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
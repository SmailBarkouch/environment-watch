package com.tensors.environment_watch.ui.mainscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.storage.FirebaseStorage
import com.tensors.environment_watch.R
import com.tensors.environment_watch.api.SpeciesAdapter
import com.tensors.environment_watch.api.species
import com.tensors.environment_watch.ui.speciesscreen.SpeciesActivity
import kotlinx.android.synthetic.main.activity_species.*
import kotlinx.android.synthetic.main.species_loc_main_layout.*
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

        Log.e("Smail", "${arguments?.getInt(ARG_SECTION_NUMBER)}")
        if(arguments?.getInt(ARG_SECTION_NUMBER) == 1) {
            setUpListView()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(arguments?.getInt(ARG_SECTION_NUMBER) == 1) {
            return inflater.inflate(R.layout.species_main_layout, container, false)
        } else if (arguments?.getInt(ARG_SECTION_NUMBER) == 2) {
            return inflater.inflate(R.layout.species_loc_main_layout, container, false)
        }

        return null
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
package com.tensors.environment_watch.api

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.storage.FirebaseStorage
import com.tensors.environment_watch.R
import com.tensors.environment_watch.ui.mainscreen.TabFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return if(position == 0) {
            TabFragment.newInstance(position + 1)
        } else {
            val mapFragment = SupportMapFragment.newInstance()
            mapFragment.getMapAsync { googleMap ->
                species.forEach { specificSpecies ->
                    FirebaseStorage.getInstance().reference.child("coords/${specificSpecies.httpRequestName}")
                        .listAll().addOnSuccessListener { listResult ->
                            listResult?.items?.forEach { storageReference ->
                                storageReference.getBytes(1024 * 1024).addOnSuccessListener { byteArray ->
                                    val (lat, lon) = String(byteArray).split(" ")
                                    googleMap?.addMarker(
                                        MarkerOptions().position(
                                            LatLng(
                                                lat.toDouble(),
                                                lon.toDouble()
                                            )
                                        )
                                    )

                                }
                            }
                        }
                }
            }

            mapFragment
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int = TAB_TITLES.size
}
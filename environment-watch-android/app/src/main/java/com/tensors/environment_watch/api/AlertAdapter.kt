package com.tensors.environment_watch.api

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.tensors.environment_watch.R

data class Species(
    val name: String,
    val fullName: String,
    val httpRequestName: String,
    val state: String,
    val description: String = "",
    val extendedDescription: String = ""
)

val species = mutableListOf(
    Species("Red-cockaded Woodpecker", "L. borealis", "red_cockaded_woodpecker", "Near Threatened", "A Southern United States species of woodpeckers. They feathers are dotted in black and white, with a long black beak and round black eyes.", "Historically, this woodpecker's range extended in the southeastern United States from Florida to New Jersey and Maryland, as far west as eastern Texas and Oklahoma, and inland to Missouri, Kentucky, and Tennessee."),
    Species("Yellow-billed Cuckoo", "C. americanus", "yellow_billed_cuckoo", "Near Threatened", "A North and South American species of cuckoo. They have a yellow beak with round black eyes and hazel feathers.", "Common folk-names for this bird in the southern United States are rain crow and storm crow. These likely refer to the bird's habit of calling on hot days, often presaging rain or thunderstorms."),
    Species("Florida Jay", "A. coerulescens", "florida_jay", "Threatened", "A North American species of srub jay. They have a long pointy black beak with a white belly and blue feathers.", "The Florida scrub jay is found only in Florida scrub habitat, an ecosystem that exists only in central Florida and in limited areas along the Atlantic coast, and is characterized by nutrient-poor soil, occasional drought, and frequent wildfires."),
    Species("Rusty Blackbird", "E. carolinus", "rusty_blackbird", "Threatened", "A North American medium-sized blackbird. They have a pointed bill, yellowish eyes, and their feathers are black with a faint green and purple gloss.", "Rusty Blackbird prefers wet forested areas, breeding in the boreal forest and muskeg across northern Canada, and migrating southeast to the United States during winter. "),
    Species("Red-legged Kittiwake", "R. brevirostris", "red-legged_kittiwake", "Threatened", "A Subartic Pacific species of seabirds. They are red legged, have a short bill, and dark grey wings.", " Its numbers are thought to have decreased by about 35% between the mid-1970s and the mid-1990s though numbers may have stabilized since. It is unclear why they have declined.")
)

class SpeciesAdapter(context: Context): BaseAdapter() {

    private var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = species.size

    override fun getItem(position: Int): Any = species[position]

    override fun getItemId(position: Int): Long = position.toLong()

    @SuppressLint("InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var speciesView = convertView

        if(speciesView == null) {
            speciesView = inflater.inflate(R.layout.species_card_layout, null)
        }
        when(position) {
            0 -> speciesView?.findViewById<ImageView>(R.id.species_image)?.setImageResource(R.drawable.red_cockaded_woodpecker)
            1 -> speciesView?.findViewById<ImageView>(R.id.species_image)?.setImageResource(R.drawable.yellow_billed_cuckoo)
            2 -> speciesView?.findViewById<ImageView>(R.id.species_image)?.setImageResource(R.drawable.florida_jay)
            3 -> speciesView?.findViewById<ImageView>(R.id.species_image)?.setImageResource(R.drawable.rusty_blackbird)
            4 -> speciesView?.findViewById<ImageView>(R.id.species_image)?.setImageResource(R.drawable.red_legged_kittiwake)
        }
        speciesView?.findViewById<TextView>(R.id.simple_species_name)?.text = species[position].name
        speciesView?.findViewById<TextView>(R.id.full_species_name)?.text = species[position].fullName
        speciesView?.findViewById<TextView>(R.id.species_state)?.text = species[position].state
        speciesView?.findViewById<TextView>(R.id.species_description)?.text = species[position].description
        return speciesView!! // it's never null but kotlin doesn't realize that
    }


}
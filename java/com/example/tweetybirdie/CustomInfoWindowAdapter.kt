//Authors:
//Lonwabo Gade
//Koketso Motsikwe
//OPSC7312 POE(Final Part)
//Sources: Git(Paths to git repos can be provided upon request) and ChatGPT


package com.example.tweetybirdie

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
class CustomInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_info_window_layout, null)

        val tvBirdName = view.findViewById<TextView>(R.id.tvBirdName)
        val tvLocation = view.findViewById<TextView>(R.id.tvLocation)
        val tvDateSpotted = view.findViewById<TextView>(R.id.tvDateSpotted)

        // Set text for the TextViews using marker data
        tvBirdName.text = marker.title
        tvLocation.text = "Location: ${marker.position.latitude}, ${marker.position.longitude}"
        tvDateSpotted.text = "Date: Your Date Here"

        return view
    }
}

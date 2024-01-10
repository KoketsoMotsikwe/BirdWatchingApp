//Authors:
//Lonwabo Gade
//Koketso Motsikwe
//OPSC7312 POE(Final Part)
//Sources: Git(Paths to git repos can be provided upon request) and ChatGPT


package com.example.tweetybirdie.Models

import android.location.Location
import java.sql.Date


data class UserObservation(
    val ObservationID: String,
    val UserID: String,
    val Date: String,
    val BirdName: String,
    val Amount: Int,
    val Location: Location,
    val Note: String,
    val PlaceName: String,

    var IsAtHotspot: Boolean
)



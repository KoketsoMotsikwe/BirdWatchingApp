//Authors:
//Lonwabo Gade
//Koketso Motsikwe
//OPSC7312 POE(Final Part)
//Sources: Git(Paths to git repos can be provided upon request) and ChatGPT



package com.example.tweetybirdie

import android.app.Application
import android.location.Location
import android.util.Log
import com.example.tweetybirdie.Models.BirdModel
import com.example.tweetybirdie.Models.SightingModel
import com.example.tweetybirdie.Models.UserObservation
import com.example.tweetybirdie.Models.UsersModel
import java.sql.Date
import java.text.SimpleDateFormat
import com.google.firebase.firestore.FirebaseFirestore


class ToolBox : Application() {

    companion object {
        fun fetchUserObservations() {
            val db = FirebaseFirestore.getInstance()
            val userObservationsCollection = db.collection("observations")

            usersObservations.clear()

            userObservationsCollection.get().addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val data = document.data

                    val locationData = data["location"] as? Map<String, Any>
                    val latitude = locationData?.get("latitude") as? Double ?: 0.0
                    val longitude = locationData?.get("longitude") as? Double ?: 0.0

                    val location = Location("fused")
                    location.latitude = latitude
                    location.longitude = longitude

                    val observation = UserObservation(
                        ObservationID = data["observationID"] as? String ?: "",
                        UserID = data["userID"] as? String ?: "",
                        Date = data["date"] as String ?: "",
                        BirdName = data["birdName"] as? String ?: "",
                        Amount = (data["amount"] as? Number ?: 0).toInt(),
                        Location = location,
                        Note = data["note"] as? String ?: "",
                        PlaceName = data["placeName"] as? String ?: "",
                        IsAtHotspot = data["isAtHotspot"] as? Boolean ?: false,
                    )

                    usersObservations.add(observation)
                }

            }.addOnFailureListener { exception ->
                Log.e("AllObservations", "Error fetching all observations: $exception")
            }
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val testDate = dateFormat.parse("2023-10-19")

        var userRegion = ""
        var users = arrayListOf<UsersModel>()
        var usersObservations = arrayListOf<UserObservation>()
        var hotspotSightings: List<SightingModel> = mutableListOf()
        var birds: List<BirdModel> = mutableListOf()
        var hotspotsSightings: List<SightingModel> = mutableListOf()
        var birdsInTheRegion: List<BirdModel> = mutableListOf()
        var newObsOnHotspot = false;
        var populated = false


        var newObslat = 0.0
        var newObslng = 0.0
        var destlat = 0.0
        var destlng = 0.0
        var currentLat = 0.0
        var currentLng = 0.0
    }



    fun fetchUserObservations() {

    }
}
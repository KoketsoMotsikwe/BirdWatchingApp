//Authors:
//Lonwabo Gade
//Koketso Motsikwe
//OPSC7312 POE(Final Part)
//Sources: Git(Paths to git repos can be provided upon request) and ChatGPT


package com.example.tweetybirdie.Models


data class LocationDataClass(
    val id: String,
    val countryCode: String,
    val regionCode: String,
    val unused1: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val date: String,
    val unused2: Int
)
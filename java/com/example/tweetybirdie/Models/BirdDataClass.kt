//Authors:
//Lonwabo Gade
//Koketso Motsikwe
//OPSC7312 POE(Final Part)
//Sources: Git(Paths to git repos can be provided upon request) and ChatGPT


package com.example.tweetybirdie.Models


data class BirdDataClass(
    val scientificName: String,
    val commonName: String,
    val speciesCode: String,
    val category: String,
    val taxonOrder: Double,
    val comNameCodes: String,
    val sciNameCodes: String,
    val bandingCodes: String,
    val order: String,
    val familyComName: String,
    val familySciName: String,
    val reportAs: String,
    val extinct: String,
    val extinctYear: String,
    val familyCode: String
)
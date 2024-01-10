//Authors:
//Lonwabo Gade
//Koketso Motsikwe
//OPSC7312 POE(Final Part)
//Sources: Git(Paths to git repos can be provided upon request) and ChatGPT


package com.example.tweetybirdie.Models


data class UsersModel(
    var UserID: String = "",
    var Name: String = "",
    var Surname: String = "",
    var Email: String = "",
    var Hash: String = "",
    var isUnitKM: Boolean = true,
    var MaxDistance: Double = 5.0,
    var ChallengePoints: Int = 0,
    var mapStyleIsDark: Boolean = false
)

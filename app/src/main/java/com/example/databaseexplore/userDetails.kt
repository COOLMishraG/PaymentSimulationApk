package com.example.databaseexplore

data class UserDetails(
    val name: String,
    val password: String,
    val money: String,

    ) {
    constructor() : this("", "", "") // Default constructor required for Firebase
}



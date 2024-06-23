package com.example.databaseexplore

data class UserDetails(
    val name: String,
    val password: String,
    val money: String,
    val image: String,

    ) {
    constructor() : this("", "", "" , "") // Default constructor required for Firebase
}



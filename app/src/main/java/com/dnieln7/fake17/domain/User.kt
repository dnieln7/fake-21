package com.dnieln7.fake17.domain

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nombre")
    val name: String,
    @SerializedName("apellidos")
    val lastName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("intereses")
    val hobbies: List<String>,
    @SerializedName("rol")
    val role: String
)
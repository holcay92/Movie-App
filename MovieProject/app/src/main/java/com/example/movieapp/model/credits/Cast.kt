package com.example.movieapp.model.credits

import com.google.gson.annotations.SerializedName

data class Cast(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("profile_path") val profilePath: String?,
)

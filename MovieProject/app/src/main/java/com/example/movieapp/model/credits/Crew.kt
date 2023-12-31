package com.example.movieapp.model.credits

import com.google.gson.annotations.SerializedName

data class Crew(
    @SerializedName("id") val id: Int?,
    @SerializedName("job") val job: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("profile_path") val profilePath: String?,
)

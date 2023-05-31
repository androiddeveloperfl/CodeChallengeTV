package com.weathergroup.codechallengetv.data.models

import com.google.gson.annotations.SerializedName

class MoviesResponse : ArrayList<MoviesResponse.MoviesResponseItem>() {
    data class MoviesResponseItem(
        @SerializedName("id")
        val id: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("image_16_9")
        val image_16_9: String,
        @SerializedName("image_2_3")
        val image_2_3: String,
        @SerializedName("plot")
        val plot: String,
        @SerializedName("contentRating")
        val contentRating: String,
        @SerializedName("rating")
        val rating: String
    )
}

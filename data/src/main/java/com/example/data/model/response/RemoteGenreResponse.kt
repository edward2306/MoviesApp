package com.example.data.model.response

import com.example.domain.model.Genre
import com.google.gson.annotations.SerializedName

data class RemoteGenreResponse (
    @SerializedName("genres") var genres: List<Genre>? = null
)
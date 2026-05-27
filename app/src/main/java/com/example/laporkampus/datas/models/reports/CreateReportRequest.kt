package com.example.laporkampus.datas.models.reports

import com.google.gson.annotations.SerializedName

data class CreateReportRequest(
    @SerializedName("titleReport") val title: String,
    @SerializedName("descriptionReport") val description: String,
    @SerializedName("locationReport") val location: String,
    @SerializedName("floorReport") val floor: String,
    @SerializedName("roomReport") val room: String,
    @SerializedName("divisionReport") val division: String,
    @SerializedName("imageUrlReport") val imageUrl: String? = null
)
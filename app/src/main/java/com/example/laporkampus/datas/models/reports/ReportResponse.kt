package com.example.laporkampus.datas.models.reports

import com.google.gson.annotations.SerializedName

data class ReportResponse (
    @SerializedName("reportIdReport") val id: Int,
    @SerializedName("titleReport") val title: String,
    @SerializedName("descriptionReport") val description: String,
    @SerializedName("statusReport") val status: String, // PENDING, IN_PROGRESS, DONE, REJECTED
    @SerializedName("locationReport") val location: String,
    @SerializedName("floorReport") val floor: String,
    @SerializedName("roomReport") val room: String,
    @SerializedName("upvoteCountReport") val upvoteCount: Int,
    @SerializedName("noteReport") val note: String?
)
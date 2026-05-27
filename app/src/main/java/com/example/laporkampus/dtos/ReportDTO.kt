package com.example.laporkampus.dtos

import com.google.gson.annotations.SerializedName

// Request body for creating a report
data class CreateReportRequest(
    @SerializedName("titleReport") val titleReport: String,
    @SerializedName("descriptionReport") val descriptionReport: String,
    @SerializedName("locationReport") val locationReport: String,
    @SerializedName("floorReport") val floorReport: String,
    @SerializedName("roomReport") val roomReport: String,
    @SerializedName("divisionReport") val divisionReport: String,
    @SerializedName("imageUrlReport") val imageUrlReport: String? = null
)

// Response body for fetching reports (List and Detail)
data class ReportResponse(
    @SerializedName("reportIdReport") val reportIdReport: Int,
    @SerializedName("titleReport") val titleReport: String,
    @SerializedName("descriptionReport") val descriptionReport: String,
    @SerializedName("statusReport") val statusReport: String,
    @SerializedName("locationReport") val locationReport: String,
    @SerializedName("floorReport") val floorReport: String,
    @SerializedName("roomReport") val roomReport: String,
    @SerializedName("upvoteCountReport") val upvoteCountReport: Int,
    @SerializedName("noteReport") val noteReport: String
)

// Generic response for Cancel/Create actions
data class BasicReportResponse(
    @SerializedName("message") val message: String,
    @SerializedName("report") val report: ReportResponse? = null
)
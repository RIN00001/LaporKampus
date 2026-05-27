package com.example.laporkampus.datas.models.reports

import com.google.gson.annotations.SerializedName

data class ValidateReportRequest(
    @SerializedName("statusReport") val status: String,
    @SerializedName("noteReport") val note: String?
)
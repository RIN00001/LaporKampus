package com.example.laporkampus.datas.services

import com.example.laporkampus.datas.models.GeneralResponseModel
import com.example.laporkampus.datas.models.reports.ReportResponse
import com.example.laporkampus.datas.models.reports.ValidateReportRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ReportStaffService {
    @GET("api/reports/staff")
    fun getAllReportsBasedOnDivision(): Call<List<ReportResponse>>

    @GET("api/reports/staff/{id}")
    fun getReportDetail(@Path("id") id: Int): Call<ReportResponse>

    @PATCH("api/reports/validate/{id}")
    fun validateReport(@Path("id") id: Int, @Body request: ValidateReportRequest): Call<GeneralResponseModel>
}
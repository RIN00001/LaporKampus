package com.example.laporkampus.datas.services

import com.example.laporkampus.datas.models.GeneralResponseModel
import com.example.laporkampus.datas.models.reports.CreateReportRequest
import com.example.laporkampus.datas.models.reports.ReportResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ReportUserService {
    // To create a new report from a user
    @POST("api/reports")
    fun createReport(@Body request: CreateReportRequest): Call<GeneralResponseModel>

    // To get all reports for the logged-in user
    @GET("api/reports/me")
    fun getAllReportsUser(): Call<List<ReportResponse>>

    // To get a detailed report for a user
    @GET("api/reports/{id}")
    fun getReportDetailUser(@Path("id") id: Int): Call<ReportResponse>

    // To cancel a report by a user
    @PATCH("api/reports/{id}/cancel")
    fun cancelReportUser(@Path("id") id: Int): Call<GeneralResponseModel>
}
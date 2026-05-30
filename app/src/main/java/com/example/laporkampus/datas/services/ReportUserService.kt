package com.example.laporkampus.datas.services

import com.example.laporkampus.datas.models.GeneralResponseModel
import com.example.laporkampus.datas.models.reports.ReportResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ReportUserService {
    // To create a new report from a user using Multipart for image upload
    @Multipart
    @POST("api/reports")
    fun createReport(
        @Part("titleReport") title: RequestBody,
        @Part("descriptionReport") description: RequestBody,
        @Part("locationReport") location: RequestBody,
        @Part("floorReport") floor: RequestBody,
        @Part("roomReport") room: RequestBody,
        @Part("divisionReport") division: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<GeneralResponseModel>

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
package com.example.laporkampus.datas.repositories

import com.example.laporkampus.datas.models.GeneralResponseModel
import com.example.laporkampus.datas.models.reports.ReportResponse
import com.example.laporkampus.datas.services.ReportUserService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

interface ReportUserRepositoryInterface {
    fun createReport(
        title: RequestBody,
        description: RequestBody,
        location: RequestBody,
        floor: RequestBody,
        room: RequestBody,
        division: RequestBody,
        image: MultipartBody.Part?
    ): Call<GeneralResponseModel>
    fun getAllReportsUser(): Call<List<ReportResponse>>
    fun getReportDetailUser(id: Int): Call<ReportResponse>
    fun cancelReportUser(id: Int): Call<GeneralResponseModel>
}

class ReportUserRepository(private val reportUserService: ReportUserService) : ReportUserRepositoryInterface {

    // To send a new report from a user to the server using Multipart
    override fun createReport(
        title: RequestBody,
        description: RequestBody,
        location: RequestBody,
        floor: RequestBody,
        room: RequestBody,
        division: RequestBody,
        image: MultipartBody.Part?
    ): Call<GeneralResponseModel> {
        return reportUserService.createReport(
            title, description, location, floor, room, division, image
        )
    }

    // To fetch all reports created by the logged-in user
    override fun getAllReportsUser(): Call<List<ReportResponse>> {
        return reportUserService.getAllReportsUser()
    }

    // To fetch detailed information of a specific report for the user
    override fun getReportDetailUser(id: Int): Call<ReportResponse> {
        return reportUserService.getReportDetailUser(id)
    }

    // To request a cancellation of a report by the user
    override fun cancelReportUser(id: Int): Call<GeneralResponseModel> {
        return reportUserService.cancelReportUser(id)
    }
}
package com.example.laporkampus.datas.repositories

import com.example.laporkampus.datas.models.GeneralResponseModel
import com.example.laporkampus.datas.models.reports.CreateReportRequest
import com.example.laporkampus.datas.models.reports.ReportResponse
import com.example.laporkampus.datas.services.ReportUserService
import retrofit2.Call

interface ReportUserRepositoryInterface {
    fun createReport(request: CreateReportRequest): Call<GeneralResponseModel>
    fun getAllReportsUser(): Call<List<ReportResponse>>
    fun getReportDetailUser(id: Int): Call<ReportResponse>
    fun cancelReportUser(id: Int): Call<GeneralResponseModel>
}

class ReportUserRepository(private val reportUserService: ReportUserService) : ReportUserRepositoryInterface {

    // To send a new report from a user to the server
    override fun createReport(request: CreateReportRequest): Call<GeneralResponseModel> {
        return reportUserService.createReport(request)
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
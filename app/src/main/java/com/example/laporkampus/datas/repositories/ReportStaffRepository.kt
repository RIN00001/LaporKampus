package com.example.laporkampus.datas.repositories

import com.example.laporkampus.datas.models.GeneralResponseModel
import com.example.laporkampus.datas.models.reports.ReportResponse
import com.example.laporkampus.datas.models.reports.ValidateReportRequest
import com.example.laporkampus.datas.services.ReportStaffService
import retrofit2.Call

interface ReportStaffRepositoryInterface {
    fun getAllReports(): Call<List<ReportResponse>>
    fun getReportDetail(id: Int): Call<ReportResponse>
    fun validateReport(id: Int, status: String, note: String?): Call<GeneralResponseModel>
}

class ReportStaffRepository(private val reportStaffService: ReportStaffService): ReportStaffRepositoryInterface {
    override fun getAllReports(): Call<List<ReportResponse>> = reportStaffService.getAllReportsBasedOnDivision()

    override fun getReportDetail(id: Int): Call<ReportResponse> = reportStaffService.getReportDetail(id)

    override fun validateReport(id: Int, status: String, note: String?): Call<GeneralResponseModel> {
        return reportStaffService.validateReport(id, ValidateReportRequest(status, note))
    }
}
package com.example.laporkampus.screens.viewmodels

import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.laporkampus.LaporKampusApplication
import com.example.laporkampus.datas.models.GeneralResponseModel
import com.example.laporkampus.datas.models.reports.ReportResponse
import com.example.laporkampus.datas.repositories.ReportUserRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ReportUserViewModel(private val repository: ReportUserRepository) : ViewModel() {

    private val _reports = MutableLiveData<List<ReportResponse>>(emptyList())
    val reports: LiveData<List<ReportResponse>> = _reports

    private val _reportDetail = MutableLiveData<ReportResponse?>(null)
    val reportDetail: LiveData<ReportResponse?> = _reportDetail

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _actionSuccessMessage = MutableLiveData<String?>(null)
    val actionSuccessMessage: LiveData<String?> = _actionSuccessMessage

    // === 1. FUNGSI AMBIL DAFTAR LAPORAN ===
    fun getReportsUser() {
        _isLoading.value = true
        _errorMessage.value = null

        val call = repository.getAllReportsUser()
        call.enqueue(object : Callback<List<ReportResponse>> {
            override fun onResponse(call: Call<List<ReportResponse>>, response: Response<List<ReportResponse>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _reports.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = "Gagal mengambil data: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<ReportResponse>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.localizedMessage ?: "Terjadi kesalahan jaringan"
            }
        })
    }

    // === 2. FUNGSI AMBIL DETAIL LAPORAN ===
    fun getReportDetailUser(reportId: Int) {
        _isLoading.value = true
        _errorMessage.value = null

        val call = repository.getReportDetailUser(reportId)
        call.enqueue(object : Callback<ReportResponse> {
            override fun onResponse(call: Call<ReportResponse>, response: Response<ReportResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _reportDetail.value = response.body()
                } else {
                    _errorMessage.value = "Gagal mengambil detail: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.localizedMessage ?: "Terjadi kesalahan jaringan"
            }
        })
    }

    // === 3. FUNGSI CANCEL/BATALKAN LAPORAN ===
    fun cancelReportUser(reportId: Int) {
        _isLoading.value = true
        _errorMessage.value = null
        _actionSuccessMessage.value = null

        val call = repository.cancelReportUser(reportId)
        call.enqueue(object : Callback<GeneralResponseModel> {
            override fun onResponse(call: Call<GeneralResponseModel>, response: Response<GeneralResponseModel>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    _actionSuccessMessage.value = body?.message ?: "Laporan berhasil dibatalkan!"
                } else {
                    _errorMessage.value = "Gagal membatalkan laporan: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<GeneralResponseModel>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.localizedMessage ?: "Terjadi kesalahan jaringan"
            }
        })
    }

    // === 4. FUNGSI BUAT LAPORAN BARU DENGAN MULTIPART ===
    fun createReport(
        title: String,
        description: String,
        location: String,
        floor: String,
        room: String,
        division: String,
        imageFile: File? = null
    ) {
        _isLoading.value = true
        _errorMessage.value = null
        _actionSuccessMessage.value = null

        // Convert strings to RequestBody
        val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val locationBody = location.toRequestBody("text/plain".toMediaTypeOrNull())
        val floorBody = floor.toRequestBody("text/plain".toMediaTypeOrNull())
        val roomBody = room.toRequestBody("text/plain".toMediaTypeOrNull())
        val divisionBody = division.toRequestBody("text/plain".toMediaTypeOrNull())

        // Convert File to MultipartBody.Part if it exists
        var imagePart: MultipartBody.Part? = null
        if (imageFile != null) {
            // Dynamically resolve the correct MIME Type based on file extension
            val extension = imageFile.extension
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "image/jpeg"

            val requestFile = imageFile.asRequestBody(mimeType.toMediaTypeOrNull())
            // "imageReport" must match the name in your backend uploadMiddleware.single("imageReport")
            imagePart = MultipartBody.Part.createFormData("imageReport", imageFile.name, requestFile)
        }

        val call = repository.createReport(
            titleBody, descriptionBody, locationBody, floorBody, roomBody, divisionBody, imagePart
        )

        call.enqueue(object : Callback<GeneralResponseModel> {
            override fun onResponse(call: Call<GeneralResponseModel>, response: Response<GeneralResponseModel>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    _actionSuccessMessage.value = body?.message ?: "Laporan berhasil dibuat!"
                } else {
                    _errorMessage.value = "Gagal membuat laporan: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<GeneralResponseModel>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.localizedMessage ?: "Terjadi kesalahan jaringan"
            }
        })
    }

    fun clearStatus() {
        _errorMessage.value = null
        _actionSuccessMessage.value = null
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LaporKampusApplication)
                ReportUserViewModel(
                    repository = application.container.reportUserRepository
                )
            }
        }
    }
}
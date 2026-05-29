package com.example.laporkampus.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.example.laporkampus.datas.repositories.ReportStaffRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportStaffViewModel(private val repository: ReportStaffRepository) : ViewModel() {

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

    // Validate form state
    var selectedStatus by mutableStateOf("")
        private set

    var noteInput by mutableStateOf("")
        private set

    fun changeSelectedStatus(status: String) {
        selectedStatus = status
    }

    fun changeNoteInput(note: String) {
        noteInput = note
    }

    // === 1. FUNGSI AMBIL SEMUA LAPORAN BERDASARKAN DIVISI STAFF ===
    fun getAllReports() {
        _isLoading.value = true
        _errorMessage.value = null

        val call = repository.getAllReports()
        call.enqueue(object : Callback<List<ReportResponse>> {
            override fun onResponse(
                call: Call<List<ReportResponse>>,
                response: Response<List<ReportResponse>>
            ) {
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
    fun getReportDetail(reportId: Int) {
        _isLoading.value = true
        _errorMessage.value = null

        val call = repository.getReportDetail(reportId)
        call.enqueue(object : Callback<ReportResponse> {
            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
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

    // === 3. FUNGSI VALIDASI LAPORAN ===
    fun validateReport(reportId: Int) {
        if (selectedStatus.isEmpty()) {
            _errorMessage.value = "Silakan pilih status terlebih dahulu"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null
        _actionSuccessMessage.value = null

        val note = noteInput.ifBlank { null }
        val call = repository.validateReport(reportId, selectedStatus, note)
        call.enqueue(object : Callback<GeneralResponseModel> {
            override fun onResponse(
                call: Call<GeneralResponseModel>,
                response: Response<GeneralResponseModel>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _actionSuccessMessage.value =
                        response.body()?.message ?: "Laporan berhasil divalidasi!"
                } else {
                    val errorBody = response.errorBody()?.string()

                    val errorMessage = try {
                        org.json.JSONObject(errorBody ?: "")
                            .getString("message")
                    } catch (e: Exception) {
                        response.message()
                    }

                    _errorMessage.value = "Gagal memvalidasi laporan: $errorMessage"
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
                ReportStaffViewModel(
                    repository = application.container.reportStaffRepository
                )
            }
        }
    }
}

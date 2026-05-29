package com.example.laporkampus.screens.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laporkampus.screens.viewmodels.ReportStaffViewModel
import com.example.laporkampus.screens.views.components.CustomDropdownMenu
import com.example.laporkampus.screens.views.components.CustomOutlinedTextField

@Composable
fun StaffReportDetailView(
    reportId: Int,
    onNavigateBack: () -> Unit,
    onValidateSuccess: () -> Unit,
    viewModel: ReportStaffViewModel = viewModel(factory = ReportStaffViewModel.Factory),
    modifier: Modifier = Modifier
) {
    val report by viewModel.reportDetail.observeAsState(initial = null)
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val errorMessage by viewModel.errorMessage.observeAsState(initial = null)
    val actionSuccessMessage by viewModel.actionSuccessMessage.observeAsState(initial = null)

    val statusOptions = listOf("IN_PROGRESS", "DONE", "REJECTED")

    // Navigasi kembali setelah validasi berhasil
    LaunchedEffect(actionSuccessMessage) {
        if (actionSuccessMessage != null) {
            onValidateSuccess()
        }
    }

    LaunchedEffect(reportId) {
        viewModel.getReportDetail(reportId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F9))
    ) {
        // Header Gradasi Orange (konsisten dengan layar lain)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFFF6D00), Color(0xFFFF9100))
                    )
                )
                .padding(horizontal = 12.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Detail Laporan",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFFF9800))
                }
            }

            errorMessage != null && report == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = errorMessage ?: "Terjadi kesalahan",
                            color = Color(0xFFD32F2F),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.getReportDetail(reportId) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                        ) {
                            Text(text = "Coba Lagi", color = Color.White)
                        }
                    }
                }
            }

            else -> {
                report?.let { currentReport ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        // --- BAGIAN DETAIL LAPORAN ---

                        // Placeholder foto bukti
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFE0E0E0))
                        ) {
                            Text(
                                text = "📸 Foto Bukti Kendala",
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Tags lokasi + upvote
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(
                                    currentReport.location,
                                    currentReport.floor,
                                    currentReport.room
                                ).forEach { tag ->
                                    if (tag.isNotEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFFFF6F00))
                                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = tag,
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFFF9800))
                                    .padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = currentReport.upvoteCount.toString(),
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Judul laporan
                        Text(
                            text = currentReport.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Deskripsi laporan
                        Text(
                            text = currentReport.description,
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.fillMaxWidth()
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = Color.LightGray
                        )

                        // Status saat ini
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Status Saat Ini",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            val (statusBg, statusLabel) = when (currentReport.status.uppercase()) {
                                "IN_PROGRESS" -> Color(0xFF1565C0) to "DIPROSES"
                                "DONE"        -> Color(0xFF2E7D32) to "SELESAI"
                                "REJECTED"    -> Color(0xFF1A1A1A) to "DITOLAK"
                                else          -> Color(0xFF757575) to "MENUNGGU"
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(statusBg)
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = statusLabel,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Tindak lanjut (note) yang sudah ada
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Tindak Lanjut",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFEEEEEE))
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = currentReport.note
                                        ?: "Belum ada respon atau tindak lanjut dari staff admin kampus.",
                                    color = if (currentReport.note != null) Color.Black else Color.Gray,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 20.dp),
                            color = Color(0xFFE0E0E0)
                        )

                        // --- BAGIAN VALIDASI LAPORAN (khusus staff) ---

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Validasi Laporan",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2C3E50)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Dropdown pilih status
                            CustomDropdownMenu(
                                label = "Ubah Status",
                                options = statusOptions,
                                selectedOption = viewModel.selectedStatus,
                                onOptionSelected = { viewModel.changeSelectedStatus(it) }
                            )

                            // Field catatan / note
                            CustomOutlinedTextField(
                                value = viewModel.noteInput,
                                onValueChange = { viewModel.changeNoteInput(it) },
                                label = "Catatan Tindak Lanjut (opsional)",
                                placeholder = "Tulis catatan untuk pelapor...",
                                isMultiline = true
                            )

                            // Tampilkan error validasi jika ada
                            if (errorMessage != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = errorMessage!!,
                                    color = Color(0xFFD32F2F),
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Tombol submit validasi
                            Button(
                                onClick = { viewModel.validateReport(reportId) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                enabled = !isLoading,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF9800),
                                    disabledContainerColor = Color(0xFFFFCC80)
                                )
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(22.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        text = "Simpan Validasi",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

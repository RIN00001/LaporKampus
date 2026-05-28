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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laporkampus.screens.viewmodels.ReportUserViewModel

@Composable
fun ReportDetailScreen(
    reportId: Int,
    onNavigateBack: () -> Unit,
    viewModel: ReportUserViewModel = viewModel(factory = ReportUserViewModel.Factory),
    modifier: Modifier = Modifier
) {
    val report by viewModel.reportDetail.observeAsState(initial = null)
    val isLoading by viewModel.isLoading.observeAsState(initial = false)

    LaunchedEffect(reportId) {
        viewModel.getReportDetailUser(reportId)
    }

    Column(modifier = modifier.fillMaxSize().background(Color.White)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(colors = listOf(Color(0xFFFF6D00), Color(0xFFFF9100))))
                .padding(horizontal = 12.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Detail Laporan", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFFF9800))
            }
        } else {
            report?.let { currentReport ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFE0E0E0))
                    ) {
                        Text(text = "📸 Foto Bukti Kendala", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf(currentReport.location, currentReport.floor, currentReport.room)
                            .filter { it.isNotEmpty() }
                            .forEach { tag ->

                                Box(
                                    modifier = Modifier
                                        .shadow(
                                            elevation = 8.dp,
                                            shape = RoundedCornerShape(12.dp),
                                            ambientColor = Color.Black,
                                            spotColor = Color.Black
                                        )
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFE65100))
                                        .padding(horizontal = 15.dp, vertical = 12.dp)
                                ) {
                                    Text(
                                        text = tag,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dihapus Elvis Operatornya agar tidak kuning
                    Text(
                        text = currentReport.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))


                    Text(
                        text = "ID Laporan: ${currentReport.id}",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = currentReport.description,
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFF9800))
                                .padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentReport.upvoteCount.toString(),
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray)

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Status", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFC107))
                                .padding(14.dp)
                        ) {
                            Text(
                                text = currentReport.status,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Tindak Lanjut", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE1E1E1))
                                .padding(14.dp)
                        ) {
                            // Cuma Note yang perlu Elvis karena di data class menggunakan String?
                            Text(
                                text = currentReport.note ?: "Belum ada respon atau tindak lanjut dari staff admin kampus.",
                                color = if (currentReport.note != null) Color.Black else Color.Gray,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.example.laporkampus.screens.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laporkampus.datas.models.UserModel // Import model user
import com.example.laporkampus.screens.viewmodels.ReportUserViewModel
import com.example.laporkampus.screens.views.components.ReportCard

@Composable
fun ReportListView(
    user: UserModel?, // Tambahkan parameter user di sini
    onReportClick: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ReportUserViewModel = viewModel(factory = ReportUserViewModel.Factory),
    modifier: Modifier = Modifier
) {
    val reports by viewModel.reports.observeAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.observeAsState(initial = false)

    LaunchedEffect(Unit) {
        viewModel.getReportsUser()
    }

    Box(modifier = modifier.fillMaxSize().background(Color.White)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(colors = listOf(Color(0xFFFF6D00), Color(0xFFFF9100))))
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        // FIX: Mengubah teks statis menjadi dinamis seperti Dashboard
                        Text(text = "Your Report,", color = Color.White, fontSize = 14.sp)
                        Text(
                            text = "${user?.name ?: "Mahasiswa"}!",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFFF9800))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(reports) { report ->
                        ReportCard(report = report, onClick = { onReportClick(report.id) })
                    }
                }
            }
        }
    }
}
package com.example.laporkampus.screens.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laporkampus.datas.models.UserModel
import com.example.laporkampus.screens.viewmodels.ReportUserViewModel
import com.example.laporkampus.screens.views.components.ReportCard

@Composable
fun UserDashboardView(
    user: UserModel?,
    onLogout: () -> Unit,
    onNavigateToMyReports: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToCreateReport: () -> Unit,
    viewModel: ReportUserViewModel = viewModel(factory = ReportUserViewModel.Factory),
    modifier: Modifier = Modifier
) {
    val reports by viewModel.reports.observeAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getReportsUser()
    }

    Box(modifier = modifier.fillMaxSize().background(Color.White)) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Gradasi Orange
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(colors = listOf(Color(0xFFFF6D00), Color(0xFFFF9100))))
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Welcome,", color = Color.White, fontSize = 14.sp)
                        Text(
                            text = "${user?.name ?: "Mahasiswa"}!",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box {
                        IconButton(
                            onClick = {
                                expanded = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {

                            DropdownMenuItem(
                                text = {
                                    Text("My Reports")
                                },
                                onClick = {
                                    expanded = false
                                    onNavigateToMyReports()
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("Logout")
                                },
                                onClick = {
                                    expanded = false
                                    onLogout()
                                }
                            )
                        }
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
                    contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(reports) { report ->
                        ReportCard(report = report, onClick = { onNavigateToDetail(report.id) })
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onNavigateToCreateReport,
            shape = CircleShape,
            containerColor = Color(0xFFFF9800),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 20.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Laporan", modifier = Modifier.size(28.dp))
        }
    }
}
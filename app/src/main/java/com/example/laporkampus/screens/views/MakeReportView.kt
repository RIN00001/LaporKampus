package com.example.laporkampus.screens.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laporkampus.screens.viewmodels.ReportUserViewModel
import com.example.laporkampus.screens.views.components.CustomDropdownMenu
import com.example.laporkampus.screens.views.components.CustomOutlinedTextField

@Composable
fun MakeReportView(
    onNavigateBack: () -> Unit,
    viewModel: ReportUserViewModel = viewModel(factory = ReportUserViewModel.Factory),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val successMessage by viewModel.actionSuccessMessage.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }
    var division by remember { mutableStateOf("") }

    LaunchedEffect(successMessage, errorMessage) {
        successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearStatus()
            onNavigateBack()
        }
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearStatus()
        }
    }

    Box(modifier = modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFF8A00))
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Membuat Laporan",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFF6D00))
                        .clickable { /* TODO: Implement Image Picker */ },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "↓", color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Masukan Gambar Laporan", color = Color.White, fontWeight = FontWeight.Bold)
                        Text(text = "Opsional*", color = Color.White, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                CustomOutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = "Judul Laporan",
                    placeholder = "Judul Laporan"
                )

                CustomOutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Deskripsi Laporan",
                    placeholder = "Deskripsi Laporan",
                    isMultiline = true
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray)

                CustomDropdownMenu(
                    label = "Lokasi Gedung",
                    options = listOf("UC Main Building", "Other Building"),
                    selectedOption = location,
                    onOptionSelected = { location = it }
                )

                CustomDropdownMenu(
                    label = "Lantai",
                    options = listOf("Lantai 1", "Lantai 2", "Lantai 3"),
                    selectedOption = floor,
                    onOptionSelected = { floor = it }
                )

                CustomOutlinedTextField(
                    value = room,
                    onValueChange = { room = it },
                    label = "Ruangan (Angka)",
                    placeholder = "123"
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray)

                CustomDropdownMenu(
                    label = "Divisi",
                    options = listOf("ICT", "PM"),
                    selectedOption = division,
                    onOptionSelected = { division = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.createReport(title, description, location, floor, room, division)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A00)),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(text = "Membuat Laporan", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
package com.example.laporkampus.screens.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laporkampus.datas.models.reports.ReportResponse

@Composable
fun ReportCard(
    report: ReportResponse,
    onClick: () -> Unit
) {
    // Sinkronisasi Warna Status sesuai standard Figma & Backend
    val (statusText, statusColor) = when (report.status.uppercase()) {
        "IN_PROGRESS", "DIPROSES" -> "DIPROSES" to Color(0xFFFFBF00)  // Maroon tua Figma
        "DONE", "SELESAI" -> "SELESAI" to Color(0xFF66AD60)          // Hijau
        "REJECTED", "DIBATALKAN" -> "DIBATALKAN" to Color(0xFF000000) // Hitam
        else -> "MENUNGGU" to Color(0xFF949494)                       // Abu-Abu (Pending)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFF9800)) // Warna utama Orange Figma
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        // Placeholder Gambar Utama laporan
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = Color.Black,
                    spotColor = Color.Black
                )
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE0E0E0))
        ) {
            Text(
                text = "📸 Bukti Laporan",
                modifier = Modifier.align(Alignment.Center),
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Judul Laporan asli dari Backend
        Text(
            text = report.title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Ganti ke nama dari yang melaporkan (ganti di model)
        Text(
            text = "ID Laporan: #${report.id}",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 11.sp,
            modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
        )

        // SINKRONISASI TAGS: Mengambil data lokasi asli dari backend
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            listOf(report.location, report.floor, report.room).forEach { tag ->
                if (tag.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = Color.Black,
                                spotColor = Color.Black
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE65100)) // Kontras orange gelap
                            .padding(horizontal = 15.dp, vertical = 12.dp)
                    ) {
                        Text(text = tag, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }


        // Baris Status & Upvote
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Badge Status
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp),
                        ambientColor = Color.Black,
                        spotColor = Color.Black
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(statusColor)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(text = statusText, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            // Upvote Counter asli dari database
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = report.upvoteCount.toString(),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = "Upvote",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp).clip(RoundedCornerShape(18.dp)).background(Color(0xFFE65100)).padding(4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportCardPreview() {

    val dummyReport = ReportResponse(
        id = 101,
        title = "Lampu Kelas Mati",
        description = "Lampu di ruang kelas A203 mati sejak pagi dan mengganggu proses belajar.",
        status = "IN_PROGRESS",
        location = "Gedung A",
        floor = "Lantai 2",
        room = "A203",
        upvoteCount = 24,
        note = "Sedang ditangani teknisi"
    )

    ReportCard(
        report = dummyReport,
        onClick = {}
    )
}


package com.example.bowlingcenter.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bowlingcenter.ui.theme.*

@Composable
fun HomeScreen(
    onVerLista: () -> Unit,
    onNuevaReserva: () -> Unit,
    vm: HomeViewModel = viewModel()
) {
    val reservasHoy        by vm.reservasHoy.observeAsState(0)
    val canchasOcupadas    by vm.canchasOcupadas.observeAsState(0)
    val reservasActivas    by vm.reservasActivas.observeAsState(0)
    val reservasFinalizadas by vm.reservasFinalizadas.observeAsState(0)
    val proximas           by vm.proximasReservas.observeAsState(emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BowlingBlack)
    ) {
        // Fondo decorativo
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(BowlingRed.copy(alpha = 0.15f), Color.Transparent),
                    center = Offset(size.width * 0.8f, size.height * 0.1f),
                    radius = size.width * 0.6f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "BOWLING",
                        color = BowlingRed,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 6.sp
                    )
                    Text(
                        text = "CENTER",
                        color = BowlingWhite,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 6.sp
                    )
                }
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = BowlingAmber,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Grid de estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Reservas\nHoy",
                    value = reservasHoy.toString(),
                    color = BowlingRed
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Canchas\nOcupadas",
                    value = canchasOcupadas.toString(),
                    color = BowlingAmber
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Reservas\nActivas",
                    value = reservasActivas.toString(),
                    color = Color(0xFF4CAF50)
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    label = "Finalizadas",
                    value = reservasFinalizadas.toString(),
                    color = Color(0xFF9E9E9E)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Próximas reservas
            Text(
                text = "PRÓXIMAS RESERVAS",
                color = BowlingAmber,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (proximas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BowlingGray, RoundedCornerShape(12.dp))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sin reservas para hoy", color = Color.Gray, fontSize = 14.sp)
                }
            } else {
                proximas.forEach { reserva ->
                    ProximaReservaItem(
                        nombre = reserva.nombreCliente,
                        hora   = reserva.hora,
                        pista  = reserva.numeroPista
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Botones
            Button(
                onClick = onNuevaReserva,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BowlingRed),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Nueva Reserva", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onVerLista,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                border = BorderStroke(1.dp, BowlingAmber),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.List, contentDescription = null, tint = BowlingAmber)
                Spacer(Modifier.width(8.dp))
                Text("Ver Listado", color = BowlingAmber, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StatCard(modifier: Modifier, label: String, value: String, color: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = BowlingGray),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = label, color = color, fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold, lineHeight = 15.sp)
            Text(text = value, color = BowlingWhite, fontSize = 36.sp,
                fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun ProximaReservaItem(nombre: String, hora: String, pista: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BowlingGray, RoundedCornerShape(10.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(BowlingRed, CircleShape)
            )
            Spacer(Modifier.width(10.dp))
            Text(nombre, color = BowlingWhite, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(hora, color = BowlingAmber, fontSize = 13.sp)
            Text("Pista $pista", color = Color.Gray, fontSize = 13.sp)
        }
    }
}
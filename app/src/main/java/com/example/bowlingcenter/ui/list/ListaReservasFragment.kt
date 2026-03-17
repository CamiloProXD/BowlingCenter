
package com.example.bowlingcenter.ui.list

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bowlingcenter.data.model.Reserva
import com.example.bowlingcenter.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaReservasScreen(
    onBack: () -> Unit,
    onEditar: (Int) -> Unit,
    vm: ListaViewModel = viewModel()
) {
    val reservas by vm.reservas.observeAsState(emptyList())
    var query by remember { mutableStateOf("") }
    var reservaAEliminar by remember { mutableStateOf<Reserva?>(null) }

    // Diálogo de confirmación
    reservaAEliminar?.let { r ->
        AlertDialog(
            onDismissRequest = { reservaAEliminar = null },
            containerColor   = BowlingGray,
            title = { Text("Eliminar reserva", color = BowlingWhite, fontWeight = FontWeight.Bold) },
            text  = { Text("¿Eliminar la reserva de ${r.nombreCliente}?", color = Color.LightGray) },
            confirmButton = {
                TextButton(onClick = { vm.eliminar(r); reservaAEliminar = null }) {
                    Text("Eliminar", color = BowlingRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { reservaAEliminar = null }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BowlingBlack)
    ) {
        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BowlingGray)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = BowlingWhite)
            }
            Text(
                text = "LISTADO DE RESERVAS",
                color = BowlingWhite,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                fontSize = 15.sp
            )
        }

        // Buscador
        OutlinedTextField(
            value = query,
            onValueChange = { query = it; vm.buscar(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Buscar por cliente...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BowlingAmber) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = BowlingRed,
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor     = BowlingWhite,
                unfocusedTextColor   = BowlingWhite,
                cursorColor          = BowlingRed
            ),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        // Lista
        if (reservas.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay reservas", color = Color.Gray, fontSize = 16.sp)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(reservas, key = { it.id }) { reserva ->
                    ReservaCard(
                        reserva   = reserva,
                        onEditar  = { onEditar(reserva.id) },
                        onEliminar = { reservaAEliminar = reserva }
                    )
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun ReservaCard(reserva: Reserva, onEditar: () -> Unit, onEliminar: () -> Unit) {
    val estadoColor = when (reserva.estado) {
        "Activa"     -> Color(0xFF4CAF50)
        "Finalizada" -> Color(0xFF9E9E9E)
        else         -> BowlingRed
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = BowlingGray),
        shape    = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = reserva.nombreCliente,
                    color = BowlingWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Surface(
                    color = estadoColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = reserva.estado,
                        color = estadoColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                InfoChip(icon = Icons.Default.DateRange, text = reserva.fecha)
                InfoChip(icon = Icons.Default.Call,      text = reserva.hora)
                InfoChip(icon = Icons.Default.Star,      text = "Pista ${reserva.numeroPista}")
            }

            Spacer(Modifier.height(10.dp))
            Divider(color = Color.DarkGray, thickness = 0.5.dp)
            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, contentDescription = null,
                        tint = BowlingAmber, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Editar", color = BowlingAmber, fontSize = 13.sp)
                }
                TextButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = null,
                        tint = BowlingRed, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Eliminar", color = BowlingRed, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun InfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(13.dp))
        Spacer(Modifier.width(4.dp))
        Text(text, color = Color.LightGray, fontSize = 12.sp)
    }
}
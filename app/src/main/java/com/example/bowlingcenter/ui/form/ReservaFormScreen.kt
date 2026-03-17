package com.example.bowlingcenter.ui.form

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bowlingcenter.data.model.Reserva
import com.example.bowlingcenter.ui.theme.*
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaFormScreen(
    reservaId: Int = 0,
    onBack: () -> Unit,
    vm: ReservaFormViewModel = viewModel()
) {
    val context = LocalContext.current

    var nombre    by remember { mutableStateOf("") }
    var telefono  by remember { mutableStateOf("") }
    var fecha     by remember { mutableStateOf("") }
    var hora      by remember { mutableStateOf("") }
    var pista     by remember { mutableStateOf("1") }
    var jugadores by remember { mutableStateOf("4") }
    var estado    by remember { mutableStateOf("Activa") }

    var expandedPista     by remember { mutableStateOf(false) }
    var expandedJugadores by remember { mutableStateOf(false) }
    var expandedEstado    by remember { mutableStateOf(false) }

    val pistaOpciones     = (1..8).map { it.toString() }
    val jugadoresOpciones = (1..6).map { it.toString() }
    val estadoOpciones    = listOf("Activa", "Finalizada", "Cancelada")

    // Cargar datos si es edición
    LaunchedEffect(reservaId) {
        if (reservaId != 0) {
            vm.cargarReserva(reservaId) { r ->
                nombre    = r.nombreCliente
                telefono  = r.telefono
                fecha     = r.fecha
                hora      = r.hora
                pista     = r.numeroPista.toString()
                jugadores = r.cantidadJugadores.toString()
                estado    = r.estado
            }
        }
    }

    // Observar resultado del ViewModel
    val resultado by vm.resultado.observeAsState()

    // Navegar atrás solo si se guardó exitosamente
    LaunchedEffect(resultado) {
        if (resultado == "OK") {
            vm.resultado.value = null
            onBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BowlingBlack)
    ) {

        // ── Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BowlingGray)
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector        = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint               = BowlingWhite
                )
            }
            Text(
                text          = if (reservaId == 0) "NUEVA RESERVA" else "EDITAR RESERVA",
                color         = BowlingWhite,
                fontWeight    = FontWeight.Bold,
                letterSpacing = 2.sp,
                fontSize      = 15.sp
            )
        }

        // ── Contenido
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ── Banner de error
            if (resultado != null && resultado != "OK") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors   = CardDefaults.cardColors(
                        containerColor = BowlingRed.copy(alpha = 0.12f)
                    ),
                    shape  = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, BowlingRed)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector        = Icons.Default.Warning,
                            contentDescription = null,
                            tint               = BowlingRed,
                            modifier           = Modifier
                                .size(20.dp)
                                .padding(top = 1.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text       = resultado ?: "",
                            color      = BowlingRed,
                            fontSize   = 13.sp,
                            lineHeight = 19.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // ── Campos de texto
            FormField(
                label         = "Nombre del Cliente",
                value         = nombre,
                onValueChange = { nombre = it },
                icon          = Icons.Default.Person
            )

            FormField(
                label         = "Teléfono",
                value         = telefono,
                onValueChange = { telefono = it },
                icon          = Icons.Default.Phone
            )

            // Fecha — abre DatePickerDialog
            FormFieldReadOnly(
                label   = "Fecha",
                value   = fecha,
                icon    = Icons.Default.DateRange,
                onClick = {
                    val cal = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, y, m, d -> fecha = "%02d/%02d/%04d".format(d, m + 1, y) },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            )

            // Hora — abre TimePickerDialog
            FormFieldReadOnly(
                label   = "Hora",
                value   = hora,
                icon    = Icons.Default.Schedule,   // ← corregido
                onClick = {
                    val cal = Calendar.getInstance()
                    TimePickerDialog(
                        context,
                        { _, h, min -> hora = "%02d:%02d".format(h, min) },
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        true
                    ).show()
                }
            )

            // ── Dropdowns
            BowlingDropdown(
                label    = "Número de Pista",
                selected = pista,
                options  = pistaOpciones,
                expanded = expandedPista,
                onExpand = { expandedPista = it },
                onSelect = { pista = it }
            )

            BowlingDropdown(
                label    = "Cantidad de Jugadores",
                selected = jugadores,
                options  = jugadoresOpciones,
                expanded = expandedJugadores,
                onExpand = { expandedJugadores = it },
                onSelect = { jugadores = it }
            )

            BowlingDropdown(
                label    = "Estado",
                selected = estado,
                options  = estadoOpciones,
                expanded = expandedEstado,
                onExpand = { expandedEstado = it },
                onSelect = { estado = it }
            )

            Spacer(Modifier.height(8.dp))

            // ── Botón Guardar
            Button(
                onClick = {
                    if (nombre.isBlank() || fecha.isBlank() || hora.isBlank()) return@Button

                    // Calcular horaMinutos aquí también
                    val partes  = hora.split(":")
                    val minutos = partes[0].toInt() * 60 + partes[1].toInt()

                    vm.guardar(
                        Reserva(
                            id                = reservaId,
                            nombreCliente     = nombre,
                            telefono          = telefono,
                            fecha             = fecha,
                            hora              = hora,
                            horaMinutos       = minutos,   // ← esto faltaba
                            numeroPista       = pista.toInt(),
                            cantidadJugadores = jugadores.toInt(),
                            estado            = estado
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BowlingRed),
                shape  = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Guardar", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }

            // ── Botón Cancelar
            OutlinedButton(
                onClick  = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                border = BorderStroke(1.dp, Color.DarkGray),
                shape  = RoundedCornerShape(10.dp)
            ) {
                Text("Cancelar", color = Color.Gray, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

// ── Componentes reutilizables

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector
) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        label         = { Text(label, color = Color.Gray) },
        leadingIcon   = { Icon(icon, contentDescription = null, tint = BowlingAmber) },
        modifier      = Modifier.fillMaxWidth(),
        colors        = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = BowlingRed,
            unfocusedBorderColor = Color.DarkGray,
            focusedTextColor     = BowlingWhite,
            unfocusedTextColor   = BowlingWhite,
            cursorColor          = BowlingRed,
            focusedLabelColor    = BowlingRed
        ),
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun FormFieldReadOnly(
    label: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    OutlinedTextField(
        value         = value,
        onValueChange = {},
        label         = { Text(label, color = Color.Gray) },
        leadingIcon   = { Icon(icon, contentDescription = null, tint = BowlingAmber) },
        modifier      = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        enabled = false,
        colors  = OutlinedTextFieldDefaults.colors(
            disabledBorderColor      = Color.DarkGray,
            disabledTextColor        = BowlingWhite,
            disabledLabelColor       = Color.Gray,
            disabledLeadingIconColor = BowlingAmber
        ),
        shape = RoundedCornerShape(10.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BowlingDropdown(
    label: String,
    selected: String,
    options: List<String>,
    expanded: Boolean,
    onExpand: (Boolean) -> Unit,
    onSelect: (String) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded         = expanded,
        onExpandedChange = onExpand
    ) {
        OutlinedTextField(
            value         = selected,
            onValueChange = {},
            readOnly      = true,
            label         = { Text(label, color = Color.Gray) },
            trailingIcon  = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = BowlingRed,
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor     = BowlingWhite,
                unfocusedTextColor   = BowlingWhite,
                focusedLabelColor    = BowlingRed
            ),
            shape = RoundedCornerShape(10.dp)
        )
        ExposedDropdownMenu(
            expanded         = expanded,
            onDismissRequest = { onExpand(false) },
            modifier         = Modifier.background(BowlingGray)
        ) {
            options.forEach { op ->
                DropdownMenuItem(
                    text    = { Text(op, color = BowlingWhite) },
                    onClick = { onSelect(op); onExpand(false) }
                )
            }
        }
    }
}
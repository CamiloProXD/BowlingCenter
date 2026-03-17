package com.example.bowlingcenter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservas")
data class Reserva(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombreCliente: String,
    val telefono: String,
    val fecha: String,        
    val hora: String,
    val horaMinutos: Int = 0,
    val numeroPista: Int,
    val cantidadJugadores: Int,
    val estado: String
)
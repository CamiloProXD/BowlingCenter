package com.example.bowlingcenter.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.bowlingcenter.data.model.Reserva

@Dao
interface ReservaDao {

    @Insert
    suspend fun insertar(reserva: Reserva)

    @Update
    suspend fun actualizar(reserva: Reserva)

    @Delete
    suspend fun eliminar(reserva: Reserva)

    @Query("SELECT * FROM reservas ORDER BY fecha ASC, hora ASC")
    fun obtenerTodas(): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Reserva?

    @Query("""
        SELECT * FROM reservas 
        WHERE nombreCliente LIKE '%' || :nombre || '%'
        ORDER BY fecha ASC, hora ASC
    """)
    fun buscarPorNombre(nombre: String): LiveData<List<Reserva>>

    @Query("""
        SELECT COUNT(*) FROM reservas
        WHERE numeroPista = :pista
        AND fecha = :fecha
        AND estado = 'Activa'
        AND id != :excludeId
        AND (:horaInicio < horaMinutos + 60 AND :horaFin > horaMinutos)
    """)
    suspend fun contarConflictos(
        pista: Int,
        fecha: String,
        horaInicio: Int,
        horaFin: Int,
        excludeId: Int = 0
    ): Int

    @Query("""
        SELECT * FROM reservas
        WHERE numeroPista = :pista
        AND fecha = :fecha
        AND estado = 'Activa'
        AND id != :excludeId
        AND (:horaInicio < horaMinutos + 60 AND :horaFin > horaMinutos)
        LIMIT 1
    """)
    suspend fun obtenerConflicto(
        pista: Int,
        fecha: String,
        horaInicio: Int,
        horaFin: Int,
        excludeId: Int = 0
    ): Reserva?

    @Query("SELECT COUNT(*) FROM reservas WHERE fecha = :hoy")
    fun reservasHoy(hoy: String): LiveData<Int>

    @Query("""
        SELECT COUNT(DISTINCT numeroPista) FROM reservas 
        WHERE fecha = :hoy AND estado = 'Activa'
    """)
    fun canchasOcupadasHoy(hoy: String): LiveData<Int>

    @Query("SELECT COUNT(*) FROM reservas WHERE estado = 'Activa'")
    fun reservasActivas(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM reservas WHERE estado = 'Finalizada'")
    fun reservasFinalizadas(): LiveData<Int>

    @Query("""
        SELECT * FROM reservas 
        WHERE fecha = :hoy AND estado = 'Activa' 
        ORDER BY hora ASC 
        LIMIT 5
    """)
    fun proximasReservas(hoy: String): LiveData<List<Reserva>>
}
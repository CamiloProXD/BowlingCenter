package com.example.bowlingcenter.repository

import androidx.lifecycle.LiveData
import com.example.bowlingcenter.data.db.ReservaDao
import com.example.bowlingcenter.data.model.Reserva

class ReservaRepository(private val dao: ReservaDao) {

    val todasLasReservas: LiveData<List<Reserva>> = dao.obtenerTodas()

    fun buscarPorNombre(nombre: String): LiveData<List<Reserva>> =
        dao.buscarPorNombre(nombre)

    suspend fun obtenerPorId(id: Int): Reserva? =
        dao.obtenerPorId(id)

    fun reservasHoy(hoy: String)        = dao.reservasHoy(hoy)
    fun canchasOcupadas(hoy: String)    = dao.canchasOcupadasHoy(hoy)
    fun reservasActivas()               = dao.reservasActivas()
    fun reservasFinalizadas()           = dao.reservasFinalizadas()
    fun proximasReservas(hoy: String)   = dao.proximasReservas(hoy)

    suspend fun guardar(reserva: Reserva)    = dao.insertar(reserva)
    suspend fun actualizar(reserva: Reserva) = dao.actualizar(reserva)
    suspend fun eliminar(reserva: Reserva)   = dao.eliminar(reserva)

    private fun horaAMinutos(hora: String): Int {
        val partes = hora.split(":")
        return partes[0].toInt() * 60 + partes[1].toInt()
    }

    suspend fun obtenerConflicto(
        pista: Int,
        fecha: String,
        hora: String,
        excludeId: Int = 0
    ): Reserva? {
        val horaInicio = horaAMinutos(hora)
        val horaFin    = horaInicio + 60
        return dao.obtenerConflicto(pista, fecha, horaInicio, horaFin, excludeId)
    }

    suspend fun existeConflicto(
        pista: Int,
        fecha: String,
        hora: String,
        excludeId: Int = 0
    ): Boolean {
        val horaInicio = horaAMinutos(hora)
        val horaFin    = horaInicio + 60
        return dao.contarConflictos(pista, fecha, horaInicio, horaFin, excludeId) > 0
    }

}
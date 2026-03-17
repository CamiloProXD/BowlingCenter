package com.example.bowlingcenter.ui.form

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.bowlingcenter.data.db.AppDatabase
import com.example.bowlingcenter.data.model.Reserva
import com.example.bowlingcenter.repository.ReservaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReservaFormViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ReservaRepository(
        AppDatabase.getDatabase(app).reservaDao()
    )

    // "OK" = guardado exitoso | mensaje de error = conflicto
    val resultado = MutableLiveData<String?>()

    fun guardar(reserva: Reserva) {
        CoroutineScope(Dispatchers.IO).launch {

            val reservaConflicto = repo.obtenerConflicto(
                reserva.numeroPista,
                reserva.fecha,
                reserva.hora,
                reserva.id
            )

            if (reservaConflicto != null) {
                // Calcular horaFin basado en la reserva YA GUARDADA
                val horaFinMinutos = reservaConflicto.horaMinutos + 60
                val horaFin = String.format(
                    "%02d:%02d",
                    horaFinMinutos / 60,
                    horaFinMinutos % 60
                )
                resultado.postValue(
                    "⚠️ La pista ${reserva.numeroPista} ya está ocupada de " +
                            "${reservaConflicto.hora} a $horaFin. Elige otra hora o pista."
                )
            } else {
                if (reserva.id == 0) repo.guardar(reserva)
                else repo.actualizar(reserva)
                resultado.postValue("OK")
            }
        }
    }

    // Usado por ReservaFormScreen para pre-llenar el formulario al editar
    fun cargarReserva(id: Int, onLoaded: (Reserva) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val reserva = repo.obtenerPorId(id)
            reserva?.let {
                CoroutineScope(Dispatchers.Main).launch { onLoaded(it) }
            }
        }
    }
}
package com.example.bowlingcenter.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.example.bowlingcenter.data.db.AppDatabase
import com.example.bowlingcenter.data.model.Reserva
import com.example.bowlingcenter.repository.ReservaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListaViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ReservaRepository(
        AppDatabase.getDatabase(app).reservaDao()
    )

    private val _query = MutableLiveData("")

    // Cambia automáticamente según el texto buscado
    val reservas = _query.switchMap { q ->
        if (q.isNullOrBlank()) repo.todasLasReservas
        else repo.buscarPorNombre(q)
    }

    fun buscar(texto: String) {
        _query.value = texto
    }

    fun eliminar(reserva: Reserva) {
        CoroutineScope(Dispatchers.IO).launch {
            repo.eliminar(reserva)
        }
    }
}
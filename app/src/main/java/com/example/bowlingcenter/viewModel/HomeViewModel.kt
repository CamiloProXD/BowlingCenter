package com.example.bowlingcenter.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.bowlingcenter.data.db.AppDatabase
import com.example.bowlingcenter.repository.ReservaRepository
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ReservaRepository(
        AppDatabase.getDatabase(app).reservaDao()
    )

    private val hoy: String =
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    val reservasHoy         = repo.reservasHoy(hoy)
    val canchasOcupadas     = repo.canchasOcupadas(hoy)
    val reservasActivas     = repo.reservasActivas()
    val reservasFinalizadas = repo.reservasFinalizadas()
    val proximasReservas    = repo.proximasReservas(hoy)
}
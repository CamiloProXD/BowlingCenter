package com.example.bowlingcenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.bowlingcenter.data.db.ReservaDao
import com.example.bowlingcenter.data.model.Reserva
import com.example.bowlingcenter.repository.ReservaRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class ExampleUnitTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dao  = mockk<ReservaDao>(relaxed = true)
    private val repo = ReservaRepository(dao)

    private val reservaBase = Reserva(
        id                = 0,
        nombreCliente     = "Carlos",
        telefono          = "3001234567",
        fecha             = "12/05/2026",
        hora              = "13:00",
        horaMinutos       = 780,
        numeroPista       = 1,
        cantidadJugadores = 4,
        estado            = "Activa"
    )

    // ── 1. Guardar
    @Test
    fun `guardar reserva llama dao insertar`() = runTest {
        repo.guardar(reservaBase)
        coVerify(exactly = 1) { dao.insertar(reservaBase) }
    }

    // ── 2. Eliminar
    @Test
    fun `eliminar reserva llama dao eliminar`() = runTest {
        repo.eliminar(reservaBase)
        coVerify(exactly = 1) { dao.eliminar(reservaBase) }
    }

    // ── 3. Actualizar
    @Test
    fun `actualizar reserva llama dao actualizar`() = runTest {
        val editada = reservaBase.copy(id = 3, estado = "Finalizada")
        repo.actualizar(editada)
        coVerify(exactly = 1) { dao.actualizar(editada) }
    }

    // ── 4. Conflicto directo (misma hora)
    @Test
    fun `existe conflicto cuando hay reserva en la misma hora`() = runTest {
        coEvery {
            dao.contarConflictos(1, "12/05/2026", 780, 840, 0)
        } returns 1

        assertTrue(repo.existeConflicto(1, "12/05/2026", "13:00"))
    }

    // ── 5. Conflicto media hora después
    @Test
    fun `existe conflicto cuando nueva reserva es 30 min despues`() = runTest {
        coEvery {
            dao.contarConflictos(1, "12/05/2026", 810, 870, 0)
        } returns 1

        assertTrue(repo.existeConflicto(1, "12/05/2026", "13:30"))
    }

    // ── 6. Conflicto media hora antes
    @Test
    fun `existe conflicto cuando nueva reserva es 30 min antes`() = runTest {
        coEvery {
            dao.contarConflictos(1, "12/05/2026", 750, 810, 0)
        } returns 1

        assertTrue(repo.existeConflicto(1, "12/05/2026", "12:30"))
    }

    // ── 7. Sin conflicto (hora libre)
    @Test
    fun `no existe conflicto cuando la hora esta libre`() = runTest {
        coEvery {
            dao.contarConflictos(1, "12/05/2026", 840, 900, 0)
        } returns 0

        assertFalse(repo.existeConflicto(1, "12/05/2026", "14:00"))
    }

    // ── 8. Sin conflicto en pista diferente
    @Test
    fun `no existe conflicto en pista diferente misma hora`() = runTest {
        coEvery {
            dao.contarConflictos(2, "12/05/2026", 780, 840, 0)
        } returns 0

        assertFalse(repo.existeConflicto(2, "12/05/2026", "13:00"))
    }

    // ── 9. Obtener por ID
    @Test
    fun `obtenerPorId retorna la reserva correcta`() = runTest {
        coEvery { dao.obtenerPorId(5) } returns reservaBase.copy(id = 5)

        val resultado = repo.obtenerPorId(5)
        assertEquals(5, resultado?.id)
        assertEquals("Carlos", resultado?.nombreCliente)
    }

    // ── 10. Editar no genera conflicto consigo misma
    @Test
    fun `editar reserva no genera conflicto con ella misma`() = runTest {
        coEvery {
            dao.contarConflictos(1, "12/05/2026", 780, 840, 3)
        } returns 0

        assertFalse(repo.existeConflicto(1, "12/05/2026", "13:00", excludeId = 3))
    }
}
package com.example.bowlingcenter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.bowlingcenter.ui.form.ReservaFormScreen
import com.example.bowlingcenter.ui.home.HomeScreen
import com.example.bowlingcenter.ui.list.ListaReservasScreen

sealed class Screen(val route: String) {
    object Home   : Screen("home")
    object Lista  : Screen("lista")
    object Form   : Screen("form?reservaId={reservaId}") {
        fun createRoute(id: Int = 0) = "form?reservaId=$id"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                onVerLista    = { navController.navigate(Screen.Lista.route) },
                onNuevaReserva = { navController.navigate(Screen.Form.createRoute()) }
            )
        }

        composable(Screen.Lista.route) {
            ListaReservasScreen(
                onBack   = { navController.popBackStack() },
                onEditar = { id -> navController.navigate(Screen.Form.createRoute(id)) }
            )
        }

        composable(
            route = Screen.Form.route,
            arguments = listOf(navArgument("reservaId") {
                type = NavType.IntType; defaultValue = 0
            })
        ) { backStack ->
            val id = backStack.arguments?.getInt("reservaId") ?: 0
            ReservaFormScreen(
                reservaId = id,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
package com.example.chequemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chequemanager.data.Direction
import com.example.chequemanager.ui.screens.AccountListScreen
import com.example.chequemanager.ui.screens.ChequeListScreen
import com.example.chequemanager.ui.screens.DashboardScreen
import com.example.chequemanager.ui.screens.PersonListScreen
import com.example.chequemanager.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    private val vm: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier) {
                    AppNavHost(vm)
                }
            }
        }
    }
}

@Composable
fun AppNavHost(vm: AppViewModel) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen(vm) { route -> navController.navigate(route) }
        }
        composable("persons") { PersonListScreen(vm) }
        composable("accounts") { AccountListScreen(vm) }
        composable("cheques_received") { ChequeListScreen(vm, Direction.RECEIVED) }
        composable("cheques_paid") { ChequeListScreen(vm, Direction.PAID) }
    }
}

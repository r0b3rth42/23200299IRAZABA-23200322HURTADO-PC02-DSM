package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.AppDatabase
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.remote.RetrofitClient
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.repository.ApodRepository
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.repository.AsteroidRepository
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.ui.screen.*
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.ui.theme._23200299IRAZABA23200322HURTADOPC02DSMTheme
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.ui.viewmodel.ApodViewModel
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.ui.viewmodel.ApodViewModelFactory
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.ui.viewmodel.AsteroidViewModel
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.ui.viewmodel.AsteroidViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val apodRepository = ApodRepository(database.apodDao(), RetrofitClient.apodApiService)
        val asteroidRepository = AsteroidRepository(database.asteroidDao(), RetrofitClient.neoWsApiService)

        setContent {
            _23200299IRAZABA23200322HURTADOPC02DSMTheme {
                DiarioEspacialApp(
                    apodRepository = apodRepository,
                    asteroidRepository = asteroidRepository
                )
            }
        }
    }
}

@Composable
fun DiarioEspacialApp(
    apodRepository: ApodRepository,
    asteroidRepository: AsteroidRepository
) {
    val navController = rememberNavController()

    val apodViewModel: ApodViewModel = viewModel(
        factory = ApodViewModelFactory(apodRepository)
    )

    val asteroidViewModel: AsteroidViewModel = viewModel(
        factory = AsteroidViewModelFactory(asteroidRepository)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf("apod_list", "asteroid_list")
    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == "apod_list",
                        onClick = {
                            navController.navigate("apod_list") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Imágenes") },
                        label = { Text("Imágenes") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "asteroid_list",
                        onClick = {
                            navController.navigate("asteroid_list") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Public, contentDescription = "Asteroides") },
                        label = { Text("Asteroides") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "apod_list",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("apod_list") {
                ApodListScreen(
                    viewModel = apodViewModel,
                    onApodClick = { date -> navController.navigate("apod_detail/$date") },
                    onNavigateToFavorites = { navController.navigate("apod_favorites") }
                )
            }
            composable(
                route = "apod_detail/{date}",
                arguments = listOf(navArgument("date") { type = NavType.StringType })
            ) { backStackEntry ->
                val date = backStackEntry.arguments?.getString("date") ?: ""
                ApodDetailScreen(
                    date = date,
                    viewModel = apodViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("apod_favorites") {
                ApodFavoritesScreen(
                    viewModel = apodViewModel,
                    onApodClick = { date -> navController.navigate("apod_detail/$date") },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("asteroid_list") {
                AsteroidListScreen(
                    viewModel = asteroidViewModel,
                    onAsteroidClick = { id -> navController.navigate("asteroid_detail/$id") },
                    onNavigateToFavorites = { navController.navigate("asteroid_favorites") }
                )
            }
            composable(
                route = "asteroid_detail/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                AsteroidDetailScreen(
                    asteroidId = id,
                    viewModel = asteroidViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("asteroid_favorites") {
                AsteroidFavoritesScreen(
                    viewModel = asteroidViewModel,
                    onAsteroidClick = { id -> navController.navigate("asteroid_detail/$id") },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

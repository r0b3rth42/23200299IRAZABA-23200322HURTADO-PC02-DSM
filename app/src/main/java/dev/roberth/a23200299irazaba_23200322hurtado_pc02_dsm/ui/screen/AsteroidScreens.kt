package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.entity.AsteroidEntity
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.ui.viewmodel.AsteroidViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidListScreen(
    viewModel: AsteroidViewModel,
    onAsteroidClick: (String) -> Unit,
    onNavigateToFavorites: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val filteredAsteroids = if (uiState.isHazardousFilterEnabled) {
        uiState.asteroids.filter { it.isPotentiallyHazardous }
    } else {
        uiState.asteroids
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asteroides (NeoWs)") },
                actions = {
                    IconButton(onClick = onNavigateToFavorites) {
                        Icon(Icons.Default.Star, contentDescription = "Mis Asteroides", tint = Color(0xFFFFD700))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Solo potencialmente peligrosos",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Switch(
                    checked = uiState.isHazardousFilterEnabled,
                    onCheckedChange = { viewModel.toggleHazardousFilter(it) }
                )
            }

            HorizontalDivider()

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading && uiState.asteroids.isEmpty() -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    filteredAsteroids.isEmpty() -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (uiState.isHazardousFilterEnabled)
                                    "No hay asteroides potencialmente peligrosos."
                                else "No hay asteroides disponibles.",
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.refresh() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(filteredAsteroids, key = { it.id }) { asteroid ->
                                AsteroidItemCard(
                                    asteroid = asteroid,
                                    onClick = { onAsteroidClick(asteroid.id) },
                                    onToggleFavorite = { viewModel.toggleFavorite(asteroid.id, asteroid.isFavorite) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AsteroidItemCard(
    asteroid: AsteroidEntity,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (asteroid.isPotentiallyHazardous) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = asteroid.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (asteroid.isPotentiallyHazardous) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = Color.Red,
                            shape = MaterialTheme.shapes.extraSmall
                        ) {
                            Text(
                                text = "PELIGROSO",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Aproximación: ${asteroid.closeApproachDate}",
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "Diámetro: ${String.format(Locale.US, "%.2f", asteroid.estimatedDiameterKmMin)} - ${String.format(Locale.US, "%.2f", asteroid.estimatedDiameterKmMax)} km",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (asteroid.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (asteroid.isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidDetailScreen(
    asteroidId: String,
    viewModel: AsteroidViewModel,
    onBack: () -> Unit
) {
    val asteroidState by viewModel.getAsteroidById(asteroidId).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(asteroidState?.name ?: "Detalle del Asteroide") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    asteroidState?.let { asteroid ->
                        IconButton(onClick = { viewModel.toggleFavorite(asteroid.id, asteroid.isFavorite) }) {
                            Icon(
                                imageVector = if (asteroid.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = if (asteroid.isFavorite) Color.Red else Color.Gray
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        val asteroid = asteroidState
        if (asteroid == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (asteroid.isPotentiallyHazardous) Color(0xFFFFCDD2) else MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = asteroid.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(text = "ID: ${asteroid.id}", fontSize = 14.sp, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(8.dp))
                        if (asteroid.isPotentiallyHazardous) {
                            Text(
                                text = "⚠️ ASTEROIDE POTENCIALMENTE PELIGROSO",
                                color = Color(0xFFB71C1C),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        } else {
                            Text(
                                text = "✅ Sin riesgo potencial detectado",
                                color = Color(0xFF1B5E20),
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "Información Detallada", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        HorizontalDivider()
                        DetailRow(label = "Fecha de aproximación", value = asteroid.closeApproachDate)
                        DetailRow(
                            label = "Magnitud absoluta (h)",
                            value = String.format(Locale.US, "%.2f", asteroid.absoluteMagnitude)
                        )
                        DetailRow(
                            label = "Diámetro estimado mín",
                            value = "${String.format(Locale.US, "%.2f", asteroid.estimatedDiameterKmMin)} km"
                        )
                        DetailRow(
                            label = "Diámetro estimado máx",
                            value = "${String.format(Locale.US, "%.2f", asteroid.estimatedDiameterKmMax)} km"
                        )
                        DetailRow(
                            label = "Velocidad relativa",
                            value = "${String.format(Locale.US, "%.2f", asteroid.relativeVelocityKmH)} km/h"
                        )
                        DetailRow(
                            label = "Distancia de fallo",
                            value = "${String.format(Locale.US, "%.2f", asteroid.missDistanceKm)} km"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidFavoritesScreen(
    viewModel: AsteroidViewModel,
    onAsteroidClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Asteroides Favoritos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.favoriteAsteroids.isEmpty()) {
                Text(
                    text = "No tienes asteroides favoritos guardados aún.",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    fontSize = 16.sp
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(uiState.favoriteAsteroids, key = { it.id }) { asteroid ->
                        AsteroidItemCard(
                            asteroid = asteroid,
                            onClick = { onAsteroidClick(asteroid.id) },
                            onToggleFavorite = { viewModel.toggleFavorite(asteroid.id, asteroid.isFavorite) }
                        )
                    }
                }
            }
        }
    }
}

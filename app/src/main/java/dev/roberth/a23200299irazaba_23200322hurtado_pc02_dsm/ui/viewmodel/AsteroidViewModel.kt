package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.entity.AsteroidEntity
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.repository.AsteroidRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class AsteroidUiState(
    val isLoading: Boolean = false,
    val asteroids: List<AsteroidEntity> = emptyList(),
    val favoriteAsteroids: List<AsteroidEntity> = emptyList(),
    val isHazardousFilterEnabled: Boolean = false,
    val errorMessage: String? = null
)

class AsteroidViewModel(
    private val repository: AsteroidRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AsteroidUiState())
    val uiState: StateFlow<AsteroidUiState> = _uiState.asStateFlow()

    init {
        observeAsteroids()
        observeFavorites()
        refresh()
    }

    private fun observeAsteroids() {
        viewModelScope.launch {
            repository.allAsteroids.collectLatest { list ->
                _uiState.value = _uiState.value.copy(asteroids = list)
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.favoriteAsteroids.collectLatest { favList ->
                _uiState.value = _uiState.value.copy(favoriteAsteroids = favList)
            }
        }
    }

    fun toggleHazardousFilter(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isHazardousFilterEnabled = enabled)
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                repository.refreshAsteroids()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Error al cargar asteroides")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun toggleFavorite(id: String, currentFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, currentFavorite)
        }
    }

    fun getAsteroidById(id: String): StateFlow<AsteroidEntity?> {
        val flow = MutableStateFlow<AsteroidEntity?>(null)
        viewModelScope.launch {
            repository.getAsteroidById(id).collect { asteroid ->
                flow.value = asteroid
            }
        }
        return flow
    }
}

class AsteroidViewModelFactory(
    private val repository: AsteroidRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AsteroidViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AsteroidViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

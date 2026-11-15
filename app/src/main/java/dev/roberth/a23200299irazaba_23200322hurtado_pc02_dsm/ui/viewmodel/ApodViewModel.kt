package dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.local.entity.ApodEntity
import dev.roberth.a23200299irazaba_23200322hurtado_pc02_dsm.data.repository.ApodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class ApodUiState(
    val isLoading: Boolean = false,
    val apods: List<ApodEntity> = emptyList(),
    val favoriteApods: List<ApodEntity> = emptyList(),
    val errorMessage: String? = null
)

class ApodViewModel(
    private val repository: ApodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApodUiState())
    val uiState: StateFlow<ApodUiState> = _uiState.asStateFlow()

    init {
        observeApods()
        observeFavorites()
        refresh()
    }

    private fun observeApods() {
        viewModelScope.launch {
            repository.allApods.collectLatest { list ->
                _uiState.value = _uiState.value.copy(apods = list)
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            repository.favoriteApods.collectLatest { favList ->
                _uiState.value = _uiState.value.copy(favoriteApods = favList)
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                repository.refreshApods()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Error al cargar datos")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun toggleFavorite(date: String, currentFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(date, currentFavorite)
        }
    }

    fun getApodByDate(date: String): StateFlow<ApodEntity?> {
        val flow = MutableStateFlow<ApodEntity?>(null)
        viewModelScope.launch {
            repository.getApodByDate(date).collect { apod ->
                flow.value = apod
            }
        }
        return flow
    }
}

class ApodViewModelFactory(
    private val repository: ApodRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ApodViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

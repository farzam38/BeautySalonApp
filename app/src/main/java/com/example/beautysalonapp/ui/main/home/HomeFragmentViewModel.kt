package com.example.beautysalonapp.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautysalonapp.model.repositories.SalonRepository
import com.example.beautysalonapp.ui.Salon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeFragmentViewModel : ViewModel() {
    val salonRepository = SalonRepository()

    val failureMessage = MutableStateFlow<String?>(null)
    val data = MutableStateFlow<List<Salon>?>(null)

    init {
        readSalons()
    }

    fun readSalons() {
        viewModelScope.launch {
            salonRepository.getSalons().catch {
                failureMessage.value = it.message
            }
                .collect {
                    data.value = it
                }
        }
    }
}
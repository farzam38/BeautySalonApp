package com.example.beautysalonapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautysalonapp.model.repositories.ServiceRepository
import com.example.beautysalonapp.ui.Service
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ServiceFragmentViewModel : ViewModel() {
    val serviceRepository = ServiceRepository()

    val failureMessage = MutableStateFlow<String?>(null)
    val data = MutableStateFlow<List<Service>?>(null)

    init {
        readHotels()
    }

    fun readHotels() {
        viewModelScope.launch {
            serviceRepository.getBooking().catch {
                failureMessage.value = it.message
            }
                .collect {
                    data.value = it
                }
        }
    }
}
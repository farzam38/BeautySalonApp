package com.example.beautysalonapp.ui.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautysalonapp.model.repositories.ServiceRepository
import com.example.beautysalonapp.ui.Service
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ServiceDetailsViewModel:ViewModel() {
    val serviceRepository=ServiceRepository()

    val isUpdated= MutableStateFlow<Boolean?>(null)
    val failureMessage= MutableStateFlow<String?>(null)
    val isDateSaved = MutableStateFlow<Boolean?>(null)

    fun saveSelectedDate(bookingId: String, selectedDate: String) {
        viewModelScope.launch {
            val result = serviceRepository.saveSelectedDate(bookingId, selectedDate)
            if (result.isSuccess) {
                isDateSaved.value = true
            } else {
                failureMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

    public fun updateService(service:Service){
        viewModelScope.launch {
            val result=serviceRepository.updateService(service)
            if (result.isSuccess)
                isUpdated.value=true
            else
                failureMessage.value=result.exceptionOrNull()?.message
        }
    }
}
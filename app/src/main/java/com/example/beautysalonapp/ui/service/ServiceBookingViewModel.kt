package com.example.beautysalonapp.ui.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautysalonapp.model.repositories.AuthRepository
import com.example.beautysalonapp.model.repositories.ServiceRepository
import com.example.beautysalonapp.ui.Service
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ServiceBookingViewModel:ViewModel() {
    val serviceRepository= ServiceRepository()
    val authRepository=AuthRepository()

    val isSaving= MutableStateFlow<Boolean>(false)
    val isSaved= MutableStateFlow<Boolean?>(null)
    val failureMessage= MutableStateFlow<String?>(null)

    fun getCurrentUser() = authRepository.getCurrentUser()

    fun saveService(service:Service){
        viewModelScope.launch {
            val result=serviceRepository.saveService(service)
            isSaving.value=false
            if (result.isSuccess){
                isSaved.value=true
            }else{
                failureMessage.value=result.exceptionOrNull()?.message
            }
        }
    }


}
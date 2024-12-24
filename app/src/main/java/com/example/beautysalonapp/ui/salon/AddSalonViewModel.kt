package com.example.beautysalonapp.ui.salon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautysalonapp.model.repositories.SalonRepository
import com.example.beautysalonapp.model.repositories.StorageRepository
import com.example.beautysalonapp.ui.Salon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AddSalonViewModel:ViewModel() {
    val salonRepository = SalonRepository()
    val storageRepository = StorageRepository()

    val isSuccessfullySaved = MutableStateFlow<Boolean?>(null)
    val failureMessage = MutableStateFlow<String?>(null)

    fun uploadImageAndSaveSalon(imagePath: String, salon: Salon) {
        storageRepository.uploadFile(imagePath, onComplete = { success, result ->
            if (success) {
                salon.image=result!!
                saveSalons(salon)
            }
            else failureMessage.value=result
        })
    }
    fun saveSalons(salon: Salon) {
        viewModelScope.launch {
            val result = salonRepository.saveSalons(salon)
            if (result.isSuccess) {
                isSuccessfullySaved.value = true
            } else {
                failureMessage.value = result.exceptionOrNull()?.message
            }
        }
    }

}
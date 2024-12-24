package com.example.beautysalonapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautysalonapp.model.repositories.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewModel :ViewModel(){
    val authRepository=AuthRepository()

    val currentUser= MutableStateFlow<FirebaseUser?>(null)
    val failureMessage= MutableStateFlow<String?>(null)
    val resetResponse= MutableStateFlow<Boolean?>(null)

    fun login(email:String,password:String){
        viewModelScope.launch {
            val result=authRepository.login(email,password)
            if (result.isSuccess){
                currentUser.value=result.getOrThrow()
            }else{
                failureMessage.value=result.exceptionOrNull()?.message
            }
        }
    }
    fun resetPassword(email:String){
        viewModelScope.launch {
            val result=authRepository.resetPassword(email)
            if (result.isSuccess){
                resetResponse.value=result.getOrThrow()
            }else{
                failureMessage.value=result.exceptionOrNull()?.message
            }
        }
    }
    fun signUp(email:String,password:String,name:String){
        viewModelScope.launch {
            val result=authRepository.signup(email,password,name)
            if (result.isSuccess){
                currentUser.value=result.getOrThrow()
            }else{
                failureMessage.value=result.exceptionOrNull()?.message
            }
        }
    }

    fun checkUser(){
        currentUser.value=authRepository.getCurrentUser()
    }
}
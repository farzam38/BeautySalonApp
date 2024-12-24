package com.example.beautysalonapp.model.repositories

import com.example.beautysalonapp.ui.Service
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class ServiceRepository {
    val bookingCollection = FirebaseFirestore.getInstance().collection("services")

    suspend fun saveService(service: Service): Result<Boolean> {
        try {
            val document = FirebaseFirestore.getInstance().collection("services").document()
            service.id = document.id
            document.set(service).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
    suspend fun updateService(service: Service): Result<Boolean> {
        try {
            val document = bookingCollection.document(service.id)
            document.set(service).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun getBooking() =
        bookingCollection.snapshots().map { it.toObjects(Service::class.java) }

}
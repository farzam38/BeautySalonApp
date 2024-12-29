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

    suspend fun saveSelectedDate(bookingId: String, selectedDate: String): Result<Boolean> {
        return try {
            val document = bookingCollection.document(bookingId)
            val updates = mapOf("selectedDate" to selectedDate)
            document.update(updates).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
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

    suspend fun getBooking(): Result<List<Service>?> {
        try {
            val querySnapshot = bookingCollection.get().await()
            val services = querySnapshot.documents.mapNotNull { document ->
                document.toObject(Service::class.java)
            }
            return Result.success(services)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }


}
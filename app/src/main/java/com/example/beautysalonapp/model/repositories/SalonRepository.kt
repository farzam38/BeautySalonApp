package com.example.beautysalonapp.model.repositories

import com.example.beautysalonapp.ui.Salon
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class SalonRepository {
    val salonlist = FirebaseFirestore.getInstance().collection("salons")


    suspend fun saveSalons(salons: Salon): Result<Boolean> {
        try {
            val document = salonlist.document()
            salons.id = document.id
            document.set(salons).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun getSalons() =
        salonlist.snapshots().map { it.toObjects(Salon::class.java) }

}
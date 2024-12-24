package com.example.beautysalonapp.model.repositories

import android.content.Context
import com.example.beautysalonapp.model.dataSource.FCMHelper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NotificationsRepository {
    val notificationCollection = FirebaseFirestore.getInstance().collection("notification")

    suspend fun saveToken(userId: String, token: String): Result<Boolean> {
        try {
            notificationCollection.document(userId).set(mapOf("token" to token)).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    fun getToken(userId: String) =
        notificationCollection.document(userId).get()

    fun sendNotification(uid: String, title: String, message: String, context: Context) {
        getToken(uid).addOnSuccessListener {
            if (it != null)
                FCMHelper().sendNotificationToUser(
                    it.getString("token")!!,
                    title,
                    message,
                    context
                )
        }
    }
}
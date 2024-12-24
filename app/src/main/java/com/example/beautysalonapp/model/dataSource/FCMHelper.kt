package com.example.beautysalonapp.model.dataSource

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.beautysalonapp.R
import com.google.auth.oauth2.GoogleCredentials
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.Executors

class FCMHelper {
    fun sendNotificationToUser(token: String, title: String, message: String, context: Context) {
        getAccessToken(context, object : AccessTokenCallback {
            override fun onSuccess(accessToken: String) {
                Log.d("notificationstest", "AccessToken: $accessToken")

                // Prepare the payload for the FCM v1 API
                val json = JSONObject().apply {
                    val messageObj = JSONObject().apply {
                        put("token", token) // Send notification to specific user

                        // Notification object
                        put("notification", JSONObject().apply {
                            put("title", title)
                            put("body", message)
                        })

                        // Optional: Android configuration for priority
                        put("android", JSONObject().apply {
                            put("priority", "high")
                        })
                    }
                    put("message", messageObj)
                }

                // Define the FCM v1 API endpoint
                val url = "https://fcm.googleapis.com/v1/projects/beautysalon-86bae/messages:send"
                val requestQueue = Volley.newRequestQueue(context)

                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.POST, url, json,
                    { response ->
                        Log.d("notificationstest", "Response: $response")
                    },
                    { error ->
                        Log.e("notificationstest", "Error: ${error.message}")
                        error.networkResponse?.let {
                            Log.e("notificationstest", "Status Code: ${it.statusCode}")
                            Log.e("notificationstest", "Response Data: ${String(it.data)}")
                        }
                    }
                ) {
                    override fun getHeaders(): MutableMap<String, String> {
                        return mutableMapOf(
                            "Content-Type" to "application/json",
                            "Authorization" to "Bearer $accessToken"
                        )
                    }
                }

                requestQueue.add(jsonObjectRequest)
            }

            override fun onError() {
                Log.d("notificationstest", "Failed to get access token.")
            }
        })
    }

    // Helper function to get access token
    private fun getAccessToken(context: Context, callback: AccessTokenCallback) {
        Executors.newSingleThreadExecutor().execute {
            try {
                val googleCredentials = GoogleCredentials
                    .fromStream(context.resources.openRawResource(R.raw.services))
                    .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
                googleCredentials.refreshIfExpired()
                val token = googleCredentials.accessToken.tokenValue

                Handler(Looper.getMainLooper()).post {
                    callback.onSuccess(token)
                }
            } catch (e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, "IO exception", Toast.LENGTH_SHORT).show()
                    callback.onError()
                }
            }
        }
    }

    // Callback interface
    interface AccessTokenCallback {
        fun onSuccess(token: String)
        fun onError()
    }

}
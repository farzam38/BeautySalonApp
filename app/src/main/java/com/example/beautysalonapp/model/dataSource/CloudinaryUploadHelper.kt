package com.example.beautysalonapp.model.dataSource

import android.content.Context
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

class CloudinaryUploadHelper() {
    companion object{
        fun initializeCloudinary(context: Context) {
            val config = mapOf(
                "cloud_name" to "dexdorod2",  // Replace with your Cloudinary cloud name
                "api_key" to "689478381813878",       // Replace with your Cloudinary API key
                "api_secret" to "dQL98uILMf43smb8rpf716rxwNg" // Replace with your Cloudinary API secret
            )
            MediaManager.init(context, config)
        }
    }

    fun uploadFile(
        filePath: String,
        onComplete: (Boolean,String?) -> Unit
    ) {
        MediaManager.get().upload(filePath)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    Log.d("Cloudinary", "Upload started: $requestId")
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    val progress = (bytes.toDouble() / totalBytes * 100).toInt()
                    Log.d("Cloudinary", "Upload progress: $progress%")
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val fileUrl = (resultData["url"] as? String)?.replace("http://", "https://")
                    if (fileUrl != null) {
                        Log.d("Cloudinary", "Upload successful. URL: $fileUrl")
                        onComplete(true,fileUrl)
                    }
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Log.e("Cloudinary", "Upload failed")
                    onComplete(false,error?.description)
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    Log.e("Cloudinary", "Upload rescheduled")
                }

            }).dispatch()
    }



}
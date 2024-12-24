package com.example.beautysalonapp.model.repositories

import com.example.beautysalonapp.model.dataSource.CloudinaryUploadHelper

class StorageRepository {

    fun uploadFile(filePath:String,onComplete: (Boolean,String?) -> Unit){
        CloudinaryUploadHelper().uploadFile(filePath,onComplete)
    }

}
package com.anxops.bkn.data.network

class ImageUploadRequest(
    val userId: String,
    val data: ByteArray
)

interface ImageUploader {
    suspend fun uploadImage(
        request: ImageUploadRequest,
        onProgressUpdate: (Float) -> Unit = {},
        onSuccess: (String) -> Unit = {},
        onFailure: (e: Exception?) -> Unit = {}
    )
}

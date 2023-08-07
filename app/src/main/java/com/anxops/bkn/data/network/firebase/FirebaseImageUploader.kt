package com.anxops.bkn.data.network.firebase

import com.anxops.bkn.data.network.ImageUploadRequest
import com.anxops.bkn.data.network.ImageUploader
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import timber.log.Timber
import java.util.UUID


class FirebaseImageUploader : ImageUploader {
    override suspend fun uploadImage(
        request: ImageUploadRequest,
        onProgressUpdate: (Float) -> Unit,
        onSuccess: (String) -> Unit,
        onFailure: (e: Exception?) -> Unit
    ) {
        try {
            val imageId = UUID.randomUUID().toString() + ".jpg"
            val storageRef = FirebaseStorage.getInstance().reference
            val fileRef = storageRef.child(request.userId).child(imageId)
            val uploadTask = fileRef.putBytes(request.data)

            uploadTask.addOnProgressListener { (bytesTransferred, totalByteCount) ->
                val progress = (100.0 * bytesTransferred) / totalByteCount
                onProgressUpdate(progress.toFloat())
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fileRef.downloadUrl.addOnCompleteListener { urlTask ->
                        val url = urlTask.result
                        if (urlTask.isSuccessful && url != null) {
                            onSuccess(url.toString())
                        } else {
                            onFailure(task.exception)
                        }
                    }
                } else {
                    onFailure(task.exception)
                }
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            onFailure(ex)
        }
    }
}
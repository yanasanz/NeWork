package com.example.nework.model

import android.net.Uri
import com.example.nework.enumeration.AttachmentType
import java.io.File

data class MediaModel(
    val uri: Uri? = null,
    val file: File? = null,
    val type: AttachmentType? = null
)
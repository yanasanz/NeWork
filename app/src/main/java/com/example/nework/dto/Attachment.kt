package com.example.nework.dto

import com.example.nework.enumeration.AttachmentType

data class Attachment(
    val url: String,
    val type: AttachmentType,
)

package com.example.nework.dto

data class PostCreateRequest(
    val id: Int,
    val content: String,
    val coords: Coordinates?,
    val link: String?,
    val attachment: Attachment?,
    val mentionIds: List<Int>,
)

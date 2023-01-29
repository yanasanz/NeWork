package com.example.nework.dto

data class UserPreview(
    val id: Int = 0,
    val name: String,
    val avatar: String?,
    var isLiked: Boolean = false,
    var isMentioned: Boolean = false,
    var isParticipating: Boolean = false,
    var isSpeaker: Boolean = false,
)

package com.example.nework.dto

data class UserResponse(
    val id: Int = 0,
    val login: String = "",
    val name: String = "",
    val avatar: String? = null,
    var isChecked: Boolean = false
)

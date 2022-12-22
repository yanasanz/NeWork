package com.example.nework.dto

import okhttp3.MultipartBody

data class RegistrationRequest(
    val login: String,
    val password: String,
    val name: String,
    val file: MultipartBody.Part?
)

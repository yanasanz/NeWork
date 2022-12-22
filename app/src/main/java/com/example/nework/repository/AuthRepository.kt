package com.example.nework.repository

import com.example.nework.auth.AuthState
import com.example.nework.dto.MediaUpload

interface AuthRepository {
    suspend fun signIn(login: String, pass: String): AuthState
    suspend fun register(login: String, pass: String, name: String): AuthState
    suspend fun registerWithPhoto(
        login: String,
        pass: String,
        name: String,
        media: MediaUpload
    ): AuthState
}
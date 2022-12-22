package com.example.nework.repository

import com.example.nework.api.ApiService
import com.example.nework.auth.AuthState
import com.example.nework.dto.MediaUpload
import com.example.nework.error.ApiError
import com.example.nework.error.NetworkError
import com.example.nework.error.UnknownError
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: ApiService
) : AuthRepository {

    override suspend fun signIn(login: String, pass: String): AuthState {
        try {
            val response = api.signIn(login, pass)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            println(e.message)
            throw NetworkError
        }
    }

    override suspend fun register(login: String, pass: String, name: String): AuthState {
        try {
            val response = api.register(login, pass, name)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun registerWithPhoto(
        login: String,
        pass: String,
        name: String,
        media: MediaUpload
    ): AuthState {
        try {
            val file = MultipartBody.Part.createFormData(
                "file", media.file.name, media.file.asRequestBody()
            )
            val response = api.registerWithPhoto(
                login.toRequestBody("text/plain".toMediaType()),
                pass.toRequestBody("text/plain".toMediaType()),
                name.toRequestBody("text/plain".toMediaType()),
                file
            )
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}
package com.example.nework.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.nework.api.ApiService
import com.example.nework.dao.UserDao
import com.example.nework.dto.UserResponse
import com.example.nework.entity.UserEntity
import com.example.nework.entity.toDto
import com.example.nework.entity.toEntity
import com.example.nework.error.ApiError
import com.example.nework.error.NetworkError
import java.io.IOException
import javax.inject.Inject

val emptyUser = UserResponse()

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) : UserRepository {

    override val data: MutableLiveData<List<UserResponse>> = userDao.getAllUsers().map(List<UserEntity>::toDto)  as MutableLiveData<List<UserResponse>>
    override val userData: MutableLiveData<UserResponse> = MutableLiveData(emptyUser)

    override suspend fun getAllUsers(){
        try {
            val response = apiService.getAllUsers()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            data.postValue(body)
            userDao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun getUserById(id: Int){
        try {
            val response = apiService.getUserById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            userData.postValue(body)
        } catch (e: IOException) {
            throw NetworkError
        }
    }
}
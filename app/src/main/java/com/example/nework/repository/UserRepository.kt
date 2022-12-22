package com.example.nework.repository

import androidx.lifecycle.MutableLiveData
import com.example.nework.dto.UserResponse

interface UserRepository {
    val data: MutableLiveData<List<UserResponse>>
    val userData: MutableLiveData<UserResponse>
    suspend fun getAllUsers()
    suspend fun getUserById(id: Int)
}
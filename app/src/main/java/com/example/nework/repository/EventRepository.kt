package com.example.nework.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.nework.dto.*
import com.example.nework.enumeration.AttachmentType
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface EventRepository {
    val data: Flow<PagingData<EventResponse>>
    val eventUsersData: MutableLiveData<List<UserPreview>>
    suspend fun getEventUsersList(event: EventResponse)
    suspend fun removeEventById(id: Int)
    suspend fun likeEventById(id: Int): EventResponse
    suspend fun dislikeEventById(id: Int): EventResponse
    suspend fun participateInEvent(id: Int): EventResponse
    suspend fun quitParticipateInEvent(id: Int): EventResponse
    suspend fun getEventById(id: Int): EventResponse
    suspend fun getUsers(): List<UserResponse>
    suspend fun addMediaToEvent(type: AttachmentType, file: MultipartBody.Part): Attachment
    suspend fun saveEvent(event: EventCreateRequest)
    suspend fun getEventCreateRequest(id: Int): EventCreateRequest
    suspend fun getUserById(id: Int): UserResponse
}
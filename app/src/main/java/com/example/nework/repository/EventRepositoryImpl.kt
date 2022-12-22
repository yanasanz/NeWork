package com.example.nework.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.example.nework.api.ApiService
import com.example.nework.dao.EventDao
import com.example.nework.dto.*
import com.example.nework.entity.EventEntity
import com.example.nework.enumeration.AttachmentType
import com.example.nework.error.ApiError
import com.example.nework.error.NetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class EventRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    mediator: EventRemoteMediator,
    private val dao: EventDao
) : EventRepository {
    override val data: Flow<PagingData<EventResponse>> =
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { dao.getAllEvents() },
            remoteMediator = mediator
        ).flow.map {
            it.map(EventEntity::toDto)
        }
    override val eventUsersData: MutableLiveData<List<UserPreview>> =
        MutableLiveData(emptyList())

    override suspend fun getEventUsersList(event: EventResponse) {
        try {
            val response = apiService.getEventById(event.id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val usersList = response.body()?.users?.values?.toMutableList()!!
            eventUsersData.postValue(usersList)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun removeEventById(id: Int) {
        try {
            val response = apiService.removeEventById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            dao.removeEventById(id)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun likeEventById(id: Int): EventResponse {
        try {
            val response = apiService.likeEventById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
            return body
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun dislikeEventById(id: Int): EventResponse {
        try {
            val response = apiService.dislikeEventById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
            return body
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun participateInEvent(id: Int): EventResponse {
        try {
            val response = apiService.participateInEvent(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
            return body
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun quitParticipateInEvent(id: Int): EventResponse {
        try {
            val response = apiService.quitParticipateInEvent(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
            return body
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun getEventById(id: Int): EventResponse {
        try {
            val response = apiService.getEventById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun getUsers(): List<UserResponse> {
        val usersList: List<UserResponse>
        try {
            val response = apiService.getAllUsers()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            usersList = response.body() ?: throw ApiError(response.code(), response.message())
            return usersList
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun addMediaToEvent(
        type: AttachmentType,
        file: MultipartBody.Part
    ): Attachment {
        try {
            val response = apiService.addMultimedia(file)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val mediaResponse =
                response.body() ?: throw ApiError(response.code(), response.message())
            return Attachment(mediaResponse.url, type)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun saveEvent(event: EventCreateRequest) {
        try {
            val response = apiService.saveEvent(event)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            } else {
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                dao.insert(EventEntity.fromDto(body))
            }
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun getEventCreateRequest(id: Int): EventCreateRequest {
        try {
            val response = apiService.getEventById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            } else {
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                return EventCreateRequest(
                    id = body.id,
                    content = body.content,
                    datetime = body.datetime,
                    coords = body.coords,
                    type = body.type,
                    attachment = body.attachment,
                    link = body.link,
                    speakerIds = body.speakerIds
                )
            }
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun getUserById(id: Int): UserResponse {
        try {
            val response = apiService.getUserById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            } else {
                return response.body() ?: throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        }
    }
}
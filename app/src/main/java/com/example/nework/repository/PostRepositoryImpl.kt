package com.example.nework.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.example.nework.api.ApiService
import com.example.nework.dao.PostDao
import com.example.nework.dto.*
import com.example.nework.entity.PostEntity
import com.example.nework.enumeration.AttachmentType
import com.example.nework.error.ApiError
import com.example.nework.error.NetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    mediator: PostRemoteMediator,
    private val dao: PostDao
) : PostRepository {

    override var data: Flow<PagingData<PostResponse>> =
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { dao.getAllPosts() },
            remoteMediator = mediator
        ).flow.map {
            it.map(PostEntity::toDto)
        }

    override val postUsersData: MutableLiveData<List<UserPreview>> = MutableLiveData(emptyList())

    override suspend fun getLikedAndMentionedUsersList(post: PostResponse) {
        try {
            val response = apiService.getPostById(post.id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val usersList = response.body()?.users!!
            for ((key, value) in usersList) {
                if (post.likeOwnerIds.contains(key)) value.isLiked = true
                if (post.mentionIds.contains(key)) value.isMentioned = true
            }
            val list = usersList.values.toMutableList()
            postUsersData.postValue(list)
        } catch (e: IOException) {
            throw NetworkError
        }
    }


    override suspend fun removePostById(id: Int) {
        try {
            val response = apiService.removePostById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            dao.removeById(id)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun likePostById(id: Int): PostResponse {
        try {
            val response = apiService.likePostById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
            return body
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun dislikePostById(id: Int): PostResponse {
        try {
            val response = apiService.dislikePostById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
            return body
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun getPostById(id: Int): PostResponse {
        try {
            val response = apiService.getPostById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun addMediaToPost(
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

    override suspend fun getUsers(): List<UserResponse> {
        try {
            val response = apiService.getAllUsers()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun savePost(post: PostCreateRequest) {
        try {
            val response = apiService.savePost(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            } else {
                val body =
                    response.body() ?: throw ApiError(response.code(), response.message())
                dao.insert(PostEntity.fromDto(body))
            }
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun getPostCreateRequest(id: Int): PostCreateRequest {
        try {
            val response = apiService.getPostById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            } else {
                val body =
                    response.body() ?: throw ApiError(response.code(), response.message())
                return PostCreateRequest(
                    id = body.id,
                    content = body.content,
                    coords = body.coords,
                    link = body.link,
                    attachment = body.attachment,
                    mentionIds = body.mentionIds
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
package com.example.nework.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.nework.dto.*
import com.example.nework.enumeration.AttachmentType
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface PostRepository {
    val data: Flow<PagingData<PostResponse>>
    val postUsersData: MutableLiveData<List<UserPreview>>
    suspend fun getLikedAndMentionedUsersList(post: PostResponse)
    suspend fun removePostById(id: Int)
    suspend fun likePostById(id: Int): PostResponse
    suspend fun dislikePostById(id: Int): PostResponse
    suspend fun getPostById(id: Int): PostResponse
    suspend fun getUsers(): List<UserResponse>
    suspend fun getPostCreateRequest(id: Int): PostCreateRequest
    suspend fun getUserById(id: Int): UserResponse
    fun getUserPosts(data: Flow<PagingData<PostResponse>>, id: Int)
    suspend fun addMediaToPost(type: AttachmentType, file: MultipartBody.Part): Attachment
    suspend fun savePost(post: PostCreateRequest)
}
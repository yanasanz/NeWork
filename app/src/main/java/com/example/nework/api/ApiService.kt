package com.example.nework.api

import com.example.nework.auth.AuthState
import com.example.nework.dto.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun signIn(
        @Field("login") login: String,
        @Field("password") pass: String
    ): Response<AuthState>

    @FormUrlEncoded
    @POST("users/registration")
    suspend fun register(
        @Field("login") login: String,
        @Field("password") pass: String,
        @Field("name") name: String
    ): Response<AuthState>

    @Multipart
    @POST("users/registration")
    suspend fun registerWithPhoto(
        @Part("login") login: RequestBody,
        @Part("password") pass: RequestBody,
        @Part("name") name: RequestBody,
        @Field("file") file: MultipartBody.Part?
    ): Response<AuthState>

    @Multipart
    @POST("media")
    suspend fun addMultimedia(@Part file: MultipartBody.Part?): Response<MediaResponse>

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: Int
    ): Response<UserResponse>

    @GET("users")
    suspend fun getAllUsers(): Response<List<UserResponse>>

    @POST("posts")
    suspend fun savePost(@Body post: PostCreateRequest): Response<PostResponse>

    @DELETE("posts/{id}")
    suspend fun removePostById(@Path("id") id: Int): Response<Unit>

    @POST("posts/{id}/likes")
    suspend fun likePostById(@Path("id") id: Int): Response<PostResponse>

    @DELETE("posts/{id}/likes")
    suspend fun dislikePostById(@Path("id") id: Int): Response<PostResponse>

    @GET("posts/{post_id}")
    suspend fun getPostById(@Path("post_id") id: Int): Response<PostResponse>

    @GET("posts/latest")
    suspend fun getLatestPosts(@Query("count") count: Int): Response<List<PostResponse>>

    @GET("posts/{id}/before")
    suspend fun getBeforePosts(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<PostResponse>>

    @GET("posts/{id}/after")
    suspend fun getAfterPosts(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<PostResponse>>

    @GET("posts")
    suspend fun getAllPosts(): Response<List<PostResponse>>

    @GET("{user_id}/jobs")
    suspend fun getUserJobs(
        @Path("user_id") id: Int
    ): Response<List<Job>>

    @POST("my/jobs")
    suspend fun saveJob(@Body job: Job): Response<Job>

    @DELETE("my/jobs/{job_id}")
    suspend fun removeJobById(@Path("job_id") id: Int): Response<Unit>

    @GET("my/jobs")
    suspend fun getMyJobs(): Response<List<Job>>

    @GET("events/latest")
    suspend fun getLatestEvents(@Query("count") count: Int): Response<List<EventResponse>>

    @GET("events/{id}/before")
    suspend fun getBeforeEvents(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<EventResponse>>

    @GET("events/{id}/after")
    suspend fun getAfterEvents(
        @Path("id") id: Long,
        @Query("count") count: Int
    ): Response<List<EventResponse>>

    @DELETE("events/{id}")
    suspend fun removeEventById(@Path("id") id: Int): Response<Unit>

    @POST("events/{id}/likes")
    suspend fun likeEventById(@Path("id") id: Int): Response<EventResponse>

    @DELETE("events/{id}/likes")
    suspend fun dislikeEventById(@Path("id") id: Int): Response<EventResponse>

    @POST("events/{id}/participants")
    suspend fun participateInEvent(@Path("id") id: Int): Response<EventResponse>

    @DELETE("events/{id}/participants")
    suspend fun quitParticipateInEvent(@Path("id") id: Int): Response<EventResponse>

    @POST("events")
    suspend fun saveEvent(@Body post: EventCreateRequest): Response<EventResponse>

    @GET("events/{event_id}")
    suspend fun getEventById(@Path("event_id") id: Int): Response<EventResponse>
}
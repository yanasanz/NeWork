package com.example.nework.repository

import androidx.lifecycle.MutableLiveData
import com.example.nework.api.ApiService
import com.example.nework.dao.JobDao
import com.example.nework.dto.Job
import com.example.nework.entity.JobEntity
import com.example.nework.entity.toEntity
import com.example.nework.error.ApiError
import com.example.nework.error.NetworkError
import java.io.IOException
import javax.inject.Inject

val emptyJobList = emptyList<Job>()

class JobRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val jobDao: JobDao
): JobRepository {

    override val data: MutableLiveData<List<Job>> = MutableLiveData(emptyJobList)

    override suspend fun getUserJobs(id: Int) {
        try {
            val response = apiService.getUserJobs(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            data.postValue(response.body())
            jobDao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun saveJob(job: Job) {
        try {
            val response = apiService.saveJob(job)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            jobDao.insert(JobEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun removeJobById(id: Int) {
        try {
            val response = apiService.removeJobById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            jobDao.removeJobById(id)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun getMyJobs() {
        try {
            val response = apiService.getMyJobs()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            data.postValue(body)
            jobDao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        }
    }
}
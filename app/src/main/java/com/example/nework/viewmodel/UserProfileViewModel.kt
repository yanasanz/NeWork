package com.example.nework.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.nework.auth.AppAuth
import com.example.nework.dto.Job
import com.example.nework.dto.PostResponse
import com.example.nework.dto.UserResponse
import com.example.nework.model.FeedModelState
import com.example.nework.repository.JobRepository
import com.example.nework.repository.PostRepository
import com.example.nework.repository.UserRepository
import com.example.nework.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

val editedJob = Job(
    id = 0,
    name = "",
    position = "",
    start = "",
    finish = null,
    link = null
)

@ExperimentalCoroutinesApi
@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val jobRepository: JobRepository,
    private val postRepository: PostRepository,
    appAuth: AppAuth
) : ViewModel() {

    val myId: Long = appAuth.authStateFlow.value.id

    var userId: MutableLiveData<Int?> = MutableLiveData()

    val newJob: MutableLiveData<Job> = MutableLiveData(editedJob)

    private val _jobCreated = SingleLiveEvent<Unit>()
    val jobCreated: LiveData<Unit>
        get() = _jobCreated

    val data: MutableLiveData<List<UserResponse>> = userRepository.data
    val userData: MutableLiveData<UserResponse> = userRepository.userData
    val jobData: MutableLiveData<List<Job>> = jobRepository.data
    val postData: Flow<PagingData<PostResponse>> = postRepository.data

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    fun getAllUsers() {
        viewModelScope.launch {
            try {
                userRepository.getAllUsers()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun getUserById(id: Int) {
        viewModelScope.launch {
            try {
                userRepository.getUserById(id)
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun getUserJobs(id: Int) {
        viewModelScope.launch {
            try {
                jobRepository.getUserJobs(id)
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun saveJob(job: Job) {
        viewModelScope.launch {
            try {
                jobRepository.saveJob(job)
                _dataState.value = FeedModelState(error = false)
                deleteEditJob()
                _jobCreated.value = Unit
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun removeJobById(id: Int) {
        viewModelScope.launch {
            try {
                jobRepository.removeJobById(id)
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun getMyJobs() {
        viewModelScope.launch {
            try {
                jobRepository.getMyJobs()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun deleteEditJob() {
        newJob.postValue(editedJob)
    }

    fun addStartDate(date: String) {
        newJob.value = newJob.value?.copy(start = date)
    }

    fun addEndDate(date: String) {
        newJob.value = newJob.value?.copy(finish = date)
    }
}
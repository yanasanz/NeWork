package com.example.nework.viewmodel

import androidx.lifecycle.*
import com.example.nework.auth.AppAuth
import com.example.nework.auth.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val auth: AppAuth) : ViewModel() {
    val data: LiveData<AuthState> = auth.authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() = auth.authStateFlow.value.id != 0L
}
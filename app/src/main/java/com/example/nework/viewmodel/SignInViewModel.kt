package com.example.nework.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nework.auth.AppAuth
import com.example.nework.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val auth: AppAuth
) : ViewModel() {

    fun signIn(login: String, password: String) = viewModelScope.launch {
        val response = repository.signIn(login, password)
        response.token?.let {
            auth.setAuth(response.id, response.token, response.avatar, response.name)
        }
    }
}
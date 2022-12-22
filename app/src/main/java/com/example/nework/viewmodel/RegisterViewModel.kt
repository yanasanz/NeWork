package com.example.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nework.auth.AppAuth
import com.example.nework.dto.MediaUpload
import com.example.nework.enumeration.AttachmentType
import com.example.nework.model.MediaModel
import com.example.nework.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

private val noAvatar = MediaModel()

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val auth: AppAuth
) : ViewModel() {

    private val _avatar = MutableLiveData(noAvatar)
    val avatar: LiveData<MediaModel>
        get() = _avatar

    fun register(login: String, pass: String, name: String) = viewModelScope.launch {
        val response = repository.register(login, pass, name)
        response.token?.let {
            auth.setAuth(response.id, response.token, response.avatar, response.name)
        }
    }

    fun registerWithPhoto(login: String, pass: String, name: String, media: MediaUpload) =
        viewModelScope.launch {
            val response = repository.registerWithPhoto(login, pass, name, media)
            response.token?.let {
                auth.setAuth(response.id, response.token, response.avatar, response.name)
            }
        }

    fun changeAvatar(uri: Uri?, file: File?) {
        _avatar.value = MediaModel(uri, file, AttachmentType.IMAGE)
    }
}
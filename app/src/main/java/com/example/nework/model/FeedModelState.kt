package com.example.nework.model

data class FeedModelState(
    val loading: Boolean = false,
    val refreshing: Boolean = true,
    val error: Boolean = false,
    val systemError: Boolean = false
)
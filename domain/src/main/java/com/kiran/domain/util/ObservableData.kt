package com.kiran.domain.util

sealed class ObservableData<out R> {
    data class Success<out T>(val data: T) : ObservableData<T>()
    data class Error(val exception: Exception) : ObservableData<Nothing>()
    object Loading : ObservableData<Nothing>()
}
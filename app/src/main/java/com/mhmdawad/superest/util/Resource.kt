package com.mhmdawad.superest.util

sealed class Resource<T>(
    val data: T? = null,
    val msg: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(msg: String, data: T? = null) : Resource<T>(data, msg)
    class Loading<T> : Resource<T>()
    class Idle<T> : Resource<T>()
}
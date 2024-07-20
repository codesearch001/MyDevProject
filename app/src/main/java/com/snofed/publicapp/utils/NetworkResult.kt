package com.snofed.publicapp.utils

sealed class NetworkResult<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()

}

//sealed class NetworkResult<out T> {
//    data class Success<out T>(val data: T) : NetworkResult<T>()
//    data class Error(val message: String) : NetworkResult<Nothing>()
//    object Loading : NetworkResult<Nothing>()
//}

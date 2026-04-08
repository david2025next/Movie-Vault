package com.example.movievault.data.utils

import com.example.movievault.domain.DataErrors
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


fun Exception.getDataError(): DataErrors {

    return when (this) {
        is UnknownHostException -> DataErrors.NetworkErrors.NO_INTERNET
        is SocketTimeoutException -> DataErrors.NetworkErrors.TIMEOUT
        is HttpException -> getApiError(this.code())
        is SerializationException -> DataErrors.LocalErrors.JSON_PARSING_FAILURE
        is IOException -> DataErrors.NetworkErrors.UNKNOWN
        else -> DataErrors.LocalErrors.UNKNOWN
    }
}

private fun getApiError(code: Int): DataErrors.NetworkErrors {

    return when (code) {
        401 -> DataErrors.NetworkErrors.UNAUTHORIZED
        404 -> DataErrors.NetworkErrors.NOT_FOUND
        429 -> DataErrors.NetworkErrors.RATE_LIMIT
        in 500 .. 599 -> DataErrors.NetworkErrors.SERVER_ERROR
        else -> DataErrors.NetworkErrors.UNKNOWN
    }
}
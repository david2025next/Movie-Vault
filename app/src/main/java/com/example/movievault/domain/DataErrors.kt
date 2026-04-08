package com.example.movievault.domain

sealed interface DataErrors : Error {

    enum class NetworkErrors : DataErrors {
        SERVER_ERROR,
        UNAUTHORIZED,
        NOT_FOUND,
        RATE_LIMIT,
        TIMEOUT,
        NO_INTERNET,
        UNKNOWN
    }

    enum class LocalErrors : DataErrors {
        JSON_PARSING_FAILURE,
        DB_ERROR,
        CACHE_ERROR,
        UNKNOWN
    }
}
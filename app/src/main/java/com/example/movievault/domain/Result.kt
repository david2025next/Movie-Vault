package com.example.movievault.domain

typealias RootError = Error

sealed interface Result<out D, out rootError : RootError>  {
    data class Success<out D, out rootError : RootError>(val data : D)  : Result<D, rootError>
    data class Error<out D, out rootError : RootError>(val error : rootError) : Result<D, rootError>
}
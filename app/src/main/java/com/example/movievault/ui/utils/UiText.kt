package com.example.movievault.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed class UiText() {

    data class DynamicString(val name: String) : UiText()
    class StringResource(val id: Int, val args: Array<Any> = arrayOf()) : UiText()


    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> this.name
            is StringResource -> LocalContext.current.getString(id, *args)
        }
    }

}
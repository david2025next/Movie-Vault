package com.example.movievault.ui.utils

import com.example.movievault.R
import com.example.movievault.domain.DataErrors


fun DataErrors.asUiText(): UiText {

    return when (this) {
        DataErrors.LocalErrors.JSON_PARSING_FAILURE -> UiText.StringResource(
            R.string.error_json_parsing
        )

        DataErrors.LocalErrors.DB_ERROR -> UiText.StringResource(
            R.string.error_db
        )

        DataErrors.LocalErrors.CACHE_ERROR -> UiText.StringResource(
            R.string.error_cache
        )

        DataErrors.LocalErrors.UNKNOWN -> UiText.StringResource(
            R.string.error_local_unknown
        )

        DataErrors.NetworkErrors.SERVER_ERROR -> UiText.StringResource(
            R.string.error_server
        )

        DataErrors.NetworkErrors.UNAUTHORIZED -> UiText.StringResource(
            R.string.error_unauthorized
        )

        DataErrors.NetworkErrors.NOT_FOUND -> UiText.StringResource(
            R.string.error_not_found
        )

        DataErrors.NetworkErrors.RATE_LIMIT -> UiText.StringResource(
            R.string.error_rate_limit
        )

        DataErrors.NetworkErrors.TIMEOUT -> UiText.StringResource(
            R.string.error_timeout
        )

        DataErrors.NetworkErrors.NO_INTERNET -> UiText.StringResource(
            R.string.error_no_internet
        )

        DataErrors.NetworkErrors.UNKNOWN -> UiText.StringResource(
            R.string.error_network_unknown
        )
    }
}
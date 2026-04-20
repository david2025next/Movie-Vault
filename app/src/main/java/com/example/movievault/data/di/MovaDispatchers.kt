package com.example.movievault.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val movaDispatcher : MovaDispatchers)

enum class MovaDispatchers{
    IO,
    Default
}
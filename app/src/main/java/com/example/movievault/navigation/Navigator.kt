package com.example.movievault.navigation

import androidx.navigation3.runtime.NavKey

class Navigator(
    val state: NavigationState
) {

    fun navigate(route: NavKey) {
        if (route in state.backStacks.keys) {
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack() {

        if (state.currentKey== state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            state.currentSubStack.removeLastOrNull()
        }
    }
}
package com.pixelsmatter.testingsample.ui.screens.greeting

sealed class GreetingScreenAction {
    data class ScreenLoad(val time : Long) : GreetingScreenAction()
    data object RetrieveData : GreetingScreenAction()
}
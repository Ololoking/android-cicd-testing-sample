package com.pixelsmatter.testingsample.ui.screens.greeting

sealed class GreetingScreenAction {
    data object RetrieveData : GreetingScreenAction()
    data object ClearData : GreetingScreenAction()
}

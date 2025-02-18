package me.kabachel.todolist

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import me.kabachel.todolist.tasks.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow {
        App()
    }
}
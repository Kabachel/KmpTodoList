package me.kabachel.todolist

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
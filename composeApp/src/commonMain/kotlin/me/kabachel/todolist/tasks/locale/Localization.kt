package me.kabachel.todolist.tasks.locale

import androidx.compose.ui.text.intl.Locale
import me.kabachel.todolist.Task

fun String.localeToRussian(russianString: String): String {
    return if (Locale.current == RUSSIAN) russianString else this
}

fun Task.Priority.localeToRussian(): String {
    return if (Locale.current == RUSSIAN) when (this) {
        Task.Priority.Low -> "Низкий"
        Task.Priority.Medium -> "Средний"
        Task.Priority.High -> "Высокий"
        Task.Priority.Vital -> "Жизненно необходимый"
    } else this.value
}

private val RUSSIAN = Locale("ru")
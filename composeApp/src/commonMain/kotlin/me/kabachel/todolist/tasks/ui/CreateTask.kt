package me.kabachel.todolist.tasks.ui

import androidx.compose.runtime.Composable
import me.kabachel.todolist.tasks.Event
import me.kabachel.todolist.tasks.locale.localeToRussian
import me.kabachel.todolist.tasks.ui.components.TaskForms

@Composable
internal fun CreateTask(onEvent: (Event) -> Unit) {
    TaskForms(
        initTask = null,
        submitButtonText = "Create".localeToRussian("Создать"),
        onSubmit = { task -> onEvent(Event.CreatedTask(task)) },
    )
}
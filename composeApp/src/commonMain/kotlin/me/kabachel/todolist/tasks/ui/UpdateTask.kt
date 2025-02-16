package me.kabachel.todolist.tasks.ui

import androidx.compose.runtime.Composable
import me.kabachel.todolist.tasks.Event
import me.kabachel.todolist.tasks.State
import me.kabachel.todolist.tasks.ui.components.TaskForms

@Composable
internal fun UpdateTask(state: State.UpdateTask, onEvent: (Event) -> Unit) {
    TaskForms(
        initTask = state.task,
        submitButtonText = "Update",
        onSubmit = { task -> onEvent(Event.UpdatedTask(task)) }
    )
}

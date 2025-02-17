package me.kabachel.todolist.tasks

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import me.kabachel.todolist.tasks.ui.CreateTask
import me.kabachel.todolist.tasks.ui.TasksContent
import me.kabachel.todolist.tasks.ui.TasksLoading
import me.kabachel.todolist.tasks.ui.UpdateTask

@Composable
internal fun TasksUI() {
    val viewModel: TasksViewModel = viewModel { TasksViewModel(TasksRepositoryImpl()) }
    val onEvent: (Event) -> Unit = viewModel::handleEvent
    when (val state = viewModel.viewState.value) {
        is State.Loading -> {
            TasksLoading()
        }

        is State.TasksContent -> {
            TasksContent(state, onEvent)
        }

        is State.CreateTask -> {
            CreateTask(onEvent)
        }

        is State.UpdateTask -> {
            UpdateTask(state, onEvent)
        }
    }
}
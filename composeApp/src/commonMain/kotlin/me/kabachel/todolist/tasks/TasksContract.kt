@file:OptIn(ExperimentalUuidApi::class)

package me.kabachel.todolist.tasks

import me.kabachel.todolist.Task
import me.kabachel.todolist.mvi.ViewEvent
import me.kabachel.todolist.mvi.ViewState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed interface State : ViewState {
    data object Loading : State
    data class TasksContent(val tasks: List<Task>) : State
    data object CreateTask : State
    data class UpdateTask(val task: Task) : State
}

sealed interface Event : ViewEvent {

    sealed interface SortBy : Event {
        data object Name : SortBy
        data object Description : SortBy
        data object Priority : SortBy
    }

    data object CreateTaskClick : Event
    data class CreatedTask(val task: Task) : Event

    data class DeleteTaskClick(val uuid: Uuid) : Event

    data class UpdateTaskClick(val task: Task) : Event
    data class UpdatedTask(val task: Task) : Event

    data class CompleteTask(val task: Task, val isCompleted: Boolean) : Event
}
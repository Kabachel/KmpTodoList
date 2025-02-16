package me.kabachel.todolist

import me.kabachel.todolist.mvi.ViewEvent
import me.kabachel.todolist.mvi.ViewState

sealed interface State : ViewState {
    data object Loading : State
    data class TasksContent(val tasks: List<Task>) : State
    data object CreateTask : State
}

sealed interface Event : ViewEvent {

    sealed interface SortBy : Event {
        data object Name : SortBy
        data object Description : SortBy
        data object Priority : SortBy
    }

    data object CreateTaskClick : Event
    data class CreatedTask(val task: Task) : Event
}
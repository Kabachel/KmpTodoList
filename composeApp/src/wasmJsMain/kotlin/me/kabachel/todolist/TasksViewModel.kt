package me.kabachel.todolist

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.kabachel.todolist.mvi.BaseViewModel
import me.kabachel.todolist.mvi.NoEffect

class TasksViewModel(
    private val tasksRepository: TasksRepository,
) : BaseViewModel<Event, State, NoEffect>() {

    override fun setInitialState(): State = State.Loading

    init {
        viewModelScope.launch {
            val tasks = tasksRepository.getTasks()
            setState { State.TasksContent(tasks) }
        }
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.SortBy -> when (event) {
                is Event.SortBy.Name -> {
                    val currentState = viewState.value as? State.TasksContent ?: return
                    val sortedTasks = currentState.tasks.sortedBy { it.name }
                    setState { State.TasksContent(sortedTasks) }
                }

                is Event.SortBy.Description -> {
                    val currentState = viewState.value as? State.TasksContent ?: return
                    val sortedTasks = currentState.tasks.sortedBy { it.description }
                    setState { State.TasksContent(sortedTasks) }
                }

                is Event.SortBy.Priority -> {
                    val currentState = viewState.value as? State.TasksContent ?: return
                    val sortedTasks = currentState.tasks.sortedBy { it.priority }
                    setState { State.TasksContent(sortedTasks) }
                }
            }
        }
    }
}
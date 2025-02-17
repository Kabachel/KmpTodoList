@file:OptIn(ExperimentalUuidApi::class)

package me.kabachel.todolist.tasks

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.kabachel.todolist.Task
import me.kabachel.todolist.mvi.BaseViewModel
import me.kabachel.todolist.mvi.NoEffect
import kotlin.uuid.ExperimentalUuidApi

class TasksViewModel(
    private val tasksRepository: TasksRepositoryImpl,
) : BaseViewModel<Event, State, NoEffect>() {

    override fun setInitialState(): State = State.Loading

    init {
        viewModelScope.launch {
            val tasks = tasksRepository.getTasks().defaultSort()
            setState { State.TasksContent(tasks) }
        }
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.SortBy -> when (event) {
                is Event.SortBy.Name -> {
                    val currentState = viewState.value as? State.TasksContent ?: return
                    val sortedTasks = currentState.tasks.sortedWith(compareBy({ it.isCompleted }, { it.name }))
                    setState { State.TasksContent(sortedTasks) }
                }

                is Event.SortBy.Description -> {
                    val currentState = viewState.value as? State.TasksContent ?: return
                    val sortedTasks = currentState.tasks.sortedWith(compareBy({ it.isCompleted }, { it.description }))
                    setState { State.TasksContent(sortedTasks) }
                }

                is Event.SortBy.Priority -> {
                    val currentState = viewState.value as? State.TasksContent ?: return
                    val sortedTasks = currentState.tasks.sortedWith(compareBy({ it.isCompleted }, { it.priority }))
                    setState { State.TasksContent(sortedTasks) }
                }
            }

            is Event.CreateTaskClick -> {
                setState { State.CreateTask }
            }

            is Event.CreatedTask -> {
                viewModelScope.launch {
                    setState { State.Loading }
                    tasksRepository.createTask(event.task)
                    val tasks = tasksRepository.getTasks().defaultSort()
                    setState { State.TasksContent(tasks) }
                }
            }

            is Event.DeleteTaskClick -> {
                viewModelScope.launch {
                    setState { State.Loading }
                    tasksRepository.deleteTask(event.uuid)
                    val tasks = tasksRepository.getTasks().defaultSort()
                    setState { State.TasksContent(tasks) }
                }
            }

            is Event.UpdateTaskClick -> {
                viewModelScope.launch {
                    setState { State.UpdateTask(event.task) }
                }
            }

            is Event.UpdatedTask -> {
                viewModelScope.launch {
                    setState { State.Loading }
                    tasksRepository.updateTask(event.task)
                    val tasks = tasksRepository.getTasks().defaultSort()
                    setState { State.TasksContent(tasks) }
                }
            }

            is Event.CompleteTask -> {
                viewModelScope.launch {
                    setState { State.Loading }
                    val completedTask = event.task.copy(isCompleted = event.isCompleted)
                    tasksRepository.updateTask(completedTask)
                    val tasks = tasksRepository.getTasks().defaultSort()
                    setState { State.TasksContent(tasks) }
                }
            }
        }
    }
}

private fun List<Task>.defaultSort(): List<Task> {
    return sortedWith(compareBy({ it.isCompleted }, { it.name }))
}
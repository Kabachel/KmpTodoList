package me.kabachel.todolist

import kotlinx.serialization.Serializable
import me.kabachel.todolist.utils.UuidSerializer
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class Task(
    @Serializable(with = UuidSerializer::class)
    val uuid: Uuid = Uuid.NIL,
    val name: String,
    val description: String,
    val priority: Priority,
    val isCompleted: Boolean,
) {
    enum class Priority(val value: String) {
        Vital("Vital"),
        High("High"),
        Medium("Medium"),
        Low("Low"),
    }
}
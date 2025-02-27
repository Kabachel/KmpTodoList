@file:OptIn(ExperimentalUuidApi::class)

package me.kabachel.todolist.tasks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.kabachel.todolist.Task
import me.kabachel.todolist.tasks.locale.localeToRussian
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TaskForms(initTask: Task?, submitButtonText: String, onSubmit: (Task) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "$submitButtonText a task".localeToRussian("$submitButtonText задачу"),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Task name".localeToRussian("Название задачи"),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        var taskName by remember { mutableStateOf(initTask?.name ?: "") }
        var taskNameIsError by remember { mutableStateOf(false) }
        fun validateTaskName(text: String) {
            taskNameIsError = text.isBlank()
        }
        TextField(
            value = taskName,
            onValueChange = {
                taskName = it
                validateTaskName(taskName)
            },
            singleLine = true,
            placeholder = { Text("E.g. write a blog post".localeToRussian("Написать пост в блог")) },
            isError = taskNameIsError,
            supportingText = {
                if (taskNameIsError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Task name cannot be empty".localeToRussian("Имя задачи не может быть пустым"),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (taskNameIsError) Icon(Icons.Outlined.Info, "error", tint = MaterialTheme.colorScheme.error)
            },
            keyboardActions = KeyboardActions { validateTaskName(taskName) },
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Description".localeToRussian("Описание"),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        var taskDescription by remember { mutableStateOf(initTask?.description ?: "") }
        TextField(
            value = taskDescription,
            onValueChange = { taskDescription = it },
            singleLine = true,
            placeholder = { Text("Add more details to your task".localeToRussian("Добавь описание к своей задаче")) },
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Priority".localeToRussian("Приоритет"),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        var expanded by remember { mutableStateOf(false) }
        var taskPriority by remember { mutableStateOf(initTask?.priority ?: Task.Priority.Low) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                readOnly = true,
                value = taskPriority.localeToRussian(),
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Task.Priority.entries.reversed().forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.localeToRussian()) },
                        onClick = {
                            taskPriority = option
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                val task = Task(
                    uuid = initTask?.uuid ?: Uuid.NIL,
                    name = taskName,
                    description = taskDescription,
                    priority = taskPriority,
                    isCompleted = initTask?.isCompleted ?: false,
                )
                onSubmit(task)
            },
            enabled = taskNameIsError.not() and taskName.isNotBlank(),
        ) {
            Text(text = submitButtonText)
        }
    }
}
@file:OptIn(ExperimentalUuidApi::class)

package me.kabachel.todolist.tasks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.kabachel.todolist.Task
import kotlin.uuid.ExperimentalUuidApi

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
            "Create a task",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Task name",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        var taskName by remember { mutableStateOf(initTask?.name ?: "") }
        TextField(
            value = taskName,
            onValueChange = { taskName = it },
            placeholder = { Text("E.g. write a blog post") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Description",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        var taskDescription by remember { mutableStateOf(initTask?.description ?: "") }
        TextField(
            value = taskDescription,
            onValueChange = { taskDescription = it },
            placeholder = { Text("Add more details to your task") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Priority",
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
                value = taskPriority.value,
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Task.Priority.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.value) },
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
                onSubmit(
                    Task(
                        name = taskName,
                        description = taskDescription,
                        priority = taskPriority,
                        isCompleted = false,
                    )
                )
            }
        ) {
            Text(text = submitButtonText)
        }
    }
}
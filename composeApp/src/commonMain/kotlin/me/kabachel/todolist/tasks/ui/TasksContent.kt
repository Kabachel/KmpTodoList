package me.kabachel.todolist.tasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.kabachel.todolist.Task
import me.kabachel.todolist.tasks.Event
import me.kabachel.todolist.tasks.State
import kotlin.uuid.ExperimentalUuidApi

@Composable
internal fun TasksContent(
    state: State.TasksContent,
    onEvent: (Event) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "My Tasks",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))

        SortingChips(onEvent)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = { onEvent(Event.CreateTaskClick) }, colors = ButtonDefaults.buttonColors()) {
            Text("Create task")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(state.tasks) { task ->
                TaskItem(task, onEvent)
            }
        }
    }
}

@Composable
private fun SortingChips(onEvent: (Event) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        var currentChip by remember { mutableStateOf<Event.SortBy>(Event.SortBy.Name) }
        Text(
            "Sort by:",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(
            selected = currentChip == Event.SortBy.Name,
            onClick = {
                onEvent(Event.SortBy.Name)
                currentChip = Event.SortBy.Name
            },
            label = { Text("Name") },
        )
        FilterChip(
            selected = currentChip == Event.SortBy.Description,
            onClick = {
                onEvent(Event.SortBy.Description)
                currentChip = Event.SortBy.Description
            },
            label = { Text("Description") },
        )
        FilterChip(
            selected = currentChip == Event.SortBy.Priority,
            onClick = {
                onEvent(Event.SortBy.Priority)
                currentChip = Event.SortBy.Priority
            },
            label = { Text("Priority") },
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun TaskItem(task: Task, onEvent: (Event) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            var isChecked by remember { mutableStateOf(task.isCompleted) }
            Switch(checked = isChecked, onCheckedChange = {
                isChecked = it
                onEvent(Event.CompleteTask(task, isChecked))
            })
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.name, fontWeight = FontWeight.Bold)
                Text(text = task.description, color = Color.Gray, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                OutlinedCard(
                    colors = CardDefaults.outlinedCardColors(containerColor = task.priority.getPriorityColor()),
                    shape = CircleShape,
                ) {
                    Text(task.priority.value, modifier = Modifier.padding(8.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedButton(onClick = { onEvent(Event.UpdateTaskClick(task)) }) {
                    Text(text = "Edit")
                }
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedButton(onClick = { onEvent(Event.DeleteTaskClick(task.uuid)) }) {
                    Text(text = "Delete")
                }
            }
        }
    }
}

private fun Task.Priority.getPriorityColor(): Color {
    return when (this) {
        Task.Priority.Low -> Color.Gray.copy(alpha = 0.5f)
        Task.Priority.Medium -> Color.Yellow.copy(alpha = 0.5f)
        Task.Priority.High -> Color.Red.copy(alpha = 0.5f)
        Task.Priority.Vital -> Color.Magenta.copy(alpha = 0.5f)
    }
}
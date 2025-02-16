package me.kabachel.todolist.tasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    ) {
        Text(
            "My Tasks",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Sort by",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(
                "Name",
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = Color.Gray)
                    .clickable { onEvent(Event.SortBy.Name) }
                    .padding(vertical = 3.dp, horizontal = 5.dp)
            )
            Text(
                "Description",
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = Color.Gray)
                    .clickable { onEvent(Event.SortBy.Description) }
                    .padding(vertical = 3.dp, horizontal = 5.dp)
            )
            Text(
                "Priority",
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = Color.Gray)
                    .clickable { onEvent(Event.SortBy.Priority) }
                    .padding(vertical = 3.dp, horizontal = 5.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "Create task",
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = Color.Gray)
                    .clickable { onEvent(Event.CreateTaskClick) }
                    .padding(vertical = 3.dp, horizontal = 5.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(state.tasks) { task ->
                TaskItem(task, onEvent)
            }
        }
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
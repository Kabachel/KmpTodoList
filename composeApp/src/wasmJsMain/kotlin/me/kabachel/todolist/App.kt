package me.kabachel.todolist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun App() {
    MaterialTheme {
        val viewModel: TasksViewModel = viewModel { TasksViewModel(TasksRepository()) }
        val onEvent: (Event) -> Unit = viewModel::handleEvent
        when (val state = viewModel.viewState.value) {
            is State.Loading -> {
                CircularProgressIndicator()
            }

            is State.TasksContent -> {
                TasksContent(onEvent, state)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TasksContent(
    onEvent: (Event) -> Unit,
    state: State.TasksContent
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

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp),
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
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(state.tasks) { task ->
                TaskItem(task)
            }
        }
    }
}

@Composable
private fun TaskItem(task: Task) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.name, fontWeight = FontWeight.Bold)
                Text(text = task.description, color = Color.Gray, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = task.priority.value,
                    color = Color.White,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(task.priority.getPriorityColor())
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(onClick = {}) {
                    Text(text = "Edit")
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

private const val BASE_URL = "http://localhost:8080"
package me.kabachel.todolist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.ktor.client.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val client = HttpClient {
    install(Logging)
}

@Composable
fun App() {
    MaterialTheme {
        var text by remember { mutableStateOf("Please wait...") }
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            delay(3000)
            scope.launch {
                val url = Url(BASE_URL)
                val response = client.get(url)
                text = response.bodyAsText()
            }
        }

        Box(Modifier.fillMaxSize()) {
            Text(
                text,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private const val BASE_URL = "http://localhost:8080"
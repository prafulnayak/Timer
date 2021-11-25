package com.yougov.aevum

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimersScreen(emptyList())
        }
    }
}

@Composable
fun TimersScreen(timers: List<Time>) {
    Surface {
        Column(modifier = Modifier.padding(16.dp)) {
            NewTimer()
            Spacer(modifier = Modifier.height(24.dp))
            Text("Running timers:")
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(timers) { timer ->
                    Divider()
                    Timer(timer.value)
                }
            }

        }
    }
}

@Composable
fun NewTimer() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = spacedBy(8.dp)) {
        TimeInput(placeholderText = "hours", modifier = Modifier.weight(1f))
        TimeInput(placeholderText = "minutes", modifier = Modifier.weight(1f))
        TimeInput(placeholderText = "seconds", modifier = Modifier.weight(1f))
        Button(
            onClick = {},
            modifier = Modifier.weight(1f)
        ) {
            Text("Start!")
        }
    }
}

@Composable
fun TimeInput(placeholderText: String, modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    TextField(
        value = input,
        onValueChange = { input = it },
        placeholder = {
            Text(
                text = placeholderText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(fontSize = 10.sp)
            )
        },
        modifier = modifier
    )
}

@Composable
fun Timer(value: String) {
    Text(text = value, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium), modifier = Modifier.padding(vertical = 16.dp))
}

@Preview
@Composable
fun PreviewTimeInput() {
    TimeInput(placeholderText = "seconds")
}

@Preview
@Composable
fun PreviewTimers() {
    TimersScreen(listOf(Time("03:23:14"), Time("00:00:11")))
}

@Preview
@Composable
fun PreviewTimersEmpty() {
    TimersScreen(emptyList())
}


@Preview
@Composable
fun PreviewTimer() {
    Timer("03:23:14")
}

@Preview
@Composable
fun PreviewNewTimer() {
    NewTimer()
}

data class Time(val value: String)
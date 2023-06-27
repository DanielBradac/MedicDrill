package cz.bradacd.medicdrill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.bradacd.medicdrill.ui.theme.MedicDrillTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedicDrillTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MedicDrill()
                }
            }
        }
    }
}

@Composable
fun MedicDrill() {
    var categoryCount by remember { mutableStateOf("") }
    val sliderIndexMap = remember { mutableStateMapOf<Int, Int>() }
    var currentQuestion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .fillMaxWidth(),
            value = categoryCount,
            onValueChange = {
                categoryCount = it
                sliderIndexMap.clear()
                repeat(categoryCount.toIntOrNull() ?: 0) { index ->
                    sliderIndexMap[index] = 1
                }
            },
            label = { Text("Number of categories") }
        )
        LazyColumn(
            modifier = Modifier.heightIn(0.dp, 500.dp)
        ) {
            items(categoryCount.toIntOrNull() ?: 0) { index ->
                var sliderPosition by remember { mutableStateOf(1f) }
                Text(text = "${getCategoryLabel(index)}: ${sliderPosition.roundToInt()}")
                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it

                    },
                    onValueChangeFinished = {
                        sliderIndexMap[index] = sliderPosition.roundToInt()
                    },
                    valueRange = 1f..100f,
                    steps = 99,
                )
            }
        }

        Button(
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 4.dp)
                .fillMaxWidth(),
            onClick = {
                currentQuestion = generateQuestion(sliderIndexMap)
            }) {
            Text(text = "Choose question")
        }

        Row {
            Text("Your current question: ", fontSize = 24.sp)
            Text(
                currentQuestion,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun generateQuestion(questionsMap: Map<Int, Int>): String {
    if (questionsMap.isNotEmpty()) {
        val category = (0 until questionsMap.keys.size).random()
        val question = (1..(questionsMap[category] ?: 1)).random()
        return "${getCategoryLabel(category)}$question"
    }
    return ""
}

private fun getCategoryLabel(index: Int): String {
    return ('A' + index).toString()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MedicDrill()
}
package uk.co.nafeelectricalsolutionsltd.procalc

import CalculatorViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorScreen()
        }
    }

    @Composable
    fun CalculatorScreen(viewModel: CalculatorViewModel = viewModel()) {
        val input by viewModel.input.observeAsState("")
        val result by viewModel.result.observeAsState("")
        val showScientificNotation by viewModel.showScientificNotation.observeAsState(false) // Observe and extract the boolean value

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            TextField(
                value = input,
                onValueChange = { newValue -> viewModel.onButtonClick(newValue) },
                modifier = Modifier.fillMaxWidth(), // Set the TextField to fill the full width
                label = { Text("Input") }
            )

            // Button grid
            Column(modifier = Modifier.padding(top = 8.dp)) {
                // Number buttons
                for (i in 1..3) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (j in (i - 1) * 3 + 1..i * 3) {
                            Button(
                                onClick = { viewModel.onButtonClick(j.toString()) },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp),
                                content = { Text(j.toString()) }
                            )
                        }
                    }
                }

                // Zero button
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { viewModel.onButtonClick("0") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        content = { Text("0") }
                    )
                }

                // Operation buttons
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (operation in listOf("+", "-", "*", "/")) {
                        Button(
                            onClick = { viewModel.onOperationClick(operation) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp),
                            content = { Text(operation) }
                        )
                    }
                }

                // Equals button
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { viewModel.onEqualsClick() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        content = { Text("=") }
                    )
                }
            }

            // Switch for scientific notation
            SwitchRow(
                modifier = Modifier.padding(top = 8.dp),
                isChecked = showScientificNotation, // Pass the boolean value directly
                onCheckedChange = { isChecked -> viewModel.toggleScientificNotation(isChecked) }
            )

            // Result text
            Text(
                text = if (showScientificNotation) result ?: "" else String.format("%.5f", result?.toDoubleOrNull() ?: 0.0), // Check if scientific notation is enabled
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }


    @Composable
    fun SwitchRow(
        modifier: Modifier = Modifier,
        isChecked: Boolean,
        onCheckedChange: (Boolean) -> Unit
    ) {
        Surface(
            modifier = modifier,
            color = Color.Transparent
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Show Scientific Notation")
                Switch(
                    checked = isChecked,
                    onCheckedChange = onCheckedChange,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}




package com.example.se2_einzelbeispiel

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.NumberFormatException
import java.net.Socket

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {

    var responseText by remember {
        mutableStateOf("No request yet!")
    }

    var requestText by remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            TextField(value = requestText,
                onValueChange = { input ->
                    if(input.isEmpty() || input.all { char -> char.isDigit() }) {
                        requestText = input
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text("Matrikelnummer...") },
            )

            Box(
                contentAlignment = Alignment.Center
            ) {
                Row(
                ) {
                    Button(onClick = {
                        Thread {
                            responseText = sendRequest("se2-submission.aau.at", 20080, requestText)
                        }.start()
                    }, modifier = Modifier
                        .padding(0.dp, 10.dp, 0.dp, 10.dp)
                    ) {
                        Text(text = "Send to Server")
                    }

                    Button(onClick = {
                        responseText = calculateBinFromSum(requestText)
                    }, modifier = Modifier.padding(5.dp, 10.dp, 0.dp, 10.dp)

                    ) {
                        Text(text = "Sum to Binary")
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .padding(20.dp, 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = "Output:",
                style = TextStyle(fontSize = 24.sp),
            )
            Text(
                text = responseText,
                style = TextStyle(fontSize = 15.sp),
            )
        }
    }
}

fun calculateBinFromSum(mn: String): String {
    val response = "Keine gültige Matrikelnummer!"
    if(mn.length != 8) return response

    return try {
        Integer.toBinaryString(mn.sumOf { char -> char.digitToInt() })
    } catch (e: NumberFormatException) {
        Log.d("exception", "NumberFormatException")
        "Could not convert to Number"
    }
}
fun sendRequest(address: String, port: Int, mn: String): String {
    val socket = Socket(address, port)
    val output = PrintWriter(socket.getOutputStream(), true)
    val input = BufferedReader(InputStreamReader(socket.inputStream))
    output.println(mn)
    val result = input.readLine()
    socket.close()
    return result
}
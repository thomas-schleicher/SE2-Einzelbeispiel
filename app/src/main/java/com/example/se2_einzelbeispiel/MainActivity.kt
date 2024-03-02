package com.example.se2_einzelbeispiel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Button(onClick = { sendRequest("se2-submission.aau.at", 20080)}) {
                
            }
        }
    }
}

fun sendRequest(address: String, port: Int) {
    val requestThread = Thread {
        val socket = Socket(address, port)
        val output = PrintWriter(socket.getOutputStream(), true)
        val input = BufferedReader(InputStreamReader(socket.inputStream))

        println("Client sending [12211604]")
        output.println("12211604")
        println("Client receiving [${input.readLine()}]")
        socket.close()
    }.start()
}
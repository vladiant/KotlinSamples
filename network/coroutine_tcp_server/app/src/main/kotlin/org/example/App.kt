package org.example

import kotlinx.coroutines.*

import java.net.ServerSocket
import java.net.Socket
import java.util.Scanner
import java.io.PrintWriter

fun startMultiClientServer() = runBlocking {
    val server = ServerSocket(9999)
    println("Server listens on port: ${server.localPort}")

    while(true) {
        // accept() blocks the thread so it is wrapped in withContext
        val client = withContext(Dispatchers.IO){server.accept()}
        println("New client connected ${client.inetAddress.hostAddress}")

        // Start new coroutine for each client
        launch(Dispatchers.IO) {
            handleClient(client)
        }
    }
}

suspend fun handleClient(client:Socket) {
    client.use{s -> //. use atomatically closes socket at the end
        val reader = Scanner(s.getInputStream())
        val writer = PrintWriter(s.getOutputStream(), true)

        while(reader.hasNextLine()) {
            val line = reader.nextLine()
            if (line == "exit") {
                break
            }

            println("[${Thread.currentThread().name}] Received: $line")
            writer.println("Echo: $line")
        }

    }

    println("Client connection terminated")
}

class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

fun main() {
    startMultiClientServer()
}

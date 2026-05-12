import kotlin.concurrent.thread
import java.net.Socket
import java.net.ServerSocket
import java.util.Scanner
import java.io.PrintWriter

fun startServer() {
    val server = ServerSocket(9999)
    println("Server started at port ${server.localPort}...")

    val client = server.accept()
    println("client connected: ${client.inetAddress.hostAddress}")

    val reader = Scanner(client.getInputStream())
    val writer = PrintWriter(client.getOutputStream(), true)

    if (reader.hasNextLine()) {
        var line = reader.nextLine()
        println("Received from client: $line")
        writer.println("Server says: received '$line'")
    }

    client.close()
    server.close()
}

fun startClient() {
    var client = Socket("127.0.0.1", 9999)
    val writer = PrintWriter(client.getOutputStream(), true) // autoFlush
    val reader = Scanner(client.getInputStream())

    println("Sending message")
    writer.println("Hello, Server!")

    if(reader.hasNextLine()) {
        println("Server response: ${reader.nextLine()}")
    }

    client.close()
}

fun main() {
    thread{ startServer() }
    Thread.sleep(1000)
    startClient()
}

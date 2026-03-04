import java.net.ServerSocket
import java.util.Scanner
import java.io.PrintWriter

fun main() {
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

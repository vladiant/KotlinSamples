import java.net.Socket
import java.util.Scanner
import java.io.PrintWriter

fun main() {
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

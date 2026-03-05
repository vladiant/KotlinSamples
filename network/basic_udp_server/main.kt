import java.net.DatagramPacket
import java.net.DatagramSocket
import java.io.PrintWriter

fun main() {
    var socket = DatagramSocket(9999)
    var buffer = ByteArray(1024)

    println("UDP server waits for packages")

    try{
        while(true){
            val packet = DatagramPacket(buffer, buffer.size)
            socket.receive(packet) // Blocks until packet arrives

            val message = String(packet.data, 0, packet.length)
            println("Received from ${packet.address}:${packet.port} -> $message")

            // Send answer back
            val responseText = "echo: $message"
            val responseData = responseText.toByteArray()
            val responsePacket = DatagramPacket(
                responseData, responseData.size,
                packet.address, packet.port
            )

            socket.send(responsePacket)
        }

    } finally {
        socket.close()
    }
}

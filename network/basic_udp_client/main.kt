import java.net.DatagramPacket
import java.net.DatagramSocket
import java.io.PrintWriter
import java.net.InetAddress

// nc -vz -u <destination_IP> <port>
// nmap -sU -p <port> <IP>
fun main() {
    var socket = DatagramSocket()
    val serverAddress = InetAddress.getByName("127.0.0.1")
    val message = "Hello, UDP server!".toByteArray()

    // Sending
    val sendPacket = DatagramPacket(
        message, message.size,
        serverAddress, 9999
    )
    socket.send(sendPacket)

    // Receiving answer
    val buffer = ByteArray(1024)
    val receivePacket = DatagramPacket(buffer, buffer.size)
    socket.receive(receivePacket)

    val response = String(receivePacket.data, 0, receivePacket.length)
    println("Server response: $response")

    socket.close()
}

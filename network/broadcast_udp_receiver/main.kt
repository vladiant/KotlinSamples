import java.net.DatagramSocket
import java.net.DatagramPacket

fun main() {
    // Listen all interfaces (0.0.0.0) at port 9999
    var socket = DatagramSocket(9999)
    val buffer = ByteArray(1024)

    println("Waiting for broadcast messages...")

    while(true) {
        val packet = DatagramPacket(buffer, buffer.size)
        socket.receive(packet) // Blocked until packet is received

        val text = String(packet.data, 0, packet.length)
        println("Received broadcast from ${packet.address}: $text")
    }
}

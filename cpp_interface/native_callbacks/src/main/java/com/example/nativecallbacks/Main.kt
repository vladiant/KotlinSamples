package com.example.nativecallbacks

/**
 * Standalone demo showing callbacks from a C++ worker thread to Kotlin via JNI.
 *
 * The NativeClass runs a background thread that periodically calls back
 * into Kotlin via the NativeListener interface.
 *
 * Based on: https://habr.com/ru/articles/1017486/ (Part 2)
 */
fun main() {
    println("=== Native Callbacks Demo ===")

    val nc = NativeClass()

    // Set up the listener — callbacks arrive on a native thread
    nc.setListener(object : NativeListener {
        override fun print(str: String) {
            println("[Callback] $str")
        }
    })

    // Start the background scanner
    println("Starting scan... Press Enter to stop.")
    nc.scan()

    // Wait for user input to stop
    readlnOrNull()

    println("Stopping scan...")
    nc.stop()

    // Give the worker thread a moment to finish
    Thread.sleep(1500)

    nc.free()
    println("Done.")
}

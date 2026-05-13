package com.example.simplewrapper

/**
 * Standalone demo showing how to use the Kotlin wrapper for a C++ class via JNI.
 *
 * Based on: https://habr.com/ru/articles/1017486/ (Part 1)
 */
fun main() {
    println("=== Simple C++ Wrapper Demo ===")

    // Create the native object
    val nc = NativeClass()
    println("NativeClass object created in Kotlin")

    // Call the native function
    nc.fun_()

    // Clean up
    nc.free()
    println("NativeClass object freed")
}

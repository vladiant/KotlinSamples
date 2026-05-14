package com.example.nativecallbacks

/**
 * Kotlin interface for receiving callbacks from native C++ code.
 * The native JniListener bridges C++ Listener::print() calls to this interface.
 *
 * Based on: https://habr.com/ru/articles/1017486/ (Part 2)
 */
interface NativeListener {
    fun print(str: String)
}

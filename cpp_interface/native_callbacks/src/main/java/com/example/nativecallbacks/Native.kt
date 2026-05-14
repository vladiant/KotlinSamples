package com.example.nativecallbacks

/**
 * JNI native function declarations.
 *
 * Based on: https://habr.com/ru/articles/1017486/ (Part 2)
 */
object Native {
    init {
        System.loadLibrary("nativecallbacks")
    }

    external fun initNativeClass(): Long
    external fun destroyNativeClass(cppClass: Long)
    external fun nativeClassScan(cppClass: Long)
    external fun nativeClassStop(cppClass: Long)
    external fun nativeClassSetListener(cppClass: Long, listener: NativeListener)
}

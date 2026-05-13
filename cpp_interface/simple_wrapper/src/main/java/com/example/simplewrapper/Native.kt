package com.example.simplewrapper

/**
 * JNI native function declarations.
 * These map to the extern "C" functions in native-lib.cpp.
 *
 * Based on: https://habr.com/ru/articles/1017486/ (Part 1)
 */
object Native {
    init {
        System.loadLibrary("simplewrapper")
    }

    external fun initNativeClass(): Long
    external fun destroyNativeClass(cppClass: Long)
    external fun nativeClassFun(cppClass: Long)
}

package com.example.simplewrapper

/**
 * Kotlin wrapper around the C++ NativeClass.
 *
 * Stores the native object address as a Long and delegates all operations
 * to the JNI bridge functions in [Native].
 *
 * The native object is freed when this wrapper is garbage collected (via finalize),
 * but you can also call [free] manually for deterministic cleanup.
 *
 * Based on: https://habr.com/ru/articles/1017486/ (Part 1)
 */
class NativeClass {
    private var cppClass: Long = Native.initNativeClass()

    fun fun_() {
        Native.nativeClassFun(cppClass)
    }

    fun free() {
        if (cppClass != 0L) {
            Native.destroyNativeClass(cppClass)
            cppClass = 0L
        }
    }

    @Suppress("deprecation")
    protected fun finalize() {
        free()
    }
}

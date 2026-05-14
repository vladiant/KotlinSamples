package com.example.nativecallbacks

/**
 * Kotlin wrapper around the C++ NativeClass with callback support.
 *
 * Usage:
 *   val nc = NativeClass()
 *   nc.setListener(object : NativeListener {
 *       override fun print(str: String) { ... }
 *   })
 *   nc.scan()   // starts background scanning
 *   nc.stop()   // stops scanning
 *   nc.free()   // releases native resources
 *
 * Important: keep a strong reference to the listener so it is not
 * garbage collected while native code still references it.
 *
 * Based on: https://habr.com/ru/articles/1017486/ (Part 2)
 */
class NativeClass {
    private var cppClass: Long = Native.initNativeClass()
    private var listener: NativeListener? = null

    fun setListener(listener: NativeListener) {
        // Keep a strong reference to prevent GC from collecting the listener
        this.listener = listener
        Native.nativeClassSetListener(cppClass, listener)
    }

    fun scan() {
        Native.nativeClassScan(cppClass)
    }

    fun stop() {
        Native.nativeClassStop(cppClass)
    }

    fun free() {
        if (cppClass != 0L) {
            stop()
            Native.destroyNativeClass(cppClass)
            cppClass = 0L
            listener = null
        }
    }

    @Suppress("deprecation")
    protected fun finalize() {
        free()
    }
}

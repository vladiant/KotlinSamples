#include <jni.h>
#include "NativeClass.h"

// JNI bridge functions that convert object-oriented C++ code
// into C-style functions callable from Kotlin/Java via JNI.
//
// The key idea: store the C++ object pointer as a jlong in the Kotlin side,
// and pass it back to native functions that need to operate on the object.
//
// Based on: https://habr.com/ru/articles/1017486/ (Part 1)

extern "C" {

// Create a new NativeClass instance and return its address as jlong
JNIEXPORT jlong JNICALL
Java_com_example_simplewrapper_Native_initNativeClass(JNIEnv* env, jobject thiz) {
    return (jlong) new NativeClass();
}

// Call fun() on the NativeClass instance identified by cppClass pointer
JNIEXPORT void JNICALL
Java_com_example_simplewrapper_Native_nativeClassFun(JNIEnv* env, jobject thiz, jlong cppClass) {
    ((NativeClass*) cppClass)->fun();
}

// Delete the NativeClass instance to free native memory
JNIEXPORT void JNICALL
Java_com_example_simplewrapper_Native_destroyNativeClass(JNIEnv* env, jobject thiz, jlong cppClass) {
    delete (NativeClass*) cppClass;
}

} // extern "C"

#include <jni.h>
#include <cstdio>
#include "NativeClass.h"

// Global JavaVM pointer, obtained from JNI_OnLoad.
// Required for attaching native threads to the JVM when making callbacks.
static JavaVM* g_jvm = nullptr;

// JNI Listener implementation that bridges C++ callbacks to Kotlin/Java.
//
// Key concepts from the article:
// - Uses a weak global reference to the Kotlin listener object
// - Attaches the native thread to JVM before making callbacks
// - Caches jmethodID for the "print" method
//
// Based on: https://habr.com/ru/articles/1017486/ (Part 2 & 3)
class JniListener : public NativeClass::Listener {
private:
    JavaVM* jvm;
    jweak listenerRef;
    jmethodID printMethod = nullptr;

public:
    JniListener(JavaVM* jvm, JNIEnv* env, jobject listener) : jvm(jvm) {
        // Create a weak global reference to the Kotlin listener object.
        // Weak ref allows GC to collect the listener if no strong refs remain.
        listenerRef = env->NewWeakGlobalRef(listener);

        // Get the class and method ID for the "print" method.
        // Signature "(Ljava/lang/String;)V" means: takes a String, returns void.
        jclass clazz = env->GetObjectClass(listener);
        if (clazz != nullptr) {
            printMethod = env->GetMethodID(clazz, "print", "(Ljava/lang/String;)V");
            env->DeleteLocalRef(clazz);
        }
    }

    ~JniListener() override {
        // Get the JNIEnv for the current thread
        JNIEnv* env = nullptr;
        bool attached = false;
        int status = jvm->GetEnv((void**)&env, JNI_VERSION_1_6);
        if (status == JNI_EDETACHED) {
            jvm->AttachCurrentThread((void**)&env, nullptr);
            attached = true;
        }

        if (env != nullptr) {
            env->DeleteWeakGlobalRef(listenerRef);
        }

        if (attached) {
            jvm->DetachCurrentThread();
        }
    }

    void print(const char* msg) override {
        JNIEnv* env = nullptr;
        bool attached = false;

        // Attach current thread to JVM if not already attached.
        // Native threads (like our worker thread) are not automatically
        // attached to the JVM — we must do it explicitly.
        int status = jvm->GetEnv((void**)&env, JNI_VERSION_1_6);
        if (status == JNI_EDETACHED) {
            if (jvm->AttachCurrentThread((void**)&env, nullptr) != JNI_OK) {
                fprintf(stderr, "[NativeCallbacks] Failed to attach thread\n");
                return;
            }
            attached = true;
        }

        if (env == nullptr || printMethod == nullptr) {
            if (attached) jvm->DetachCurrentThread();
            return;
        }

        // Create a local reference from the weak global reference.
        // If the Java object has been GC'd, this returns nullptr.
        jobject localRef = env->NewLocalRef(listenerRef);
        if (localRef != nullptr) {
            jstring jMsg = env->NewStringUTF(msg);
            env->CallVoidMethod(localRef, printMethod, jMsg);
            env->DeleteLocalRef(jMsg);
            env->DeleteLocalRef(localRef);
        } else {
            fprintf(stderr, "[NativeCallbacks] Listener was garbage collected\n");
        }

        if (attached) {
            jvm->DetachCurrentThread();
        }
    }
};

extern "C" {

// Called when the native library is loaded. Store the JavaVM pointer.
JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM* vm, void* reserved) {
    g_jvm = vm;
    return JNI_VERSION_1_6;
}

JNIEXPORT jlong JNICALL
Java_com_example_nativecallbacks_Native_initNativeClass(JNIEnv* env, jobject thiz) {
    return (jlong) new NativeClass();
}

JNIEXPORT void JNICALL
Java_com_example_nativecallbacks_Native_destroyNativeClass(JNIEnv* env, jobject thiz, jlong cppClass) {
    delete (NativeClass*) cppClass;
}

JNIEXPORT void JNICALL
Java_com_example_nativecallbacks_Native_nativeClassScan(JNIEnv* env, jobject thiz, jlong cppClass) {
    ((NativeClass*) cppClass)->scan();
}

JNIEXPORT void JNICALL
Java_com_example_nativecallbacks_Native_nativeClassStop(JNIEnv* env, jobject thiz, jlong cppClass) {
    ((NativeClass*) cppClass)->stop();
}

// Set the listener: creates a JniListener bridge that calls back into Kotlin
JNIEXPORT void JNICALL
Java_com_example_nativecallbacks_Native_nativeClassSetListener(
        JNIEnv* env, jobject thiz, jlong cppClass, jobject listener) {
    ((NativeClass*) cppClass)->setListener(new JniListener(g_jvm, env, listener));
}

} // extern "C"

#pragma once

#include <cstdio>

// Simple C++ class that we want to wrap in Kotlin via JNI
// Based on: https://habr.com/ru/articles/1017486/ (Part 1)
class NativeClass {
public:
    NativeClass() {
        printf("[NativeClass] created\n");
    }

    ~NativeClass() {
        printf("[NativeClass] destroyed\n");
    }

    void fun() {
        printf("[NativeClass] Hello from native\n");
        fflush(stdout);
    }
};

# Kotlin JNI Example

A minimal VSCode project demonstrating how to call C++ code from Kotlin using JNI (Java Native Interface), based on [this article](https://matt-moore.medium.com/kotlin-jni-for-native-code-835e93af7ddf).

## Project Structure

```
kotlin-jni-example/
├── .vscode/
│   ├── tasks.json          # VSCode build tasks
│   └── extensions.json     # Recommended extensions
├── src/
│   ├── Main.kt             # Kotlin entry point
│   ├── NativeSample.kt     # Kotlin class with `external` JNI method
│   ├── NativeSample.h      # JNI C header declaration
│   └── hello.cpp           # C++ implementation
├── build.sh                # One-shot build & run script
└── README.md
```

## Prerequisites

| Tool | Install |
|------|---------|
| JDK 11+ | `sudo apt install default-jdk` |
| Kotlin compiler | `sudo snap install --classic kotlin` |
| GCC/G++ | `sudo apt install build-essential` |

## How to Build & Run

### Option A – VSCode Task
Press **Ctrl+Shift+B** (or **⌘+Shift+B** on macOS) to run the default **Build & Run (JNI)** task.

### Option B – Terminal
```bash
bash build.sh
```

Expected output:
```
==> [1/3] Compiling Kotlin sources...
==> [2/3] Compiling C++ shared library...
==> [3/3] Running...
Hello from C++!
```

## How It Works

1. **`NativeSample.kt`** declares an `external fun sayHello()` and loads the native library with `System.loadLibrary("hello")`.
2. **`build.sh`** compiles the Kotlin sources into a JAR, then compiles `hello.cpp` into a shared library (`libhello.so` / `libhello.dylib`).
3. The JVM is launched with `-Djava.library.path=build/` so it can find `libhello`.
4. When `sayHello()` is called, JNI routes it to `Java_NativeSample_sayHello` in the C++ library.

## JNI Naming Convention

The C++ function name must follow the pattern:

```
Java_<ClassName>_<methodName>
```

So `NativeSample.sayHello()` maps to `Java_NativeSample_sayHello`.

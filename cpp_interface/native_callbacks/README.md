# native_callbacks

Standalone Kotlin/JVM + C++ project demonstrating native-to-Kotlin callbacks via JNI (no Android required).

Based on [habr.com/ru/articles/1017486](https://habr.com/ru/articles/1017486/) — Parts 2 & 3.

## Overview

A C++ worker thread runs in the background and periodically calls back into Kotlin via a `NativeListener` interface.

**Key ideas:**
- `JNI_OnLoad` stores the `JavaVM*` pointer so native threads can access the JVM
- Native threads must call `AttachCurrentThread` before invoking Java/Kotlin methods
- Use weak global references for the listener to cooperate with the GC
- Cache `jmethodID` for callback methods to avoid repeated lookups
- Keep a strong reference to the listener on the Kotlin side to prevent premature GC

```
C++ worker thread  ──── JniListener ────►  Kotlin NativeListener
   (background)         (JNI bridge)          (callback interface)
```

## Prerequisites
* VSCode
* [fwcd.kotlin VSCode extension](https://marketplace.visualstudio.com/items?itemName=fwcd.kotlin)
* CMake 3.10+
* GCC or Clang compiler
* GDB debugger (for C++ debugging)

## Tested on
* Gradle 8.14.4 (Snap package)
* Kotlin 2.3.10 (Snap package)
* CMake 3.28.3
* GCC 13.3.0
* Ubuntu 24.04.4 LTS

## Create a Kotlin sample app
```bash
mkdir simple_gradle_project
cd simple_gradle_project
gradle init --type=kotlin-application

# Continue? Yes
# Enter target Java version: Default 21
# Project name (default: simple_gradle_project)
# Select application structure: Single application project
# Select build script DSL: Kotlin
# Select test framework: kotlin.test
# Generate build using new APIs and behavior: no
 
# Have a try is everything is ok
% ./gradlew build
```

## List of generated files and folders
```
	.gitattributes
	.gitignore
	app/
	gradle.properties
	gradle/
	gradlew
	gradlew.bat
	settings.gradle.kts
```


## Settings to build and debug
File: `.vscode/tasks.json`
```json
{
    "version": "2.0.0",
    "tasks": [
        {
            "label": "build",
            "type": "shell",
            "command": "./gradlew build -x test --no-configuration-cache",
            "problemMatcher": [],
            "group": {
                "kind": "build",
                "isDefault": true
            }
        },
        {
            "label": "run",
            "type": "shell",
            "command": "./gradlew run --no-configuration-cache",
            "problemMatcher": []
        },
        {
            "label": "test",
            "type": "shell",
            "command": "./gradlew test --no-configuration-cache",
            "problemMatcher": []
        },
        {
            "label": "buildNativeDebug",
            "type": "shell",
            "command": "cd ${workspaceFolder} && mkdir -p build/native_debug && cd build/native_debug && cmake -DCMAKE_BUILD_TYPE=Debug ${workspaceFolder}/src/main/cpp && cmake --build . --config Debug",
            "problemMatcher": ["$gcc"],
            "group": {
                "kind": "build"
            },
            "presentation": {
                "reveal": "always"
            }
        },
        {
            "label": "buildCppDebug",
            "dependsOrder": "sequence",
            "dependsOn": ["buildNativeDebug", "installDist"],
            "group": {
                "kind": "build"
            }
        },
        {
            "label": "installDist",
            "type": "shell",
            "command": "./gradlew installDist -x test --no-configuration-cache",
            "problemMatcher": []
        }
    ]
}
```
* In vscode menu, Terminal -> Run Task… 
* You can execute gradlew’s build / run / test tasks.

## Debug settings
File: `.vscode/launch.json`

### Kotlin Debug Configuration
```json
{
    "type": "kotlin",
    "request": "launch",
    "name": "Kotlin Launch",
    "projectRoot": "${workspaceFolder}",
    "mainClass": "com/example/nativecallbacks/MainKt",
    "vmArguments": "-Djava.library.path=/path/to/build/native",
    "preLaunchTask": "build"
}
```

> **Note:** The `vmArguments` property (used by the `fwcd.kotlin` extension) does not expand `${workspaceFolder}`. Use the absolute path to `build/native` where `libnativecallbacks.so` is produced.

### C++ Debug Configuration

Debugs native C++ code by launching the JVM directly under GDB. The `buildCppDebug` pre-launch task runs `buildNativeDebug` (CMake debug build) then `installDist` (produces the classpath JARs).

GDB must pass `SIGSEGV`/`SIGBUS` to the JVM, which uses them internally for JIT compilation and safepoints.

```json
{
    "type": "cppdbg",
    "name": "C++ Debug",
    "request": "launch",
    "program": "/usr/lib/jvm/java-21-openjdk-amd64/bin/java",
    "args": [
        "-Djava.library.path=${workspaceFolder}/build/native_debug",
        "-cp",
        "${workspaceFolder}/build/install/native_callbacks/lib/native_callbacks.jar:${workspaceFolder}/build/install/native_callbacks/lib/kotlin-stdlib-1.9.22.jar:${workspaceFolder}/build/install/native_callbacks/lib/annotations-13.0.jar",
        "com.example.nativecallbacks.MainKt"
    ],
    "stopAtEntry": false,
    "cwd": "${workspaceFolder}",
    "environment": [],
    "externalConsole": false,
    "MIMode": "gdb",
    "setupCommands": [
        {
            "description": "Enable pretty-printing for gdb",
            "text": "-enable-pretty-printing",
            "ignoreFailures": true
        },
        {
            "description": "Pass SIGSEGV to the JVM (used for JIT/safepoints)",
            "text": "handle SIGSEGV pass noprint nostop",
            "ignoreFailures": true
        },
        {
            "description": "Pass SIGBUS to the JVM",
            "text": "handle SIGBUS pass noprint nostop",
            "ignoreFailures": true
        }
    ],
    "preLaunchTask": "buildCppDebug"
}
```

> **Note:** The `-cp` wildcard (`lib/*`) is not expanded by GDB since it launches `java` directly without a shell. List all JARs explicitly with `:` separators. Update if dependencies change (re-run `./gradlew installDist` and check `build/install/native_callbacks/lib/`).

## Build and Run

```bash
./gradlew run
# Press Enter to stop the scan
```

The Gradle build automatically compiles the C++ shared library with CMake before running.

## Build native library only

```bash
cd src/main/cpp
mkdir -p build && cd build
cmake .. -DCMAKE_BUILD_TYPE=Release
cmake --build .
```

## Project Structure

```
native_callbacks/
├── build.gradle.kts          # Gradle build (compiles C++ and runs Kotlin)
├── settings.gradle.kts       # Standalone Gradle settings
├── gradlew / gradlew.bat     # Gradle wrapper
└── src/main/
    ├── cpp/
    │   ├── CMakeLists.txt    # CMake build for the shared library
    │   ├── NativeClass.h     # C++ class with Listener interface and worker thread
    │   └── native-lib.cpp    # JNI bridge: JniListener, JNI_OnLoad, JNI functions
    └── java/com/example/nativecallbacks/
        ├── Native.kt         # JNI declarations (external functions)
        ├── NativeClass.kt    # Kotlin wrapper that holds the C++ pointer
        ├── NativeListener.kt # Kotlin interface for callbacks
        └── Main.kt           # Entry point (interactive: press Enter to stop)
```

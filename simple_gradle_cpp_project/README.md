# Simple Gradle + C++ Project

A Kotlin application with integrated C++ native library and CMake build support.

## Requires
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
            "command": "./gradlew build -x test",
            "problemMatcher": [],
            "group": {
                "kind": "build",
                "isDefault": true
            }
        },
        {
            "label": "run",
            "type": "shell",
            "command": "./gradlew run",
            "problemMatcher": []
        },
        {
            "label": "test",
            "type": "shell",
            "command": "./gradlew test",
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
    "projectRoot": "${workspaceFolder}/app",
    "mainClass": "org/example/AppKt",
    "preLaunchTask": "build"
}
```

### C++ Debug Configuration
```json
{
    "type": "cppdbg",
    "name": "C++ Debug (Calculator)",
    "request": "launch",
    "program": "${workspaceFolder}/build/native_debug/calculator_test",
    "args": [],
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
        }
    ],
    "preLaunchTask": "buildNativeDebug"
}
```

## C++ Native Library

### Structure
- **CMakeLists.txt** - CMake configuration for building C++ code
- **app/src/main/cpp/Calculator.h** - C++ Calculator interface and implementation
- **app/src/main/cpp/Calculator.cpp** - Calculator implementation
- **app/src/main/cpp/CalculatorTest.cpp** - Test executable for debugging

### Building C++ Code

#### Release Build
```bash
./gradlew buildNative
```
Builds the C++ library in Release mode (optimizations enabled).

#### Debug Build
```bash
./gradlew buildNativeDebug
```
or use the VS Code task:
- Terminal → Run Task → buildNativeDebug

Builds the C++ library with debug symbols for stepping through code.

### Debugging C++ Code

1. Open a C++ source file (e.g., `app/src/main/cpp/Calculator.cpp`)
2. Set breakpoints by clicking on line numbers
3. Go to the VS Code Debug view (Ctrl+Shift+D)
4. Select **"C++ Debug (Calculator)"** from the debug configuration dropdown
5. Press F5 or click the Start Debugging button
6. The test executable will build and start under GDB debugger

You can now:
- Step through code with F10 (step over) and F11 (step into)
- Inspect variables in the Debug panel
- Set conditional breakpoints
- Evaluate expressions in the Debug Console

## References

### Kotlin & Gradle
* [Debugging Kotlin program using VSCode](https://medium.com/@thunderz99/debugging-kotlin-program-using-vscode-318ed43fe2f0)
* <https://github.com/thunderz99/kt-sample-app>
* [Get started with Gradle and Kotlin/JVM](https://kotlinlang.org/docs/get-started-with-jvm-gradle-project.html)
* [Gradle best practices﻿](https://kotlinlang.org/docs/gradle-best-practices.html)

### C++ & CMake
* [CMake Documentation](https://cmake.org/documentation/)
* [VS Code C++ Debugging with GDB](https://code.visualstudio.com/docs/cpp/launch-json-reference)
* [CMake C/C++ Project Setup](https://cmake.org/cmake/help/latest/guide/tutorial/index.html)

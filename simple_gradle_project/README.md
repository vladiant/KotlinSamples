# Simple Gradle Project

## Requires
* VSCode
* [fwcd.kotlin VSCode extension](https://marketplace.visualstudio.com/items?itemName=fwcd.kotlin)

## Tested on
* Gradle 8.14.4 (Snap package)
* Kotlin 2.3.10 (Snap package)
* Ubuntu Ubuntu 24.04.4 LTS

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
```
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "kotlin",
            "request": "launch",
            "name": "Kotlin Launch",
            "projectRoot": "${workspaceFolder}/app",
            "mainClass": "org/example/AppKt",
            "preLaunchTask": "build"
        }
    ]
}
```

## References
* [Debugging Kotlin program using VSCode](https://medium.com/@thunderz99/debugging-kotlin-program-using-vscode-318ed43fe2f0)
* <https://github.com/thunderz99/kt-sample-app>

# Simple VSCode Project

## Application

### Compile
```
kotlinc main.kt -include-runtime -d main.jar
```

### Run
```
java -jar main.jar
```

## Library

### Compile
```
kotlinc main.kt -d main.jar
```

### Run
```
kotlin -classpath main.jar MainKt
```

## References
* [Kotlin: Create and run an application](https://kotlinlang.org/docs/command-line.html#create-and-run-an-application)

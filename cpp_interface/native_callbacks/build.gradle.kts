plugins {
    kotlin("jvm") version "1.9.22"
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.example.nativecallbacks.MainKt")
}

// Task to build the native shared library using CMake
val nativeBuildDir = layout.buildDirectory.dir("native")
val cppSrcDir = file("src/main/cpp")

tasks.register<Exec>("buildNative") {
    val buildDir = nativeBuildDir.get().asFile
    doFirst { buildDir.mkdirs() }
    workingDir(buildDir)
    commandLine("sh", "-c",
        "cmake ${cppSrcDir.absolutePath} -DCMAKE_BUILD_TYPE=Release && cmake --build .")
}

tasks.named<JavaExec>("run") {
    dependsOn("buildNative")
    jvmArgs("-Djava.library.path=${nativeBuildDir.get().asFile.absolutePath}")
    // Allow stdin for interactive stop
    standardInput = System.`in`
}

sourceSets {
    main {
        kotlin.srcDir("src/main/java")
    }
}

plugins {
    kotlin("jvm") version "2.1.20"
    application
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.example.simplewrapper.MainKt")
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
    // Add the native lib directory to java.library.path
    jvmArgs("-Djava.library.path=${nativeBuildDir.get().asFile.absolutePath}")
}

sourceSets {
    main {
        kotlin.srcDir("src/main/java")
    }
}

cmake_minimum_required(VERSION 3.22.1)

project("nativecallbacks")

find_package(JNI REQUIRED COMPONENTS JVM)
find_package(Threads REQUIRED)

add_library(nativecallbacks SHARED
    native-lib.cpp
)

target_include_directories(nativecallbacks PRIVATE ${JNI_INCLUDE_DIRS})
target_link_libraries(nativecallbacks Threads::Threads)
target_compile_features(nativecallbacks PRIVATE cxx_std_17)

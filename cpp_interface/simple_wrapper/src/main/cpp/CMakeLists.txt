cmake_minimum_required(VERSION 3.22.1)

project("simplewrapper")

find_package(JNI REQUIRED COMPONENTS JVM)

add_library(simplewrapper SHARED
    native-lib.cpp
)

target_include_directories(simplewrapper PRIVATE ${JNI_INCLUDE_DIRS})
target_compile_features(simplewrapper PRIVATE cxx_std_17)

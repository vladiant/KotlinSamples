cmake_minimum_required(VERSION 3.10)
project(calculator)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Add debug symbols
set(CMAKE_CXX_FLAGS_DEBUG "-g -O0")
set(CMAKE_CXX_FLAGS_RELEASE "-O3 -g")

# Add the library
add_library(calculator STATIC
    app/src/main/cpp/Calculator.cpp
    app/src/main/cpp/Calculator.h
)

# Include directories
target_include_directories(calculator PUBLIC 
    ${CMAKE_CURRENT_SOURCE_DIR}/app/src/main/cpp
)

# Add debug test executable
add_executable(calculator_test
    app/src/main/cpp/CalculatorTest.cpp
)

target_link_libraries(calculator_test PRIVATE calculator)

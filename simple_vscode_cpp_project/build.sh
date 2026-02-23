#!/usr/bin/env bash
set -e

# ─── Configuration ─────────────────────────────────────────────────────────────
# Auto-detect JAVA_HOME if not set
if [ -z "$JAVA_HOME" ]; then
    if command -v java &>/dev/null; then
        JAVA_HOME=$(java -XshowSettings:all -version 2>&1 \
            | grep "java.home" | awk '{print $3}')
        # Some JDKs report jre sub-directory; walk up if needed
        [ -d "$JAVA_HOME/include" ] || JAVA_HOME="$(dirname "$JAVA_HOME")"
    fi
fi

if [ -z "$JAVA_HOME" ] || [ ! -d "$JAVA_HOME/include" ]; then
    echo "ERROR: Could not find JAVA_HOME with a valid include/ directory."
    echo "Set JAVA_HOME manually, e.g.:"
    echo "  export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64"
    exit 1
fi

echo "Using JAVA_HOME: $JAVA_HOME"

# Detect OS for the platform-specific JNI include dir and shared-lib extension
OS="$(uname -s)"
case "$OS" in
    Linux*)  JNI_PLATFORM="linux"; LIB_EXT="so" ;;
    Darwin*) JNI_PLATFORM="darwin"; LIB_EXT="dylib" ;;
    *)       echo "Unsupported OS: $OS"; exit 1 ;;
esac

# ─── Directories ───────────────────────────────────────────────────────────────
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SRC="$SCRIPT_DIR/src"
BUILD="$SCRIPT_DIR/build"
mkdir -p "$BUILD"

# ─── Step 1: Compile Kotlin → JAR ──────────────────────────────────────────────
echo ""
echo "==> [1/3] Compiling Kotlin sources..."
kotlinc -include-runtime -d "$BUILD/NativeSample.jar" "$SRC/NativeSample.kt" "$SRC/Main.kt"

# ─── Step 2: Compile C++ → shared library ──────────────────────────────────────
echo ""
echo "==> [2/3] Compiling C++ shared library..."
g++ "$SRC/hello.cpp" \
    -o "$BUILD/libhello.$LIB_EXT" \
    -shared -fPIC \
    -I "$JAVA_HOME/include" \
    -I "$JAVA_HOME/include/$JNI_PLATFORM"

# ─── Step 3: Run ───────────────────────────────────────────────────────────────
echo ""
echo "==> [3/3] Running..."
java -Djava.library.path="$BUILD" -jar "$BUILD/NativeSample.jar"

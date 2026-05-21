#!/usr/bin/env bash
#
# build_apk.sh — Build CodePad Android APK from terminal (no Android Studio needed)
#
# Usage:
#   ./build_apk.sh           # Debug APK
#   ./build_apk.sh release   # Release APK (unsigned)
#   ./build_apk.sh clean     # Clean build artifacts
#
# Requirements: Java 17+ (auto-detected), internet connection (first run downloads ~500 MB)
# Tested on: Ubuntu/Debian, macOS, WSL2
#

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

# ─── Configuration ────────────────────────────────────────────────────────────

ANDROID_SDK_ROOT="${ANDROID_SDK_ROOT:-$HOME/.android-sdk}"
CMDLINE_TOOLS_VERSION="11076708"   # Latest command-line tools (Jan 2025)
GRADLE_VERSION="8.9"
BUILD_TOOLS_VERSION="35.0.0"
COMPILE_SDK="35"
PLATFORM="android-${COMPILE_SDK}"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

info()  { echo -e "${CYAN}[INFO]${NC} $*"; }
ok()    { echo -e "${GREEN}[OK]${NC} $*"; }
warn()  { echo -e "${YELLOW}[WARN]${NC} $*"; }
fail()  { echo -e "${RED}[ERROR]${NC} $*"; exit 1; }

# ─── Step 1: Check Java ──────────────────────────────────────────────────────

check_java() {
    info "Checking Java..."
    if ! command -v java &>/dev/null; then
        fail "Java not found. Install JDK 17+:
  Ubuntu/Debian: sudo apt install openjdk-17-jdk
  macOS:         brew install openjdk@17
  Fedora:        sudo dnf install java-17-openjdk-devel"
    fi

    JAVA_VER=$(java -version 2>&1 | head -1 | sed -E 's/.*"([0-9]+).*/\1/')
    if [ "$JAVA_VER" -lt 17 ] 2>/dev/null; then
        fail "Java 17+ required (found Java $JAVA_VER). Please upgrade."
    fi
    ok "Java $JAVA_VER found"
}

# ─── Step 2: Install Android SDK (if missing) ────────────────────────────────

install_android_sdk() {
    export ANDROID_HOME="$ANDROID_SDK_ROOT"
    export ANDROID_SDK_ROOT="$ANDROID_SDK_ROOT"
    export PATH="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools:$PATH"

    if command -v sdkmanager &>/dev/null; then
        ok "Android SDK already installed"
        return
    fi

    info "Android SDK not found at $ANDROID_SDK_ROOT — downloading..."

    # Detect OS
    case "$(uname -s)" in
        Linux*)   OS="linux" ;;
        Darwin*)  OS="mac" ;;
        CYGWIN*|MINGW*|MSYS*) OS="win" ;;
        *)        fail "Unsupported OS: $(uname -s)" ;;
    esac

    CMDLINE_URL="https://dl.google.com/android/repository/commandlinetools-${OS}-${CMDLINE_TOOLS_VERSION}_latest.zip"
    TEMP_ZIP="/tmp/android-cmdline-tools.zip"

    info "Downloading Android command-line tools..."
    if command -v curl &>/dev/null; then
        curl -fSL -o "$TEMP_ZIP" "$CMDLINE_URL"
    elif command -v wget &>/dev/null; then
        wget -q -O "$TEMP_ZIP" "$CMDLINE_URL"
    else
        fail "Neither curl nor wget found. Install one of them first."
    fi

    mkdir -p "$ANDROID_SDK_ROOT/cmdline-tools"
    unzip -qo "$TEMP_ZIP" -d "$ANDROID_SDK_ROOT/cmdline-tools"
    mv "$ANDROID_SDK_ROOT/cmdline-tools/cmdline-tools" "$ANDROID_SDK_ROOT/cmdline-tools/latest" 2>/dev/null || true
    rm -f "$TEMP_ZIP"

    ok "Android command-line tools installed"
}

# ─── Step 3: Accept licenses & install required SDK packages ─────────────────

setup_sdk_packages() {
    info "Accepting Android SDK licenses..."
    yes 2>/dev/null | sdkmanager --licenses > /dev/null 2>&1 || true

    info "Installing required SDK packages (this may take a few minutes on first run)..."
    sdkmanager --install \
        "platforms;$PLATFORM" \
        "build-tools;$BUILD_TOOLS_VERSION" \
        "platform-tools" \
        > /dev/null 2>&1

    ok "SDK packages ready"
}

# ─── Step 4: Set up Gradle wrapper (gradlew + jar) ───────────────────────────

setup_gradle_wrapper() {
    GRADLE_WRAPPER_JAR="$SCRIPT_DIR/gradle/wrapper/gradle-wrapper.jar"

    if [ -f "$SCRIPT_DIR/gradlew" ] && [ -f "$GRADLE_WRAPPER_JAR" ]; then
        chmod +x "$SCRIPT_DIR/gradlew"
        ok "Gradle wrapper already present"
        return
    fi

    info "Setting up Gradle $GRADLE_VERSION wrapper..."
    GRADLE_DIST_URL="https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip"
    GRADLE_TEMP="/tmp/gradle-${GRADLE_VERSION}-bin.zip"
    GRADLE_EXTRACT="/tmp/gradle-${GRADLE_VERSION}"

    if [ ! -d "$GRADLE_EXTRACT" ] || [ ! -x "$GRADLE_EXTRACT/bin/gradle" ]; then
        info "Downloading Gradle $GRADLE_VERSION..."
        if command -v curl &>/dev/null; then
            curl -fSL -o "$GRADLE_TEMP" "$GRADLE_DIST_URL"
        else
            wget -q -O "$GRADLE_TEMP" "$GRADLE_DIST_URL"
        fi
        rm -rf "$GRADLE_EXTRACT"
        unzip -qo "$GRADLE_TEMP" -d "/tmp/"
        rm -f "$GRADLE_TEMP"
    fi

    info "Generating gradlew wrapper..."
    "$GRADLE_EXTRACT/bin/gradle" wrapper --gradle-version "$GRADLE_VERSION"
    chmod +x "$SCRIPT_DIR/gradlew"

    # Clean up the full distribution
    rm -rf "$GRADLE_EXTRACT"
    ok "Gradle wrapper ready"
}

# ─── Step 5: Build APK ───────────────────────────────────────────────────────

build_apk() {
    local BUILD_TYPE="${1:-debug}"

    info "Writing local.properties..."
    echo "sdk.dir=$ANDROID_SDK_ROOT" > "$SCRIPT_DIR/local.properties"

    case "$BUILD_TYPE" in
        release)
            info "Building RELEASE APK..."
            ./gradlew assembleRelease --no-daemon --warning-mode=all
            APK_PATH="app/build/outputs/apk/release/app-release-unsigned.apk"
            ;;
        clean)
            info "Cleaning build artifacts..."
            ./gradlew clean --no-daemon
            ok "Build cleaned!"
            exit 0
            ;;
        *)
            info "Building DEBUG APK..."
            ./gradlew assembleDebug --no-daemon --warning-mode=all
            APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
            ;;
    esac

    if [ -f "$APK_PATH" ]; then
        APK_SIZE=$(du -sh "$APK_PATH" | cut -f1)
        echo ""
        echo -e "${GREEN}========================================${NC}"
        echo -e "${GREEN}  APK built successfully!${NC}"
        echo -e "${GREEN}========================================${NC}"
        echo -e "  Path: ${CYAN}$SCRIPT_DIR/$APK_PATH${NC}"
        echo -e "  Size: ${CYAN}$APK_SIZE${NC}"
        echo -e "  Type: ${CYAN}$BUILD_TYPE${NC}"
        echo ""
        echo -e "  Install on device:"
        echo -e "  ${YELLOW}adb install $APK_PATH${NC}"
        echo ""
    else
        fail "APK not found at expected path: $APK_PATH"
    fi
}

# ─── Main ─────────────────────────────────────────────────────────────────────

main() {
    echo ""
    echo -e "${CYAN}╔═══════════════════════════════════════╗${NC}"
    echo -e "${CYAN}║     CodePad Android APK Builder       ║${NC}"
    echo -e "${CYAN}╚═══════════════════════════════════════╝${NC}"
    echo ""

    check_java
    install_android_sdk
    setup_sdk_packages
    setup_gradle_wrapper
    build_apk "${1:-debug}"
}

main "$@"

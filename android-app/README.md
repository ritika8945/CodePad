# CodePad Android

A lightweight, highly optimized native Android app for **CodePad** — the multi-language online code editor & execution platform.

## Features

- **24+ Language Support** with complete abbreviation mapping (js, py, cpp, rs, kt, rb, etc.)
- **Same UI Style** as the CodePad web app — dark theme, code editor with line numbers, input panel, console output
- **Tab Navigation** (Editor / Input / Console) on phones, side-by-side layout on tablets
- **Real-time Code Execution** via the CodePad backend API
- **Configurable Server URL** — connect to any CodePad instance
- **Material Design 3** with Jetpack Compose
- **Edge-to-edge** display support
- **Lightweight** — minified + shrunk release build (~3 MB)
- **Works on all Android devices** — minSdk 24 (Android 7.0+), targetSdk 35

## Supported Languages

| Language | Abbreviations |
|----------|--------------|
| JavaScript | js, jsx, node, nodejs, ecmascript, es6 |
| TypeScript | ts, tsx |
| Python | py, python3, py3, pypy |
| Java | jav |
| C | c-lang |
| C++ | cpp, cxx, cplusplus, cc |
| C# | csharp, cs, c-sharp, dotnet |
| Go | golang |
| Rust | rs, rustlang |
| PHP | php7, php8 |
| Ruby | rb |
| Kotlin | kt, kts, kotlinscript |
| R | rlang, r-lang, rscript |
| Bash | shell, sh, zsh, ksh, shellscript |
| Swift | swiftlang |
| Dart | dartlang, flutter |
| Scala | sc |
| Perl | pl, perl5, perl6 |
| Lua | luajit |
| Haskell | hs, ghc |
| Elixir | ex, exs, iex |
| Clojure | clj, cljs, cljc |
| Groovy | gvy, gy |
| SQL | mysql, postgresql, postgres, sqlite |

## Tech Stack

- **Kotlin** + **Jetpack Compose**
- **Material Design 3**
- **OkHttp** for networking
- **Kotlin Coroutines** for async operations
- **MVVM** architecture with StateFlow

## Build from Terminal (No Android Studio Needed)

Just run one command — the script handles everything (Java check, Android SDK download, Gradle setup):

```bash
cd android-app

# Build debug APK
./build_apk.sh

# Build release APK
./build_apk.sh release

# Clean build artifacts
./build_apk.sh clean
```

**What the script does automatically:**
1. Checks Java 17+ is installed
2. Downloads Android SDK command-line tools (~150 MB, first run only)
3. Installs required SDK packages (platform, build-tools)
4. Downloads and sets up Gradle wrapper
5. Builds the APK

**Output:** The APK will be at:
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release-unsigned.apk`

**Install on device:**
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Build with Android Studio (Alternative)

```bash
# Open android-app/ folder in Android Studio, then:
./gradlew assembleDebug
./gradlew assembleRelease
```

## Architecture

```
app/src/main/java/com/codepad/app/
├── CodePadApp.kt              # Application class
├── data/
│   ├── model/
│   │   ├── Language.kt         # Language definitions + abbreviations
│   │   └── ExecutionResult.kt  # API response model
│   └── repository/
│       └── CodeExecutionRepository.kt  # API client
├── ui/
│   ├── MainActivity.kt         # Entry point
│   ├── theme/                   # Material 3 dark/light theme
│   ├── screens/
│   │   ├── CodePadScreen.kt    # Main screen (portrait/landscape)
│   │   └── CodePadViewModel.kt # UI state management
│   └── components/
│       ├── CodeEditor.kt       # Code editor with line numbers
│       ├── InputPanel.kt       # Program input
│       ├── ConsoleOutput.kt    # Execution output
│       ├── LanguageSelector.kt # Language dropdown
│       ├── TopBar.kt           # App bar with actions
│       └── SettingsDialog.kt   # Server URL config
└── util/                        # Utilities
```

## Requirements

**Terminal build (build_apk.sh):**
- JDK 17+ (`sudo apt install openjdk-17-jdk` / `brew install openjdk@17`)
- Internet connection (first run downloads Android SDK + Gradle)
- Linux, macOS, or WSL2

**Android Studio build:**
- Android Studio Hedgehog or later
- JDK 17
- Android SDK 35

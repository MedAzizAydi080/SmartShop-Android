# SmartShop

**SmartShop** is an Android application built with Kotlin and Jetpack Compose, featuring an Amazon-inspired UI theme. It integrates Firebase for backend services, specifically Authentication.

## Project Overview

*   **Type:** Android Application (Mobile)
*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose (Material 3)
*   **Architecture:** MVVM (Model-View-ViewModel) pattern is encouraged.
*   **Backend:** Firebase (Authentication, Firestore)

## Key Features

*   **Authentication:**
    *   **Sign In:** Email/Password login with Amazon-styled UI.
    *   **Sign Up:** Dedicated registration screen.
    *   **Forgot Password:** Feature to send password reset emails via Firebase.
*   **Navigation:**
    *   Implemented using `androidx.navigation.compose`.
    *   Current flows: `SignIn` -> `SignUp` -> `SignIn`.
*   **Theming:**
    *   Custom color palette mimicking Amazon branding (Dark Blue, Orange).
    *   Dark/Light mode support (currently enforced to Amazon theme colors).

## Tech Stack & Dependencies

*   **Android SDK:**
    *   `minSdk`: 24
    *   `targetSdk`: 36
    *   `compileSdk`: 36
*   **Build System:** Gradle (Kotlin DSL)
*   **Version Catalog:** Dependencies are managed in `gradle/libs.versions.toml`.
*   **Key Libraries:**
    *   `androidx.compose.material3`: Material Design 3 components.
    *   `androidx.navigation.compose`: Navigation for Compose.
    *   `com.google.firebase:firebase-auth`: User authentication.
    *   `com.google.firebase:firebase-firestore`: Cloud database.

## Building and Running

To build and run the application, use the standard Gradle commands:

```bash
# Build the debug APK
./gradlew assembleDebug

# Install and run on a connected device/emulator
./gradlew installDebug
```

## Directory Structure

*   `app/src/main/java/com/example/smartshop/`
    *   `MainActivity.kt`: Entry point, sets up Navigation Host.
    *   `ui/auth/`: Authentication screens (`SignInScreen.kt`, `SignUpScreen.kt`).
    *   `ui/theme/`: Theme definitions (`Color.kt`, `Theme.kt`, `Type.kt`).
*   `gradle/libs.versions.toml`: Centralized dependency management.
*   `app/google-services.json`: Firebase configuration file.

## Development Conventions

*   **UI:** All new UI components should be built using Jetpack Compose.
*   **Navigation:** Define new screens as composables and add them to the `NavHost` in `MainActivity.kt` (or a dedicated Navigation graph file if the app grows).
*   **State Management:** Use `remember` and `mutableStateOf` for local UI state. For complex business logic, introduce ViewModels.
*   **Firebase:** Use the `firebase_get_environment` tool to verify project connection.

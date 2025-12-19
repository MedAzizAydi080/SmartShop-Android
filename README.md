# SmartShop - Android Inventory Management App

SmartShop is a modern Android application built with **Kotlin** and **Jetpack Compose** designed for efficient product inventory management. It demonstrates best practices in modern Android development, including MVVM architecture, local persistence with Room, and real-time cloud synchronization with Firebase.

## 📱 Features

### **Product Management**
*   **Create, Read, Update, Delete (CRUD):** Manage your inventory with ease.
*   **Validation:** Ensures data integrity (e.g., Price > 0, Quantity ≥ 0).
*   **Amazon-Themed UI:** A clean, professional user interface inspired by e-commerce standards.

### **💾 Data Persistence**
*   **Local Storage (Room):** Works offline! All data is stored locally on the device using Room Database.
*   **Cloud Sync (Firebase Firestore):**
    *   **Real-time Synchronization:** Changes made on one device appear instantly on others.
    *   **Bi-directional Sync:** Supports offline mode with automatic sync when online.

### **📊 Analytics & Export**
*   **Dashboard:** Visualize total stock count and total value in **TND (Tunisian Dinar)**.
*   **Charts:** Interactive Pie Chart showing stock value distribution.
*   **CSV Export:** Export your entire inventory to a CSV file for external use.

### **🔐 Authentication**
*   **Secure Sign-In/Sign-Up:** Integrated with Firebase Authentication (Email/Password).

## 🛠 Tech Stack

*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose (Material Design 3)
*   **Architecture:** MVVM (Model-View-ViewModel)
*   **Asynchronous Programming:** Coroutines & Flows
*   **Local Database:** Android Room
*   **Backend:** Firebase (Firestore, Auth)
*   **Dependency Injection:** Manual / ViewModel Factory

## 🚀 Getting Started

### Prerequisites
*   Android Studio Ladybug (or newer)
*   JDK 17+

### Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/azizaydi/SmartShop.git
    ```
2.  **Open in Android Studio:**
    Open the project folder.
3.  **Firebase Setup:**
    *   The project expects a `google-services.json` file in the `app/` directory.
    *   Ensure your Firebase project has Firestore and Authentication enabled.
4.  **Build and Run:**
    ```bash
    ./gradlew installDebug
    ```

## 📸 Screenshots
*(Add screenshots here)*

## 📄 License
This project is for educational purposes.

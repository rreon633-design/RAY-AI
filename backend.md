# RAY AI — Local Backend & C++ JNI Inference Engine

## 1. On-Device Backend Philosophy

Unlike standard AI applications that rely on cloud APIs (OpenAI, Gemini REST APIs), RAY AI features a **100% local, on-device execution engine** running entirely on modern ARM64 Android hardware.

---

## 2. JNI & GGML Execution Core

### Architecture Details
- **Engine Bindings**: JNI (Java Native Interface) bridge linking Kotlin runtime to GGML C/C++ libraries.
- **Precision**: 4-bit Medium Quantization (`Q4_K_M` / `Q4_0`) allowing 2B - 7B parameter models to execute within 1.5 GB - 3.5 GB of CPU RAM.
- **ARM NEON & Dot Product Acceleration**: Utilizes ARMv8.2-A SDOT/UDOT instructions for hardware-accelerated integer matrix vector multiplication.
- **Multithreaded OpenMP Execution**: Dynamically maps inference tasks across 1 to 8 physical ARM cores (Performance vs. Efficiency cores).

```
   +-------------------------------------------------------------+
   |                     Kotlin Coroutines Flow                   |
   +------------------------------+------------------------------+
                                  | Native JNI Call
                                  v
   +-------------------------------------------------------------+
   |                  c++ JNI Bridge (localai.cpp)                |
   +------------------------------+------------------------------+
                                  | ggml_graph_compute()
                                  v
   +-------------------------------------------------------------+
   |                 GGML C Runtime Engine                        |
   |   - GGUF File Loader (mmap)                                  |
   |   - KV Cache Manager                                         |
   |   - ARM NEON SIMD Matrix Vector Multiplier                   |
   +-------------------------------------------------------------+
```

---

## 3. Supported Model Formats

| Family | Model Name | Quantization | Size on Disk | RAM Required | Typical Speed (Snapdragon 8 Gen 2) |
|---|---|---|---|---|---|
| **Qwen** | Qwen 2.5 0.5B Instruct | Q4_K_M | ~390 MB | ~600 MB | 38.5 tok/s |
| **Gemma** | Gemma 2 2B IT | Q4_0 | ~1.4 GB | ~1.8 GB | 18.2 tok/s |
| **MiniCPM** | MiniCPM3 4B | Q4_K_M | ~2.1 GB | ~2.6 GB | 12.4 tok/s |
| **Llama** | Llama 3.2 1B Instruct | Q4_K_M | ~750 MB | ~1.1 GB | 28.6 tok/s |

---

## 4. Local Data Persistence Engine (Room)

```sql
-- Chat Sessions Table
CREATE TABLE IF NOT EXISTS `chat_sessions` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `title` TEXT NOT NULL,
    `modelName` TEXT NOT NULL,
    `createdAt` INTEGER NOT NULL,
    `updatedAt` INTEGER NOT NULL
);

-- Chat Messages Table
CREATE TABLE IF NOT EXISTS `chat_messages` (
    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `sessionId` INTEGER NOT NULL,
    `role` TEXT NOT NULL,
    `content` TEXT NOT NULL,
    `timestamp` INTEGER NOT NULL,
    FOREIGN KEY(`sessionId`) REFERENCES `chat_sessions`(`id`) ON DELETE CASCADE
);

-- Models Table
CREATE TABLE IF NOT EXISTS `local_models` (
    `id` TEXT PRIMARY KEY NOT NULL,
    `name` TEXT NOT NULL,
    `downloadUrl` TEXT NOT NULL,
    `sizeBytes` INTEGER NOT NULL,
    `isDownloaded` INTEGER NOT NULL,
    `downloadProgress` REAL NOT NULL,
    `quantization` TEXT NOT NULL,
    `category` TEXT NOT NULL
);

-- Settings Table
CREATE TABLE IF NOT EXISTS `app_settings` (
    `id` INTEGER PRIMARY KEY NOT NULL,
    `cpuThreads` INTEGER NOT NULL,
    `contextWindow` INTEGER NOT NULL,
    `quantization` TEXT NOT NULL,
    `temperature` REAL NOT NULL,
    `systemPrompt` TEXT NOT NULL,
    `themeMode` TEXT NOT NULL,
    `notificationsEnabled` INTEGER NOT NULL,
    `downloadAlertsEnabled` INTEGER NOT NULL,
    `dailyBriefEnabled` INTEGER NOT NULL,
    `usageWarningsEnabled` INTEGER NOT NULL,
    `memoriesJson` TEXT NOT NULL
);
```

# RAY AI — Architecture & Engineering Specs

## 1. Architectural Pattern Overview

RAY AI adheres to **Clean Architecture** and **MVVM (Model-View-ViewModel)** principles using modern Jetpack Compose for UI and Kotlin Coroutines/Flow for unidirectional data flow (UDF).

```
   ┌─────────────────────────────────────────────────────────────┐
   │                       Jetpack Compose UI                    │
   │   (ChatScreen, ModelsScreen, BenchmarkScreen, Settings)    │
   └──────────────────────────────┬──────────────────────────────┘
                                  │ Collects StateFlow / Triggers Events
                                  ▼
   ┌─────────────────────────────────────────────────────────────┐
   │                         ViewModels                          │
   │  (ChatViewModel, ModelViewModel, SettingsViewModel, etc.)   │
   └──────────────────────────────┬──────────────────────────────┘
                                  │ Manages Domain Logic & Coroutine Scope
                                  ▼
   ┌─────────────────────────────────────────────────────────────┐
   │                        Repositories                         │
   │   (ChatRepository, ModelRepository, SettingsRepository)     │
   └──────────────┬──────────────────────────────┬───────────────┘
                  │                              │
                  ▼                              ▼
   ┌──────────────────────────────┐ ┌───────────────────────────┐
   │  Room Database (Local DAOs)  │ │   GGML Local CPU JNI      │
   │ (ChatEntity, ModelEntity)    │ │   Inference Engine        │
   └──────────────────────────────┘ └───────────────────────────┘
```

---

## 2. Component Layer Breakdown

### A. Presentation Layer (`com.example.ui`)
- **Jetpack Compose Screens**:
  - `ChatScreen`: Streaming assistant responses, markdown parser, code block renderer, HTML sandbox modal, model selector menu.
  - `ModelsScreen`: Download progress cards, size indicators, model deletion, category filter chips.
  - `BenchmarkScreen`: CPU stress test, token generation speedometer, memory usage graphs.
  - `SettingsScreen`: Theme selector (Light, Dark, System), memory management, CPU thread allocation, system prompt editor, notification toggles, bug report submission.
- **Theme & Design System**:
  - `Color.kt` & `Theme.kt`: **Sunrise Minimal** design tokens supporting `SunriseLight` and `SunriseDark` palettes with smooth transitions.

### B. ViewModel Layer (`com.example.ui.viewmodel`)
- **`ChatViewModel`**: Manages chat sessions, streaming tokens, model switching, and system memory context injection.
- **`ModelViewModel`**: Coordinates background model downloads, progress tracking, and GGUF file validation.
- **`SettingsViewModel`**: Manages Room configuration updates, memory list parsing, theme switching, and diagnostics.
- **`BenchmarkViewModel`**: Executes on-device matrix math and synthetic token speed benchmarks.

### C. Data Layer (`com.example.data`)
- **Room Local Database** (`AppDatabase`):
  - `ChatDao` & `ChatEntity`: Stores conversation histories, role messages, timestamps, and model associations.
  - `ModelDao` & `ModelEntity`: Tracks downloaded model files, local file paths, quantized precision (INT4, Q4_K_M), and active status.
  - `SettingsDao` & `SettingsEntity`: Stores CPU thread counts, context window sizes, temperature, theme mode, notification toggles, and JSON memories.

### D. Native Engine Layer (`com.example.engine`)
- **`LocalAiEngine`**: Native JNI bridge interfacing with GGML C/C++ runtime for loading GGUF weights into CPU RAM and executing SIMD-accelerated matrix multiplication.

---

## 3. Unidirectional Data Flow (UDF)

1. **User Action**: User taps "Send" in `ChatScreen`.
2. **ViewModel Event**: `ChatViewModel.sendMessage()` appends user message to `ChatRepository` and updates `ChatUiState`.
3. **Engine Invocation**: `ChatViewModel` passes prompt + memory context to `LocalAiEngine`.
4. **Flow Emitted**: Engine streams response tokens incrementally via `Flow<String>`.
5. **State Update**: `ChatUiState` appends incoming tokens in real time; Compose recomposes markdown text view.

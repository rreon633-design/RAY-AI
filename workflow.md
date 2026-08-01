# RAY AI — System Workflow & Execution Diagrams

## 1. End-to-End Chat Workflow

```
┌─────────────┐       ┌─────────────────┐       ┌──────────────────────┐
│  User Input │ ───>  │  ChatViewModel  │ ───>  │ Memory & Prompt Prep │
└─────────────┘       └─────────────────┘       └──────────────────────┘
                                                           │
                                                           ▼
┌─────────────┐       ┌─────────────────┐       ┌──────────────────────┐
│   Compose   │  <─── │ LocalAiEngine   │  <─── │ Inject System Prompt │
│  Markdown   │       │ (JNI GGML Core) │       │ + Memories Context   │
└─────────────┘       └─────────────────┘       └──────────────────────┘
       │                       ▲
       ▼                       │
 [Token Stream] ───────────────┘
```

1. **User Prompt Submission**: User types message into input box and taps Send icon.
2. **Context Assembly**: `ChatViewModel` fetches system prompt, active settings (CPU threads, temperature), and saved user memories from `SettingsEntity`.
3. **Prompt Template Formatting**: Constructs ChatML / Alpaca / Instruct formatted prompt string.
4. **Native Token Generation**: `LocalAiEngine` invokes C++ runtime on ARM CPU cores.
5. **Streaming Response**: Tokens are emitted via Kotlin `Flow` and accumulated in Compose UI state.
6. **Room Database Sync**: Message history is persisted asynchronously to Room DB.

---

## 2. Model Download Workflow

```
┌──────────────────┐     ┌──────────────────┐     ┌─────────────────────┐
│ ModelsScreen UI  │ ──> │ ModelViewModel   │ ──> │ DownloadManager Service│
└──────────────────┘     └──────────────────┘     └─────────────────────┘
                                                             │
                                                             ▼
┌──────────────────┐     ┌──────────────────┐     ┌─────────────────────┐
│ Active Model Set │ <── │ Room DB Updated  │ <── │ Save .gguf to Disk │
└──────────────────┘     └──────────────────┘     └─────────────────────┘
```

1. User selects model card (e.g., Qwen 2.5 0.5B) in `ModelsScreen`.
2. `ModelViewModel.startDownload()` launches an HTTP GET request with range byte headers.
3. Download progress percentage and MB downloaded are posted to `ModelUiState`.
4. Upon completion, file integrity is verified, and `isDownloaded` flag in Room DB is set to `true`.
5. Model is immediately available in the Chat model selector dropdown.

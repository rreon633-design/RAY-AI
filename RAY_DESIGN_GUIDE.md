# RAY AI — Sunrise Minimal Design System & Interface Guide

Welcome to the **RAY AI Design & Interface Guide**. This document provides an exhaustive breakdown of the user interface architecture, structural layouts, navigation flows, custom indicators, and under-the-hood mechanisms powering RAY AI.

---

## 1. Sunrise Design Philosophy & Splash Experience
RAY AI is crafted around a custom **Sunrise Minimal** design language. Rather than a sterile "high-tech" or standard slate-gray appearance, RAY AI uses a warm, cozy, and highly inviting visual system inspired by the sunrise.

- **Warm Neutral Canvas**: Uses `#FFFBF5` / `#FFF8F0` as the soft background canvas. This reduces eye strain, avoids direct clinical white glare, and establishes a friendly, domestic atmosphere.
- **Sunrise Accents**: Coral orange (`#FF7A59`), golden amber (`#FFB84D`), and soft peach container tones represent high energy, optimism, and warmth.
- **Warm Charcoal Typography**: Text uses `#2B2320` instead of a harsh pure black (`#000000`), maintaining excellent WCAG AA readability while softening visual boundaries.
- **Modern Organic Geometry**: Rounded corners on components (20dp to 28dp pill shapes) evoke a premium, crafted, tactile feel reminiscent of modern hardware or artisanal stationery.

### 1.1 Android 12+ Splash System (`SplashScreen.kt` & `MainActivity.kt`)
On initial startup, the system intercepts launching with a unified, dual-layered splash sequence:
1. **System Window Initializer**: Uses the AndroidX Core SplashScreen API with style `Theme.App.Starting` to maintain color-accurate status bars and backdrops (`#FFFBF5`) while the app process boots in the background.
2. **High-Fidelity Sunrise Splash**: Displays a beautiful Jetpack Compose scene featuring:
   - **Slowly Drifting Gradient Background**: A dynamic linear brush drifting softly between deep coral, golden amber, and off-white to emulate dawn.
   - **Overshoot Animated Logomark**: The centered sun badge scales in dynamically with a spring-loaded bounce/overshoot effect.
   - **Fade-In Tagline**: The title "RAY AI" and tagline *"Private AI. On your device."* fade in cleanly below the logo.
   - **Horizontal Progress Indicator**: A slim, elegant sunrise-gradient progress bar that tracks the initialization of native libraries before executing a smooth crossfade transition into the main chat page.

---

## 2. Navigation System & Shell

The application is framed by a cohesive, easy-to-reach navigation system built to handle offline, single-handed mobile scenarios.

### 2.1 Top App Header Bar (`ChatHeaderBar.kt`)
Located at the top of the chat view, the App Header remains sticky and transparently blends with the warm canvas.
- **Menu/Drawer Toggle Button**: A circular outlined button on the far left that pulls out the Chat History Side Drawer.
- **Brand Title**: "RAY AI" presented in bold, geometric typography.
- **Status Indicator Pill**: A dynamic status badge displaying:
  - **No Model Selected (Idle)**: Gray status indicator reading `No Model`.
  - **Model Loading**: Glowing amber indicator reading `Loading...`.
  - **Model Ready/Active**: Bright green pulse indicator displaying the truncated active model name (e.g., `Qwen 1.5B`).
- **New Chat Icon Button**: An outlined action button on the far right that flushes current state and starts a clean chat session instantly.

### 2.2 Bottom Navigation Bar (`AppNavBar.kt`)
The primary tab container anchoring the entire application layout, keeping screen transitions reachable within thumb range.
- **Layout Structure**: A floating surface with subtle elevation or border, ensuring clear division from the content area.
- **Navigation Tabs**:
  1. **Chat**: Main conversational interface with local LLM.
  2. **Models**: Central catalog to download, manage, and toggle local GGUF/ONNX engines.
  3. **Settings**: Advanced controls for CPU threads, temperatures, prompts, and memory logs.
- **Active State Highlights**: Active tabs are tinted in sunrise coral, backed by an elegant rounded pill background to emphasize selection.

### 2.3 Chat History Side Drawer (`ChatDrawerSheet.kt`)
Accessible by swiping from the left margin or clicking the header menu icon.
- **Workspace Canvas**: A solid panel containing a list of past conversational threads saved in the local Room database.
- **Trash/Delete Trigger**: An elegant trash icon beside each past session item for secure, immediate thread destruction.
- **Local Persistence Indicator**: Clear visual labels reminding the user that all conversations are stored locally on the device's secure SQLite sandbox.

---

## 3. Screen Layout Architecture

### 3.1 Chat Interface (`ChatScreen.kt` & `ChatMessageList.kt`)
A clean, vertically-scrolling message canvas with high text density but ample negative space.
- **Scroll Anchor**: Automatically snaps to the newest incoming token chunk. Includes a floating "Scroll-to-Bottom" button if the user scrolls upwards manually.
- **Empty State Illustration**: If there are no messages, a large, beautiful Sunrise Sun gradient ring logo rises above the greeting: *"Private AI. On your device."* 

### 3.2 Model Management Screen (`ModelsScreen.kt` & `ModelCardItem.kt`)
A comprehensive catalog to control physical intelligence on your hardware.
- **Device Statistics Dashboard**: Displays live metrics about the phone's physical architecture:
  - Total RAM & Free Memory.
  - Active CPU Core Count.
  - Recommended Model fitting size.
- **Storage Allocation Bar**: A horizontal multi-color progress bar displaying how much disk storage remains on device, helping the user prevent out-of-disk failures during heavy downloads.

### 3.3 Settings & Customization Screen (`SettingsScreen.kt`)
Organized as structured, grouped action cards.
- **Inference Tuning Group**: High-precision interactive sliders to adjust:
  - **Temperature**: Controls creativity/randomness.
  - **Max Output Tokens**: Limits the length of LLM responses.
  - **System Instructions**: Customizable text input card to alter RAY's persona.
- **Memories Panel**: An interactive log where users can view facts RAY AI has learned about them over time (e.g., *"User lives in Seattle"* or *"User prefers Python"*).

---

## 4. Interactive Elements & Action Buttons

### 4.1 Chat Message Bubbles (`ChatMessageItem.kt`)
Conversational bubbles are styled differently to create immediate visual separation:
- **User Messages**:
  - Right-aligned.
  - Styled with a warm, soft peach container (`#FFF2ED`) with dark terracotta text.
  - Features an asymmetrical bottom-right tail.
- **Assistant Messages**:
  - Left-aligned.
  - Flat white card with a subtle warm border (`#F1E6DC`).
  - Topped by a compact peach icon avatar representational of RAY.
- **Action Toolbar (Hover/Focus-triggered)**: A row of low-contrast micro-actions fading in beneath each bubble:
  - **Copy Button**: Places text on the secure system clipboard.
  - **Regenerate Button**: Resends the prompt to trigger a fresh model response.
  - **Share Action**: Standard Android intent to distribute the text.

### 4.2 Conversational Input Bar (`ChatInputBar.kt`)
A floating, rounded pill container anchored above the soft keyboard, preventing intrusive layouts.
- **Attachment Trigger (Left)**: A circular button to feed documents, text files, or configuration assets into the model context.
- **Dynamic Auto-growing Text Field**: Clean input canvas supporting up to 6 lines before activating scrolling.
- **Circular Gradient Send Button**: A gorgeous sunrise gradient button (`#FF7A59` to `#FFB84D`) that morphs visually:
  - **Normal State**: Displays a paper-plane send icon.
  - **Generating State**: Morphs into a red-tinted Stop/Square button to immediately halt local generation/token streaming.

---

## 5. Live Indicators & System Mechanisms

### 5.1 Device Hardware Recommendation Engine ("Phone Fit ★" Indicator)
An advanced matching algorithm parsed on initial startup to protect consumer hardware from crash/OOM scenarios:
- **Hardware Parser Mechanism**: Checks the physical device configuration (Total RAM, free RAM, and CPU core efficiency).
- **Auto-Highlight Tag**: The system selects the most efficient GGUF quantized model and flags it with a bright, comforting green badge: **"Phone Fit ★"**.
- **Model Card Highlighting**: The designated card is decorated with a thicker, glowing, soft green border (`#7FB77E`) to guide the user towards a safe, fast, and optimized experience.

### 5.2 Token Generation Metrics Caption
Displays live telemetry directly underneath completed responses:
- **Token Speed**: Displays generation velocity in tokens per second (e.g., `38.4 tok/s`).
- **Inference Latency**: Shows Time-to-First-Token (TTFT) and total processing latency (e.g., `0.9s delay`).

### 5.3 Model Loading & Downloader States
- **Download Progress Ring**: Replaces standard flat indicators with a circular progress wheel directly inside the model management card. Displays speed (MB/s) and estimated time of arrival (ETA) underneath.
- **Active Model Highlight**: The model currently loaded in system RAM is given a gradient left border accent and a soft drop shadow, communicating its active state.

---

## 6. Under-the-Hood Offline Verification

Since **RAY AI is designed to run 100% offline**, the interface strictly separates connection-dependent utilities from conversation:
- **Download Banner**: Appears above the models tab indicating that internet connectivity is *only* required for downloading the model files from public repositories.
- **Conversation Offline Guarantee**: A quiet label on the chat screen guarantees: *"All computational inference occurs locally on your CPU. No data ever leaves your device."*

---
*Created and maintained under the RAY AI Design System Guidelines. Keep all elements warm, accessible, and fast.*

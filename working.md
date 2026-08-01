# RAY AI — Working Principle & Technical Deep-Dive

## 1. How Local AI Inference Works on Android

Local AI on Android involves executing large language models directly on the mobile system-on-chip (SoC) using CPU cores (Cortex-X / Cortex-A) or GPU/NPU accelerators without network reliance.

---

## 2. Quantization Mechanics (INT4)

Large Language Models (LLMs) originally train using 16-bit floating point numbers (`FP16`). A 7B parameter model in FP16 requires **14 GB of RAM**, which far exceeds average Android RAM constraints.

**Quantization** maps 16-bit floating values down to 4-bit integers (`INT4`):

$$\text{Weight}_{FP16} \approx \text{Scale} \times \text{Weight}_{INT4} + \text{ZeroPoint}$$

- **Memory Compression**: Reduces a 7B model from 14 GB down to **~3.5 GB**.
- **Bandwidth Reduction**: Mobile SoCs are heavily memory-bandwidth constrained. Reading 4-bit weights over the LPDDR5 bus is **4x faster** than 16-bit weights.

---

## 3. CPU Thread Scheduling & Thermal Management

Inference speed is directly tied to ARM thread allocation:

- **Performance Cores (Cortex-X4 / Cortex-A720)**: High frequency, fast tok/s, higher power consumption.
- **Efficiency Cores (Cortex-A520)**: Lower frequency, ideal for background idle processing.

RAY AI allows users to set CPU threads from **1 to 8**:
- **4 Threads (Recommended)**: Optimal balance between speed and battery thermal stability.
- **8 Threads**: Maximum speed for quick single-turn coding queries.

---

## 4. Memory Context Management

The **KV (Key-Value) Cache** stores previous token attention calculations to avoid re-evaluating prompt history on every turn.

- **Context Window (2048 Tokens)**: Occupies ~128 MB of KV Cache in RAM.
- **Context Recopying**: When history exceeds 2048 tokens, older non-system prompt turns are pruned while preserving user memories.

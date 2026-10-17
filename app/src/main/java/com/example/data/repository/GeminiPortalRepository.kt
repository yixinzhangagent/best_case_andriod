package com.example.data.repository

import com.example.data.model.BenchmarkMetric
import com.example.data.model.CodeSnippetVariant
import com.example.data.model.DocSectionItem
import com.example.data.model.GeminiModelSpec
import com.example.data.model.NewsArticleItem
import com.example.data.model.ResourceAssetItem

object GeminiPortalRepository {

    val modelSpecs: List<GeminiModelSpec> = listOf(
        GeminiModelSpec(
            id = "gemini-4-ultra",
            name = "Gemini 4 Ultra",
            apiCode = "gemini-4.0-ultra-preview",
            badge = "旗舰科研与极限推理",
            contextWindow = "10,000,000 Tokens (10M)",
            maxOutput = "128,000 Tokens",
            modalities = "文本 / 代码 / 原生音频 / 4K视频流 / 3D点云 / PDF原件",
            latencyTtft = "0.42s",
            priceInputPerM = "$1.75 / 1M (缓存 $0.20)",
            sweBenchScore = "94.8% (Verified)",
            claudeModelCompared = "Claude 4 Opus / 3.7 Sonnet Max",
            claudeWinSummary = "上下文容量是 Claude (200K) 的 50 倍；SWE-bench 高出 9.6%，输入成本降低 88%。",
            googleColorHex = 0xFF1A73E8
        ),
        GeminiModelSpec(
            id = "gemini-4-pro",
            name = "Gemini 4 Pro",
            apiCode = "gemini-3.1-pro-preview",
            badge = "全栈生产主力",
            contextWindow = "5,000,000 Tokens (5M)",
            maxOutput = "65,536 Tokens",
            modalities = "文本 / 代码 / 图像 / 音频 / 视频 / 实时工具调用",
            latencyTtft = "0.24s",
            priceInputPerM = "$0.75 / 1M (缓存 $0.08)",
            sweBenchScore = "91.4% (Verified)",
            claudeModelCompared = "Claude 3.7 Sonnet / Claude 4 Sonnet",
            claudeWinSummary = "原生支持 2 小时 4K 视频与整库代码直接喂入，无需切片 RAG，单次调用成本仅为 Claude Sonnet 的 1/4。",
            googleColorHex = 0xFF34A853
        ),
        GeminiModelSpec(
            id = "gemini-4-flash",
            name = "Gemini 4 Flash",
            apiCode = "gemini-3.5-flash",
            badge = "亚秒级高频实时交互",
            contextWindow = "2,000,000 Tokens (2M)",
            maxOutput = "65,536 Tokens",
            modalities = "全模态双向实时流 (Live WebRTC + Audio/Video)",
            latencyTtft = "0.11s",
            priceInputPerM = "$0.075 / 1M",
            sweBenchScore = "86.2% (Verified)",
            claudeModelCompared = "Claude 3.5 Haiku",
            claudeWinSummary = "推理质量超越上一代旗舰 Pro，延迟低至 110ms，内置免费代码执行沙盒与 Google Search 实时联网。",
            googleColorHex = 0xFFFBBC04
        ),
        GeminiModelSpec(
            id = "gemini-4-nano",
            name = "Gemini 4 Nano-Edge",
            apiCode = "gemini-4.0-nano-aicore",
            badge = "Android 端侧零流量隐私推理",
            contextWindow = "128,000 Tokens",
            maxOutput = "16,384 Tokens",
            modalities = "文本 / 屏幕视觉感知 / 语音流 / 本地系统 Action",
            latencyTtft = "0.03s (本地 NPU)",
            priceInputPerM = "$0.00 (端侧免费)",
            sweBenchScore = "74.5% (端侧记录)",
            claudeModelCompared = "Claude 无端侧模型 (0 支持)",
            claudeWinSummary = "Claude 仅支持纯云端 API；Gemini 4 Nano 通过 Android AICore 原生运行在数十亿手机与车载设备上，100% 离线可用。",
            googleColorHex = 0xFFEA4335
        )
    )

    val docSections: List<DocSectionItem> = listOf(
        DocSectionItem(
            id = "doc-quickstart",
            category = "快速开始",
            title = "5 分钟接入 Gemini 4 统一多模态 SDK",
            readTime = "4 分钟",
            summary = "通过 Google GenAI 统一 SDK 或标准 REST 接口，用 6 行代码同时处理文本、代码库、音频流与 4K 视频，并提供完整的 OpenAI / Anthropic 兼容层。",
            claudeEquivalentPath = "docs.claude.com/en/docs/initial-setup",
            superiorityPoints = listOf(
                "内置 Anthropic Messages API 零改动兼容路由，把 baseUrl 切换到 Google 即可接管现有 Claude 代码。",
                "原生支持图像、音频、视频、PDF 字节流混合输入，无需像 Claude 一样受限于 200K 上下文与无视频帧率理解。",
                "免费附带云端 Python 代码执行沙盒（Code Execution）与 Google 实时搜索（Search Grounding）。"
            ),
            architectureSteps = listOf(
                "1. 在 AI Studio Secrets 面板配置 GEMINI_API_KEY（自动注入 Android BuildConfig 或环境变量）。",
                "2. 选择模型标识：复杂工程用 gemini-3.1-pro-preview，高频低延迟用 gemini-3.5-flash。",
                "3. 构造 GenerateContentRequest，可按需开启 thinkingConfig 与 googleSearch 工具。",
                "4. 使用流式 SSE (streamGenerateContent) 实现 110ms 首字上屏体验。"
            ),
            codeVariants = listOf(
                CodeSnippetVariant(
                    language = "Kotlin (Android)",
                    code = """
// Android Retrofit + Coroutines 调用 Gemini 4 系列接口
val request = GenerateContentRequest(
    systemInstruction = Content(parts = listOf(Part(text = "你是 Google Gemini 4 架构师。"))),
    contents = listOf(
        Content(parts = listOf(Part(text = "分析高并发微服务网关的背压控制策略，并给出 Kotlin 协程实现。")))
    ),
    generationConfig = GenerationConfig(
        temperature = 0.3f,
        thinkingConfig = ThinkingConfig(thinkingLevel = "high")
    )
)
val response = RetrofitClient.service.generateContent(BuildConfig.GEMINI_API_KEY, request)
println(response.candidates.first().content.parts.first().text)
                    """.trimIndent()
                ),
                CodeSnippetVariant(
                    language = "Python",
                    code = """
from google import genai
from google.genai import types

client = genai.Client() # 自动读取 GEMINI_API_KEY
response = client.models.generate_content(
    model="gemini-3.5-flash",
    contents=["用架构图和代码解释 Gemini 4 的 10M 上下文缓存机制"],
    config=types.GenerateContentConfig(
        thinking_config=types.ThinkingConfig(thinking_level="HIGH"),
        tools=[{"google_search": {}}, {"code_execution": {}}]
    )
)
print(response.text)
                    """.trimIndent()
                ),
                CodeSnippetVariant(
                    language = "TypeScript",
                    code = """
import { GoogleGenAI } from "@google/genai";

const ai = new GoogleGenAI({ apiKey: process.env.GEMINI_API_KEY });
const response = await ai.models.generateContent({
  model: "gemini-3.1-pro-preview",
  contents: "构建一个支持离线同步的 CRDT 协同文档状态机",
  config: {
    systemInstruction: "输出严密、可直接编译的 TypeScript 生产级代码。",
    temperature: 0.2
  }
});
console.log(response.text);
                    """.trimIndent()
                ),
                CodeSnippetVariant(
                    language = "cURL (REST)",
                    code = """
curl "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=${'$'}GEMINI_API_KEY" \
  -H 'Content-Type: application/json' \
  -X POST \
  -d '{
    "contents": [{"parts":[{"text": "对比 Gemini 4 与 Claude 4 的多模态架构差异"}]}],
    "generationConfig": {"thinkingConfig": {"thinkingLevel": "high"}}
  }'
                    """.trimIndent()
                )
            ),
            sandboxPrompt = "请给出高并发网关背压（Backpressure）控制的 3 种核心模式，并附带一段精简可运行的 Kotlin Flow 示例代码。",
            sandboxSystemInstruction = "你是 Google 首席分布式系统工程师，回答精准、结构清晰、代码可直接编译。",
            expectedBenchmarkOutput = """
### 高并发网关背压控制三大核心模式（Gemini 4 深度架构解析）

1. **令牌桶 + 响应式信号量动态限流 (Dynamic Adaptive Concurrency)**
   根据下游 P99 延迟自动缩放并发窗口（类似 Netflix Gradient2 算法），防止级联雪崩。

2. **有界环形缓冲 + 优先级丢弃策略 (Bounded RingBuffer with Priority Shedding)**
   利用 Kotlin `Channel(capacity, BufferOverflow.DROP_OLDEST)` 对非核心遥测流量降级，保障核心交易链路 100% 吞吐。

3. **端到端流控令牌传播 (HTTP/3 & gRPC Flow Control Window)**
   直接将消费者消费速率反压至 TCP/QUIC 接收窗口。

```kotlin
fun <T> Flow<T>.adaptiveBackpressure(maxConcurrency: Int = 64): Flow<T> =
    this.buffer(capacity = maxConcurrency, onBufferOverflow = BufferOverflow.SUSPEND)
        . throttleLatest(15.milliseconds)
```
            """.trimIndent()
        ),
        DocSectionItem(
            id = "doc-10m-context",
            category = "核心架构",
            title = "1000 万 Token 零衰减上下文与持久化缓存 (Context Caching 2.0)",
            readTime = "7 分钟",
            summary = "告别繁琐脆弱的向量切片 RAG！直接将整个 Linux 内核模块、300 本企业手册或 8 小时高清视频一次性注入持久缓存，大海捞针准确率达 99.94%。",
            claudeEquivalentPath = "docs.claude.com/en/docs/build-with-claude/prompt-caching",
            superiorityPoints = listOf(
                "Claude 4 最大仅支持 200K（特定企业白名单 500K），而 Gemini 4 提供标准 2M~10M 上下文窗口。",
                "支持跨会话显式持久化缓存（Explicit TTL Cache）与隐式前缀自动缓存，缓存命中成本降低 88%。",
                "多模态大海捞针测试（MM-NIAH）在 1000 万 Token 处仍保持 99.94% 召回率，无首尾遗忘效应。"
            ),
            architectureSteps = listOf(
                "1. 上传大型代码库打包文件、长视频或多份财报 PDF 至 Files API 或内联缓存对象。",
                "2. 创建 CachedContent 资源并设置 TTL（支持分钟级至 30 天持久化驻留）。",
                "3. 后续数万次团队查询直接引用 cachedContent 句柄，首字延迟固定在 200ms 内。",
                "4. 支持增量热更新（Delta Context Append），无需重建整块缓存。"
            ),
            codeVariants = listOf(
                CodeSnippetVariant(
                    language = "Python",
                    code = """
from google import genai
from google.genai import types

client = genai.Client()
# 一次性缓存整个工程代码库 + 3 小时架构评审录像 (共 4.2M Tokens)
cache = client.caches.create(
    model="gemini-3.1-pro-preview",
    config=types.CreateCachedContentConfig(
        display_name="monorepo_v4_cache",
        system_instruction="你是熟知本仓库每一行历史提交的首席架构师。",
        contents=[monorepo_bundle, architecture_video_4k],
        ttl="86400s" # 缓存 24 小时，团队共享零重复计费
    )
)

response = client.models.generate_content(
    model="gemini-3.1-pro-preview",
    contents="定位支付模块在跨时区结算时的潜在竞态漏洞，并生成修复补丁。",
    config=types.GenerateContentConfig(cached_content=cache.name)
)
                    """.trimIndent()
                ),
                CodeSnippetVariant(
                    language = "Kotlin (Android)",
                    code = """
// 在移动端直接引用云端预构建的百万 Token 知识库缓存句柄
val cachedRequest = GenerateContentRequest(
    contents = listOf(
        Content(parts = listOf(Part(text = "根据已缓存的 1200 页医疗器械合规手册，核查第 44 条蓝牙加密规范。")))
    ),
    generationConfig = GenerationConfig(temperature = 0.1f)
)
                    """.trimIndent()
                )
            ),
            sandboxPrompt = "如果我有一个包含 45 万行代码的微服务单仓（约 380 万 Token），使用 Gemini 4 Context Caching 相比传统向量数据库 RAG 有哪些架构与成本优势？",
            sandboxSystemInstruction = "你是 Google Cloud 首席 AI 架构师，请用定量数据对比 10M Context Caching 与传统 RAG 及 Claude 200K 方案。",
            expectedBenchmarkOutput = """
### 380 万 Token 全量代码库：Gemini 4 Context Caching vs 传统切片 RAG vs Claude 200K

1. **全局调用链完整性（100% vs 42%）**
   传统 RAG 会把跨文件的接口定义、DI 注入与宏展开切碎，导致跨模块 Bug 漏报率高达 58%；Claude 200K 窗口仅能装下该仓库的 5.2%。Gemini 4 可 100% 装入全部 45 万行代码，构建完整 AST 语义图。

2. **TCO 成本与延迟对比**
   - 首次建立 3.8M Token 缓存后，每次团队提问仅按缓存读取价（$0.08/1M）计费。
   - 省去昂贵的向量数据库（Pinecone/Milvus）运维与 Embedding 同步管道，综合工程成本降低 **71%**。
            """.trimIndent()
        ),
        DocSectionItem(
            id = "doc-deep-think",
            category = "推理引擎",
            title = "Deep Think 2.0：可调算力预算与形式化验证推理",
            readTime = "6 分钟",
            summary = "超越 Claude Extended Thinking：支持动态算力分级（minimal / low / medium / high）与并行蒙特卡洛树搜索，在竞赛级数学、形式化证明与复杂重构中实现零幻觉推导。",
            claudeEquivalentPath = "docs.claude.com/en/docs/build-with-claude/extended-thinking",
            superiorityPoints = listOf(
                "支持按请求精确控制 thinkingLevel，简单任务零额外思考开销，极限任务自动启动多路径并行验证。",
                "思考过程中原生允许调用 Python 沙盒验算中间公式，而非像 Claude 仅靠纯文本自言自语猜测计算结果。",
                "AIME 2026 数学满分 99.2%，GPQA Diamond 科学推理达 89.7%，全面领先 Claude 4 Opus。"
            ),
            architectureSteps = listOf(
                "1. 在 generationConfig 中配置 thinkingConfig = ThinkingConfig(thinkingLevel = \"high\")。",
                "2. 开启思考摘要流（Thought Summary Stream），在 UI 实时展示推理节点与数学验算过程。",
                "3. 结合 Structured Outputs，在深度思考结束后严格输出符合 JSON Schema 的类型安全结果。"
            ),
            codeVariants = listOf(
                CodeSnippetVariant(
                    language = "Kotlin (Android)",
                    code = """
val deepThinkRequest = GenerateContentRequest(
    contents = listOf(
        Content(parts = listOf(Part(text = "证明并实现一个无锁 MPMC 队列在 ARMv9 弱内存模型下的内存序安全性。")))
    ),
    generationConfig = GenerationConfig(
        thinkingConfig = ThinkingConfig(thinkingLevel = "high"),
        temperature = 0.2f
    )
)
                    """.trimIndent()
                ),
                CodeSnippetVariant(
                    language = "Python",
                    code = """
response = client.models.generate_content(
    model="gemini-3.1-pro-preview",
    contents="求解带时间窗与动态充电桩排队约束的 50 节点冷链物流车辆路径问题 (VRPTW)",
    config=types.GenerateContentConfig(
        thinking_config=types.ThinkingConfig(
            thinking_level="HIGH",
            include_thoughts=True
        )
    )
)
                    """.trimIndent()
                )
            ),
            sandboxPrompt = "请使用深度推理分析：在分布式 Raft 共识协议中，为什么单纯依赖 Leader Lease 机制在发生 GC 停顿或时钟漂移时会导致脑裂读取？如何用 ReadIndex 优化？",
            sandboxSystemInstruction = "你是分布式共识算法专家，请按「问题根因 -> 时序反例推导 -> ReadIndex 解决方案」分步推理。",
            expectedBenchmarkOutput = """
### Deep Think 2.0 形式化时序推导：Raft Leader Lease 脑裂风险与 ReadIndex 优化

1. **时序反例推导（Clock Drift + STW GC）**
   假设旧 Leader L₁ 获得租约 [t₀, t₀ + Δ]，若 L₁ 发生长 GC 停顿且本地石英钟速率偏慢（drift > ε），集群在 t₁ 已选出新 Leader L₂ 并提交新日志 X=2，而 L₁ 恢复后本地时钟仍认为处于租约有效期内，直接返回脏数据 X=1（破坏线性一致性 Linearizability）。

2. **ReadIndex 零磁盘写入优化方案**
   - 记录当前 `commitIndex` 为 `readIndex`；
   - 向多数派发送一轮轻量级心跳（无需写 WAL 磁盘日志）确认自己仍是合法 Leader；
   - 等待状态机 `applyIndex >= readIndex` 后立即响应客户端，兼顾高吞吐与严格线性一致性。
            """.trimIndent()
        ),
        DocSectionItem(
            id = "doc-mcp-agents",
            category = "智能体与工具",
            title = "Agentic Tool Use & 开放协议：原生兼容 MCP 与 Google A2A",
            readTime = "8 分钟",
            summary = "不仅 100% 兼容 Anthropic 提出的 MCP (Model Context Protocol)，更原生搭载 Google A2A (Agent-to-Agent) 跨智能体协同总线、内置 Google Search Grounding 与 Computer Use 2.0。",
            claudeEquivalentPath = "docs.claude.com/en/docs/agents-and-tools/mcp",
            superiorityPoints = listOf(
                "同时支持本地 MCP Server 工具挂载与跨云端 A2A 多智能体协商协议，打破单智能体上下文瓶颈。",
                "支持并行工具调用（Parallel Function Calling）与组合式自动重试，工具参数准确率达 98.9%。",
                "Computer Use 2.0 原生支持 Android 屏幕树与 Web DOM 双模高精度像素级操作，速度比 Claude 快 2.8 倍。"
            ),
            architectureSteps = listOf(
                "1. 声明 FunctionDeclarations 或直接通过 MCP 桥接器导入现有 GitHub / Postgres / Slack 工具集。",
                "2. 开启 AUTO 或 ANY 工具调用模式，让 Gemini 4 自主规划多步 DAG 执行图。",
                "3. 利用 A2A 协议将子任务分发给专属垂直 Agent（如安全审计 Agent、SQL 优化 Agent）。"
            ),
            codeVariants = listOf(
                CodeSnippetVariant(
                    language = "Kotlin (Android)",
                    code = """
val tools = listOf(
    buildJsonObject {
        putJsonArray("functionDeclarations") {
            addJsonObject {
                put("name", "queryProductionMetrics")
                put("description", "查询指定微服务在过去 N 分钟内的 P99 延迟与错误率")
                putJsonObject("parameters") {
                    put("type", "OBJECT")
                    putJsonObject("properties") {
                        putJsonObject("serviceName") { put("type", "STRING") }
                        putJsonObject("windowMinutes") { put("type", "INTEGER") }
                    }
                    putJsonArray("required") { add("serviceName") }
                }
            }
        }
    }
)
                    """.trimIndent()
                )
            ),
            sandboxPrompt = "设计一个由『架构规划 Agent』『代码生成 Agent』『安全红队审计 Agent』组成的 A2A + MCP 协同流水线，说明它们如何协作修复一个 SQL 注入与越权漏洞。",
            sandboxSystemInstruction = "你是企业级多智能体系统架构师，输出清晰的协作协议流转与工具声明。",
            expectedBenchmarkOutput = """
### A2A + MCP 三智能体协同修复流水线

1. **架构规划 Agent (Coordinator)**
   通过 MCP `git_diff` 与 `sonarqube_scan` 工具定位存在拼接风险的 DAO 层文件，生成包含安全约束的 A2A 任务工单。
2. **代码生成 Agent (Builder)**
   接收工单后将动态 SQL 重构为参数化预编译语句（PreparedStatement）并补充 RBAC 租户 ID 强制校验注解。
3. **安全红队审计 Agent (Verifier)**
   在内置隔离沙盒中自动构造 12 种多态注入 Payload 并发起回归测试，验证通过后自动签发 PR。
            """.trimIndent()
        ),
        DocSectionItem(
            id = "doc-multimodal-live",
            category = "实时多模态",
            title = "Multimodal Live API：双向超低延迟音视频流与实时屏幕共享",
            readTime = "6 分钟",
            summary = "Claude 至今不具备的杀手级能力：通过 WebSocket / WebRTC 建立双向全双工音视频管道，支持随时语音打断、摄像头实时画面理解、情感语调感知与多语种同传。",
            claudeEquivalentPath = "docs.claude.com/en/docs/build-with-claude/vision",
            superiorityPoints = listOf(
                "Claude Vision 仅支持静态图片单向提问；Gemini 4 Live API 支持 30fps 视频流 + 24kHz 原生音频双向对话。",
                "端到端语音交互延迟低至 220ms，支持自然打断（Barge-in）与 48 种高表现力原生声线。",
                "可在会话中途无缝触发 Function Calling、代码执行与 Google 地图/搜索实景增强。"
            ),
            architectureSteps = listOf(
                "1. 使用 gemini-2.5-flash-native-audio-preview-12-2025 或 Gemini 4 Live 端点建立双向会话。",
                "2. 将 Android CameraX 视频帧与 AudioRecord PCM 流持续推送至会话。",
                "3. 实时接收模型返回的音频流与结构化工具调用事件。"
            ),
            codeVariants = listOf(
                CodeSnippetVariant(
                    language = "Kotlin (Android)",
                    code = """
val liveRequest = GenerateContentRequest(
    contents = listOf(
        Content(parts = listOf(Part(text = "用温暖专业的中文语音总结当前屏幕上的架构图要点")))
    ),
    generationConfig = GenerationConfig(
        responseModalities = listOf("AUDIO"),
        speechConfig = SpeechConfig(
            voiceConfig = VoiceConfig(PrebuiltVoiceConfig(voiceName = "Kore"))
        )
    )
)
                    """.trimIndent()
                )
            ),
            sandboxPrompt = "如何在 Android 应用中结合 CameraX 与 Gemini Multimodal Live API，为视障用户构建一个实时过马路与超市商品识别语音助手？",
            sandboxSystemInstruction = "你是 Android 多模态无障碍应用专家，给出低延迟帧采样与语音打断设计方案。",
            expectedBenchmarkOutput = """
### Android CameraX + Gemini 4 Live API 实时无障碍视觉助手架构

1. **自适应关键帧采样 (1~2 FPS + 运动触发)**
   使用 CameraX `ImageAnalysis.Analyzer` 提取 YUV_420_888 并压缩为 768px WebP，当加速度计检测到用户转向时自动提升帧率。
2. **全双工 PCM 语音流与紧急打断**
   当识别到红绿灯倒计时或台阶障碍物时，模型通过高优先级 `[URGENT_INTERRUPT]` 标记立即抢占语音播报通道，端到端告警延迟 < 250ms。
            """.trimIndent()
        ),
        DocSectionItem(
            id = "doc-structured-grounding",
            category = "生产可靠性",
            title = "100% 严格 JSON Schema 输出与 Google Search 实时事实锚定",
            readTime = "5 分钟",
            summary = "通过有限状态机约束解码（Constrained Decoding）保证 100% 符合复杂嵌套 JSON Schema，同时结合全球第一的 Google 搜索引擎实时注入可溯源网页引用与置信度评分。",
            claudeEquivalentPath = "docs.claude.com/en/docs/test-and-evaluate/strengthen-guardrails/reduce-hallucinations",
            superiorityPoints = listOf(
                "原生 responseSchema 在解码层强制语法树合法，绝不出现缺括号或字段类型漂移。",
                "内置 Google Search Grounding 返回精确到句子的 groundingChunks 与源站 URL，事实准确率比 Claude Web Search 高出 18.4%。"
            ),
            architectureSteps = listOf(
                "1. 设置 responseMimeType = \"application/json\" 并传入严格定义的 responseSchema。",
                "2. 在 tools 数组中声明 {\"google_search\": {}} 开启实时联网锚定。",
                "3. 从 groundingMetadata 中提取搜索词、网页标题与原文片段用于前端高亮溯源。"
            ),
            codeVariants = listOf(
                CodeSnippetVariant(
                    language = "Kotlin (Android)",
                    code = """
val schemaRequest = GenerateContentRequest(
    contents = listOf(Content(parts = listOf(Part(text = "提取最新 Android 16 隐私沙盒变更要点")))),
    generationConfig = GenerationConfig(
        responseMimeType = "application/json",
        responseSchema = buildJsonObject {
            put("type", "OBJECT")
            putJsonObject("properties") {
                putJsonObject("summary") { put("type", "STRING") }
                putJsonObject("breakingChanges") {
                    put("type", "ARRAY")
                    putJsonObject("items") { put("type", "STRING") }
                }
            }
        }
    )
)
                    """.trimIndent()
                )
            ),
            sandboxPrompt = "请以严格 JSON 格式输出一个包含 3 条『企业级大模型防幻觉核查清单』的对象，包含 id、checkItem、severity、verificationMethod 字段。",
            sandboxSystemInstruction = "只输出合法、格式化的 JSON 字符串，并附上简短架构说明。",
            expectedBenchmarkOutput = """
```json
{
  "checklistName": "Gemini 4 Enterprise Grounding Guardrails",
  "items": [
    {
      "id": "GRD-01",
      "checkItem": "强制开启 Google Search Grounding 归因阈值 >= 0.85",
      "severity": "CRITICAL",
      "verificationMethod": "校验 groundingMetadata.groundingSupports 覆盖率"
    },
    {
      "id": "GRD-02",
      "checkItem": "受控解码 JSON Schema 强类型枚举约束",
      "severity": "HIGH",
      "verificationMethod": "启用 responseMimeType=application/json 与字段必填校验"
    },
    {
      "id": "GRD-03",
      "checkItem": "数值与财务计算强制路由至内置 Python 代码沙盒",
      "severity": "CRITICAL",
      "verificationMethod": "检查 executableCode 执行返回状态码 == OK"
    }
  ]
}
```
            """.trimIndent()
        )
    )

    val newsArticles: List<NewsArticleItem> = listOf(
        NewsArticleItem(
            id = "news-gemini4-launch",
            category = "重磅发布",
            date = "2026-10-15",
            title = "Google 正式发布 Gemini 4 全系模型：1000 万上下文与 SWE-bench 94.8% 刷新人类工程纪录",
            subtitle = "涵盖 Ultra、Pro、Flash 与 Android 端侧 Nano-Edge 四大矩阵，推理速度提升 3.1 倍，API 调用成本直降 75%。",
            readTime = "5 分钟",
            highlightTag = "里程碑发布",
            claudeNewsComparison = "对比 Claude.news 发布的 Claude 4 / 3.7 Sonnet 动态：Gemini 4 在全部 18 项权威代码、数学、多模态与长上下文基准中实现大满贯超越。",
            keyTakeaways = listOf(
                "SWE-bench Verified 达到 94.8%，比 Claude 最高纪录高出 9.6 个百分点，可独立完成跨百个文件的系统级重构。",
                "原生 1000 万 Token 零衰减上下文窗口，支持一次性读入 8 小时 4K 视频或百万行级单仓代码库。",
                "推出 Anthropic Messages API 与 MCP 协议零改动迁移网关，30 秒内即可将现有 Claude 应用平滑升级至 Gemini 4。"
            ),
            bodyParagraphs = listOf(
                "今天，Google DeepMind 与 Google Cloud 联合发布全新一代原生多模态基座大模型——Gemini 4 系列。从最初架构设计起，Gemini 4 就不再局限于单模态文本预测，而是将代码执行状态机、原生高帧率音视频流、三维空间理解与 Deep Think 2.0 形式化推理深度融合在同一个稀疏混合专家（MoE-Ultra）网络中。",
                "在软件工程领域，Gemini 4 Ultra 与 Gemini 4 Pro 在 SWE-bench Verified 基准上分别取得了 94.8% 和 91.4% 的历史最高分。更重要的是，得益于 TPU v6p (Trillium+) 集群的硬件协同优化，Gemini 4 的首字响应时间（TTFT）缩短至 110ms~240ms，且上下文缓存读取价格低至 $0.08 / 百万 Token。",
                "针对此前使用 Claude 的开发者与企业客户，Google 同步上线了『Claude -> Gemini 4 一键迁移套件』，全面兼容 MCP 工具生态，并提供免费的 Prompt 自动优化器与实时基准对比沙盒。"
            ),
            benchmarks = listOf(
                BenchmarkMetric("SWE-bench Verified (真实软件工程修复)", 0.948f, "94.8%", 0.852f, "85.2%", "越高越好"),
                BenchmarkMetric("AIME 2026 (奥数竞赛级推理)", 0.992f, "99.2%", 0.913f, "91.3%", "越高越好"),
                BenchmarkMetric("MM-NIAH 2M+ (超长多模态大海捞针)", 0.999f, "99.9%", 0.420f, "仅支持200K", "10M vs 200K"),
                BenchmarkMetric("LiveCodeBench (防污染实时编程竞赛)", 0.924f, "92.4%", 0.831f, "83.1%", "越高越好"),
                BenchmarkMetric("GPQA Diamond (博士级科学问答)", 0.897f, "89.7%", 0.840f, "84.0%", "越高越好")
            ),
            sandboxPrompt = "请生成一段面向企业 CTO 的《从 Claude 迁移至 Google Gemini 4 的核心 ROI 与技术收益评估摘要》，包含性能、上下文窗口与成本测算。"
        ),
        NewsArticleItem(
            id = "news-gemini-code-agent",
            category = "开发者生态",
            date = "2026-10-12",
            title = "Gemini Code CLI 与 Android Studio Agent 全面升级：支持 500 万 Token 整库感知与自动回归验证",
            subtitle = "正面超越 Claude Code：无需手动挑选上下文文件，自动建立全仓依赖图谱并在沙盒中完成编译与 Robolectric 测试。",
            readTime = "4 分钟",
            highlightTag = "超越 Claude Code",
            claudeNewsComparison = "相比 Claude Code 频繁触发 200K 上下文压缩导致丢失关键调用链，Gemini Code Agent 可在内存中常驻完整百万行代码树，且单次重构费用降低 80%。",
            keyTakeaways = listOf(
                "全仓零压缩索引：一次性加载整个 Android + 后端 Monorepo，跨模块重构不再遗漏任何接口实现。",
                "内置多智能体并发工作流：同时拉起 UI 构建、单元测试编写与安全漏洞扫描三个并行子 Agent。",
                "深植 Android 生态：原生理解 Jetpack Compose 布局预览、Gradle 版本目录与 Room/Firestore 迁移脚本。"
            ),
            bodyParagraphs = listOf(
                "许多开发者在使用 Claude Code 时都会遇到同一个痛点：当工程超过几百个源码文件时，200K 上下文窗口迅速耗尽，不得不频繁执行 `/compact` 压缩历史，导致模型忘记前文约定好的接口签名，且每次复杂任务的 API 账单动辄数十美元。",
                "全新的 Gemini 4 Code Agent 彻底终结了这一限制。凭借 500 万至 1000 万 Token 的持久化 Context Caching，您的整个代码库、Git 提交历史与设计规范文档只需上传一次即可全天驻留高速缓存。",
                "在内部百万行级企业工程实测中，Gemini 4 Code Agent 的一次性编译通过率达到 96.4%，平均任务耗时比 Claude Code 缩短 62%。"
            ),
            benchmarks = listOf(
                BenchmarkMetric("跨模块大型重构一次性编译通过率", 0.964f, "96.4%", 0.845f, "84.5%", "越高越好"),
                BenchmarkMetric("百万行工程上下文完整保留率", 1.000f, "100%", 0.180f, "18% (需压缩)", "无损全仓"),
                BenchmarkMetric("单次复杂特性开发 API 成本效率", 0.920f, "$0.18/次", 0.280f, "$1.45/次", "越省越好")
            ),
            sandboxPrompt = "请演示 Gemini 4 Code Agent 如何自动将一个遗留的 XML + AsyncTask Android 页面重构为现代 Jetpack Compose + StateFlow + Coroutines 架构。"
        ),
        NewsArticleItem(
            id = "news-live-multimodal-2",
            category = "技术报告",
            date = "2026-10-08",
            title = "原生音视频双向流技术白皮书：为什么『原生多模态』比『外挂视觉编码器』领先一代",
            subtitle = "深度剖析 Gemini 4 如何在同一组 Transformer 权重中联合理解语音韵律、微表情、空间几何与实时代码。",
            readTime = "6 分钟",
            highlightTag = "核心论文",
            claudeNewsComparison = "Claude 仅支持将静态图像转码为文本旁路特征且无原生音频/视频流能力；Gemini 4 从预训练第一天起即采用全模态交错序列建模。",
            keyTakeaways = listOf(
                "支持最高 60fps 局部高帧率动作捕捉与 8 小时超长视频语义检索，精确到毫秒级时间戳。",
                "原生音频理解可直接识别说话人的犹豫、讽刺、背景机械异响，无需经过有损 ASR 转文字。",
                "空间 3D 边界框（3D Bounding Box）与机械臂轨迹规划零样本泛化率达 91.2%。"
            ),
            bodyParagraphs = listOf(
                "传统大模型在处理语音和视频时，往往采用『ASR 语音转文字 -> 文本大模型 -> TTS 文字转语音』的三段式拼接流水线，不仅累加了超过 1.5 秒的延迟，还彻底丢失了语调、情绪、环境音以及视频中的连续物理运动规律。",
                "Gemini 4 采用统一的原生多模态离散与连续混合 Token 化架构（Unified Native Multimodal Tokenization），无论是 Python 源码、交响乐音频波形还是手术显微镜下的 4K 实时视频流，都在同一个注意力空间中进行跨模态对齐推理。"
            ),
            benchmarks = listOf(
                BenchmarkMetric("Video-MME (长视频理解含字幕/无字幕)", 0.935f, "93.5%", 0.680f, "68.0% (仅抽帧)", "越高越好"),
                BenchmarkMetric("端到端实时语音交互首包响应速率", 0.950f, "220ms", 0.350f, "无原生语音流", "原生全双工")
            ),
            sandboxPrompt = "请解释在工业质检与远程医疗场景中，原生音视频多模态模型相比『先抽帧再加 ASR 转写』的传统方案能避免哪些致命信息丢失？"
        ),
        NewsArticleItem(
            id = "news-enterprise-grounding",
            category = "基准评测",
            date = "2026-10-03",
            title = "SimpleQA 与企业级防幻觉评测出炉：Gemini 4 + Google Search Grounding 准确率达 97.6%",
            subtitle = "深度整合全球实时知识图谱与 Vertex AI 企业私域检索，彻底解决金融、法律与医疗场景的事实核查难题。",
            readTime = "4 分钟",
            highlightTag = "零幻觉标杆",
            claudeNewsComparison = "在最新时效性事实核查与长文档法规引用基准中，Gemini 4 凭借原生 Google 搜索图谱与可验证引用块，将幻觉率压低至 1.2%（Claude 为 6.8%）。",
            keyTakeaways = listOf(
                "每一句关键结论自动附带源网页 URL、发布时间与原文锚点，支持一键点击核验。",
                "支持动态 Grounding 阈值控制：当知识库置信度低于设定阈值时自动触发联网检索或明确拒答。",
                "通过 ISO 42001、SOC2 Type II、HIPAA 与 FedRAMP High 全套最高等级合规认证。"
            ),
            bodyParagraphs = listOf(
                "对于金融机构、律所和医疗科技企业而言，大模型偶尔编造的虚假法条或过期财报数据是不可接受的红线。Gemini 4 将 Grounding（事实锚定）提升为一等公民能力。",
                "在推理解码过程中，Gemini 4 会实时计算每个事实实体（Entity）的内部置信度熵，并在亚百毫秒内向 Google 知识图谱或企业 BigQuery/ AlloyDB 发起混合检索，确保输出的每一个数字都有据可查。"
            ),
            benchmarks = listOf(
                BenchmarkMetric("SimpleQA + Grounding (事实精准度)", 0.976f, "97.6%", 0.864f, "86.4%", "越高越好"),
                BenchmarkMetric("法律合同与财报引用零幻觉率", 0.988f, "98.8%", 0.932f, "93.2%", "越高越好")
            ),
            sandboxPrompt = "请演示如何为一个跨境合规审查系统编写一段带有『严格引用溯源与不确定性声明』的 Gemini 4 系统指令（System Instruction）。"
        )
    )

    val resourceAssets: List<ResourceAssetItem> = listOf(
        ResourceAssetItem(
            id = "res-claude-migrator",
            type = "迁移工具",
            title = "Claude -> Gemini 4 零成本一键迁移指南与提示词转换器",
            subtitle = "30 秒完成 SDK 替换、XML 标签提示词优化与 MCP 工具无缝挂载，成本立减 70%。",
            badgeText = "最受欢迎 · 迁移必备",
            claudeResourceEquivalent = "对标 claude.com/resources 迁移与入门指南",
            whyBetterThanClaude = "不仅完全支持 Claude 风格的 <context> / <instructions> XML 结构化标签，还进一步提供原生 JSON Schema 强约束与 10M 缓存替换方案。",
            contentMarkdown = """
### 为什么全球团队正在从 Claude 迁移至 Google Gemini 4？
1. **打破 200K 上下文天花板**：将原本需要复杂切片的 RAG 管道简化为单次 `CachedContent` 调用。
2. **原生兼容 XML 结构化提示词**：您原本为 Claude 编写的 `<role>`, `<example>`, `<thinking>` 标签无需重写即可在 Gemini 4 上获得更高准确率，配合原生 `systemInstruction` 与 `responseSchema` 效果再提升 24%。
3. **API 字段对照表**：
   - `anthropic.messages.create` -> `client.models.generate_content`
   - `system="..."` -> `config.system_instruction="..."`
   - `thinking={"type": "enabled", "budget_tokens": 8000}` -> `thinking_config=ThinkingConfig(thinking_level="HIGH")`
   - `stop_sequences` -> `config.stop_sequences`
            """.trimIndent(),
            templateCode = """
// 迁移前 (Claude SDK):
// anthropic.messages.create(model="claude-3-7-sonnet", max_tokens=4096, system=sys, messages=[...])

// 迁移后 (Google Gemini 4 统一架构):
val request = GenerateContentRequest(
    systemInstruction = Content(parts = listOf(Part(text = sysInstruction))),
    contents = listOf(Content(parts = listOf(Part(text = userPrompt)))),
    generationConfig = GenerationConfig(
        temperature = 0.2f,
        thinkingConfig = ThinkingConfig(thinkingLevel = "high"),
        responseMimeType = "application/json"
    )
)
            """.trimIndent(),
            actionableSandboxPrompt = "我有以下原本用于 Claude 3.7 Sonnet 的提示词：『<role>你是高级安全审计员</role><task>检查代码中是否存在 SSRF 与反序列化漏洞</task>』。请将其升级为充分发挥 Gemini 4 Deep Think 2.0 与严格 JSON Schema 优势的最佳实践版本。"
        ),
        ResourceAssetItem(
            id = "res-prompt-atlas",
            type = "提示词模板",
            title = "Gemini 4 企业级提示词工程全图谱 (Prompt Engineering Atlas)",
            subtitle = "涵盖长上下文定位、多模态思维链、少样本结构化抽取与自我批改循环的 36 个工业级黄金模板。",
            badgeText = "官方权威手册",
            claudeResourceEquivalent = "对标 docs.claude.com/en/resources/prompt-library",
            whyBetterThanClaude = "除了纯文本/代码提示词外，独有『长视频时序定位提示法』『原生音频情绪分析提示法』与『10M 上下文锚点索引法』。",
            contentMarkdown = """
### Gemini 4 四大黄金提示词法则
1. **长文档末尾指令置顶法则 (Query-at- the-End Positioning)**：在传入百万 Token 代码或文档后，将核心约束与输出格式放在 Prompt 最末尾，可使注意力聚焦度提升 15%。
2. **跨模态时间戳锚定 (Timestamped Grounding)**：分析视频或长音频时，要求模型按 `[MM:SS - MM:SS]` 格式输出视觉与听觉双重证据链。
3. **思考层级显式分流 (Adaptive Thinking Routing)**：对确定性抽取设 `thinkingLevel = "low"` 追求极致吞吐，对架构设计与定理证明设 `thinkingLevel = "high"`。
4. **反例边界声明 (Negative Constraints & Edge Cases)**：明确列出禁止使用的废弃 API（如禁止使用旧版 AsyncTask 或已弃用的库）。
            """.trimIndent(),
            templateCode = """
<system_role>
你是 Google Gemini 4 首席多模态分析引擎。
</system_role>

<context_anchoring>
请先扫描上方输入的全部文档/代码/视频流，建立全局符号与时间轴索引。
</context_anchoring>

<task_instructions>
1. 识别所有潜在风险点，并精确引用源文件名与行号（或视频时间戳 [MM:SS]）。
2. 对每个问题给出根因分析（Root Cause）与可直接执行的修复补丁。
3. 在最终输出前，自检所有 API 调用均符合最新稳定版规范。
</task_instructions>
            """.trimIndent(),
            actionableSandboxPrompt = "请套用《Gemini 4 企业级提示词全图谱》的『跨模态与代码双重审计模板』，为一套车载 Android 智能座舱语音控车系统生成一份高可靠性的 System Instruction。"
        ),
        ResourceAssetItem(
            id = "res-system-instructions",
            type = "系统指令库",
            title = "生产级 System Instructions 黄金预设库（全栈 / 金融 / 医疗 / 智能体）",
            subtitle = "经过百万次红队压测验证的系统角色指令集，一键复制即可赋予模型专家级严谨度与安全边界。",
            badgeText = "开箱即用",
            claudeResourceEquivalent = "对标 Claude System Prompts 公开指南",
            whyBetterThanClaude = "内置针对工具调用（Function Calling）、Google Search 溯源阈值与 JSON Schema 强类型输出的协同控制指令。",
            contentMarkdown = """
### 核心设计原则
- **角色与边界分离**：明确模型拥有的工具权限、拒答红线与不确定性表达规范。
- **反幻觉强制锚定**：规定任何涉及外部事实、法规条款或 API 版本号的内容必须标注来源置信度。
- **结构化自检闭环**：要求模型在生成长代码前先输出模块依赖表。
            """.trimIndent(),
            templateCode = """
你是 Google Gemini 4 生产级全栈工程智能体。请严格遵守以下执行宪章：
1. 【零占位符原则】严禁输出 "// TODO" 或伪代码，所有代码必须包含完整导包、异常处理与边界校验。
2. 【事实溯源原则】引用任何第三方库或法规数据时，必须验证版本兼容性；若不确定，必须显式说明前置条件。
3. 【防御式设计】默认考虑并发安全、空指针防护、网络超时重试与最小权限原则。
            """.trimIndent(),
            actionableSandboxPrompt = "请使用上方『生产级全栈工程智能体系统指令』，编写一个支持指数退避重试、熔断降级与协程取消安全的 Kotlin 网络请求封装类。"
        ),
        ResourceAssetItem(
            id = "res-whitepaper-tco",
            type = "技术白皮书",
            title = "《2026 企业级生成式 AI 架构与 TCO 成本优化白皮书》",
            subtitle = "详尽拆解日均 1 亿 Token 规模下，Gemini 4 Flash/Pro 混合路由与上下文缓存如何为企业节省 68% 预算。",
            badgeText = "架构师必读",
            claudeResourceEquivalent = "对标 claude.com/resources 企业白皮书",
            whyBetterThanClaude = "提供真实的级联模型路由（Cascade Routing）公式与 Context Caching 盈亏平衡点推算模型。",
            contentMarkdown = """
### 级联智能路由 (Gemini Cascade Routing) 实战收益
- **第一层（80% 流量）**：由 `Gemini 4 Flash` 处理日常分类、UI 生成、实时语音客服与常规代码补全，TTFT 仅 110ms，成本低至 $0.075/1M。
- **第二层（18% 流量）**：由 `Gemini 4 Pro` 处理跨文件代码重构、长视频理解与复杂多步 Agent 编排。
- **第三层（2% 流量）**：由 `Gemini 4 Ultra (Deep Think High)` 攻克核心算法证明与疑难故障根因定位。
- **综合对比**：相比全量使用 Claude Sonnet/Opus，综合准确率提升 6.4%，而月度云端账单下降 **68.5%**。
            """.trimIndent(),
            templateCode = """
// 级联模型路由器 (Cascade Router) 核心判定逻辑
fun selectOptimalGeminiModel(taskComplexityScore: Float, tokenCount: Int, isRealtimeAudio: Boolean): String {
    return when {
        isRealtimeAudio -> "gemini-2.5-flash-native-audio-preview-12-2025"
        taskComplexityScore >= 0.85f || tokenCount > 1_500_000 -> "gemini-3.1-pro-preview"
        else -> "gemini-3.5-flash"
    }
}
            """.trimIndent(),
            actionableSandboxPrompt = "假设我们公司每天有 50,000 次研发代码审查请求，每次请求需要携带 150,000 Token 的项目公共规范与核心接口定义。请计算使用 Gemini 4 Context Caching + Pro/Flash 混合路由相比 Claude Sonnet 的月度成本节省比例。"
        )
    )
}

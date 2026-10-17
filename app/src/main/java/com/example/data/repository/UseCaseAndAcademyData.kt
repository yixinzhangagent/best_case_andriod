package com.example.data.repository

import com.example.data.model.AcademyCourseItem
import com.example.data.model.CourseLesson
import com.example.data.model.QuizQuestion
import com.example.data.model.UseCaseItem

object UseCaseAndAcademyData {

    val academyCourses: List<AcademyCourseItem> = listOf(
        AcademyCourseItem(
            id = "course-101-foundations",
            track = "全栈 AI 工程师",
            level = "必修基础 · L100",
            title = "Gemini 4 核心架构与多模态提示词工程实战",
            subtitle = "掌握原生多模态输入、Deep Think 算力预算调控、结构化 JSON Schema 与防幻觉事实锚定。",
            duration = "45 分钟",
            lessonCount = 3,
            claudeAcademyEquivalent = "对标 academy.claude.com《Claude Prompt Engineering Foundations》",
            googleAdvantage = "比 Claude 学院课程新增了原生音视频流提示词设计、10M 超长上下文定位技巧与受控解码状态机实验。",
            lessons = listOf(
                CourseLesson(
                    lessonNumber = 1,
                    title = "第一课：原生多模态注意力机制与提示词结构化设计",
                    duration = "15 分钟",
                    coreConcepts = listOf("原生跨模态对齐", "System Instruction 宪章模式", "长文本末尾指令锚定"),
                    detailContent = "在 Gemini 4 中，图像、音频、视频、PDF 和代码共享同一个语义表示空间。编写高质量提示词的核心在于：用 systemInstruction 锁定全局专家人格与输出红线，用结构化分段（XML 标签或 Markdown 标题）隔离长背景材料，并将最核心的判别指令放置在上下文最末端。",
                    codeLabSnippet = """
val system = "你是严谨的架构评审官，任何结论必须引用输入文档的具体章节号。"
val prompt = "<architecture_doc>...[百万字架构文档]...</architecture_doc>\n请列出上述架构在跨地域多活容灾中的 3 个单点故障风险。"
                    """.trimIndent(),
                    sandboxPrompt = "请示范如何用『System Instruction + XML 边界标签 + 末尾强约束指令』重构一段模糊的用户提问：『帮我看看这段登录代码安不安全』。"
                ),
                CourseLesson(
                    lessonNumber = 2,
                    title = "第二课：Deep Think 2.0 思考层级调控与数学/代码自验证",
                    duration = "15 分钟",
                    coreConcepts = listOf("Thinking Level 四级调控", "内置代码执行验算", "推理延迟与准确率平衡"),
                    detailContent = "并非所有请求都需要消耗长推理时间。Gemini 4 允许开发者按单次请求动态指定 thinkingLevel（minimal / low / medium / high）。当开启 high 模式并挂载 code_execution 工具时，模型会在输出给用户前自动编写 Python 脚本验算复杂公式与边界条件。",
                    codeLabSnippet = """
generationConfig = GenerationConfig(
    thinkingConfig = ThinkingConfig(thinkingLevel = "high"),
    temperature = 0.2f
)
                    """.trimIndent(),
                    sandboxPrompt = "请对比说明：在处理『实时语音闲聊』与『跨 20 个微服务的分布式事务死锁排查』时，应该如何分别配置 Gemini 4 的 thinkingLevel 与 temperature？"
                ),
                CourseLesson(
                    lessonNumber = 3,
                    title = "第三课：100% 类型安全的 JSON Schema 受控生成",
                    duration = "15 分钟",
                    coreConcepts = listOf("有限状态机受控解码", "枚举与嵌套对象约束", "彻底消除解析异常"),
                    detailContent = "还在用正则表达式从模型输出里抠 JSON 吗？通过在 GenerationConfig 中声明 responseMimeType = \"application/json\" 与 responseSchema，Gemini 4 在底层 Token 采样阶段直接屏蔽任何会导致语法非法或字段缺失的候选词，实现生产环境 100% 零报错反序列化。",
                    codeLabSnippet = """
GenerationConfig(
    responseMimeType = "application/json",
    responseSchema = buildJsonObject {
        put("type", "OBJECT")
        putJsonObject("properties") {
            putJsonObject("riskScore") { put("type", "INTEGER") }
            putJsonObject("verdict") {
                put("type", "STRING")
                putJsonArray("enum") { add("APPROVE"); add("REJECT"); add("MANUAL_REVIEW") }
            }
        }
    }
)
                    """.trimIndent(),
                    sandboxPrompt = "请为一个『自动化简历初筛与岗位匹配度评估系统』设计一套严格的 Gemini 4 JSON Schema，并展示示例输出。"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    question = "当向 Gemini 4 输入 200 万 Token 的大型代码库或长文档时，核心任务指令放在哪个位置能获得最佳注意力聚焦？",
                    options = listOf(
                        "放在整段长文档的最开头，后面紧跟 200 万 Token 数据",
                        "放在长文档与多模态数据块的最末尾（配合 systemInstruction 设定全局角色）",
                        "拆成 50 个小请求分别发送",
                        "不需要任何指令结构"
                    ),
                    correctIndex = 1,
                    explanation = "正确！根据 Gemini 4 长上下文最佳实践，使用 systemInstruction 锁定全局角色，并将具体问题与输出约束放在长上下文数据块的末尾（Query-at-the-End），注意力召回率可达 99.94%。"
                ),
                QuizQuestion(
                    question = "Gemini 4 相比 Claude 在保证 JSON 结构化输出时不出错的核心机制是什么？",
                    options = listOf(
                        "靠在提示词里反复强调『请不要输出多余文字』",
                        "出错后在客户端用正则替换修复",
                        "底层解码器基于 responseSchema 进行有限状态机受控采样（Constrained Decoding）",
                        "不支持 JSON 输出"
                    ),
                    correctIndex = 2,
                    explanation = "正确！Gemini 4 的 responseSchema 在 Token 采样层直接屏蔽不符合 Schema 语法树的 Token，从数学上保证 100% 合法 JSON。"
                )
            )
        ),
        AcademyCourseItem(
            id = "course-201-agents",
            track = "企业级 Agent 架构师",
            level = "高阶进阶 · L200",
            title = "构建自主多智能体系统：MCP 工具生态与 Google A2A 协议",
            subtitle = "从单智能体 Function Calling 演进到跨团队多智能体编排、人机回环审批与长周期状态记忆。",
            duration = "60 分钟",
            lessonCount = 3,
            claudeAcademyEquivalent = "对标 academy.claude.com《Building Effective Agents & Tool Use》",
            googleAdvantage = "不仅完整覆盖 Anthropic 的 Orchestrator-Workers 模式与 MCP 协议，更独创跨网络 Agent-to-Agent (A2A) 发现与协商机制。",
            lessons = listOf(
                CourseLesson(
                    lessonNumber = 1,
                    title = "第一课：高可靠并行 Function Calling 与工具契约设计",
                    duration = "20 分钟",
                    coreConcepts = listOf("原子化工具签名", "并行工具调用", "错误自愈反馈循环"),
                    detailContent = "优秀的 Agent 取决于清晰的工具边界。为每个 FunctionDeclaration 编写包含前置条件、单位说明与枚举取值的描述，可让 Gemini 4 的一次工具调用准确率提升至 98.9%。当用户查询涉及多个独立数据源时，Gemini 4 会在单个 Turn 内并发返回多个 functionCall。",
                    codeLabSnippet = """
// Gemini 4 支持在单轮响应中并发触发多个工具调用：
// 1. getCustomerOrderHistory(userId = "U8821")
// 2. checkWarehouseInventory(sku = "PIXEL-10-PRO")
// 3. calculateRefundPolicy(orderDate = "2026-10-01")
                    """.trimIndent(),
                    sandboxPrompt = "请为一个『企业差旅自动改签与财务合规核验智能体』设计 3 个正交、高内聚的 Function Declaration 工具定义。"
                ),
                CourseLesson(
                    lessonNumber = 2,
                    title = "第二课：兼容 MCP 与升级 Google A2A 多智能体协同拓扑",
                    duration = "20 分钟",
                    coreConcepts = listOf("MCP 统一数据源挂载", "A2A Agent Card 能力发现", "层级监督与博弈辩论模式"),
                    detailContent = "通过 MCP (Model Context Protocol)，Gemini 4 可以即插即用连接本地 Git、PostgreSQL、Linear 与 Slack；而当任务跨越不同部门或安全域时，Google A2A 协议允许主控 Agent 通过标准 Agent Card 发现远端专家 Agent，进行异步任务委派与状态流式同步。",
                    codeLabSnippet = """
// A2A Agent Card 能力声明示例
{
  "agentName": "SecurityRedTeamAgent",
  "protocol": "google-a2a/v1",
  "capabilities": ["sast_scan", "dependency_cve_audit", "patch_verification"],
  "endpoint": "https://agents.internal.corp/security-redteam"
}
                    """.trimIndent(),
                    sandboxPrompt = "请对比 MCP（模型-工具协议）与 Google A2A（智能体-智能体协议）在企业架构中的分工边界，并给出二者结合的落地方案。"
                ),
                CourseLesson(
                    lessonNumber = 3,
                    title = "第三课：Computer Use 2.0 与沙盒安全护栏 (Guardrails)",
                    duration = "20 分钟",
                    coreConcepts = listOf("GUI 视觉坐标动作空间", "高危操作人机确认 (HITL)", "防间接提示词注入"),
                    detailContent = "当 Agent 具备操作浏览器 DOM 或 Android 屏幕 UI 的能力时，必须防止网页或邮件中的恶意隐藏指令（Indirect Prompt Injection）。Gemini 4 内置双层指令隔离墙，并对涉及资金划转、删除生产库等高危动作强制触发人机确认签名。",
                    codeLabSnippet = """
if (toolCall.name in HIGH_RISK_ACTIONS) {
    requireHumanApprovalModal(toolCall.args) // 强制人机回环确认
}
                    """.trimIndent(),
                    sandboxPrompt = "如何设计一套防御机制，防止具备邮件读取与转账工具权限的 Agent 被一封包含隐藏恶意指令的钓鱼邮件诱导执行未授权操作？"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    question = "在企业多智能体架构中，MCP 协议与 Google A2A 协议的最佳组合方式是什么？",
                    options = listOf(
                        "两者完全冲突，只能二选一",
                        "使用 MCP 让单个 Agent 连接数据库/API 等确定性工具，使用 A2A 让多个自治 Agent 跨领域协同通信",
                        "只在测试环境使用",
                        "把所有逻辑塞进单条 200 字提示词里"
                    ),
                    correctIndex = 1,
                    explanation = "正确！MCP 解决『Agent 如何调用工具与上下文资源』，而 A2A 解决『多个独立 Agent 如何彼此发现、协商与委派任务』，二者相辅相成。"
                )
            )
        ),
        AcademyCourseItem(
            id = "course-301-context-rag",
            track = "全栈 AI 工程师",
            level = "架构专精 · L300",
            title = "1000 万 Token 混合架构：Context Caching 2.0 与 Search Grounding",
            subtitle = "颠覆传统碎片化 RAG，构建亚秒级响应、零幻觉溯源的企业知识大脑。",
            duration = "50 分钟",
            lessonCount = 2,
            claudeAcademyEquivalent = "对标 academy.claude.com《Retrieval Augmented Generation & Caching》",
            googleAdvantage = "容量是 Claude 的 50 倍，支持音视频原件直接入库缓存，且原生集成 Google Search 与 Vertex AI 实时锚定。",
            lessons = listOf(
                CourseLesson(
                    lessonNumber = 1,
                    title = "第一课：何时用长上下文缓存 (Cache) 替代向量切片 (Chunking RAG)",
                    duration = "25 分钟",
                    coreConcepts = listOf("长上下文 vs 向量检索决策树", "缓存前缀拓扑设计", "成本盈亏平衡点测算"),
                    detailContent = "当企业知识库小于 1000 万 Token（约相当于 750 万汉字或 40,000 页源码）且需要跨章节全局推理时，直接将其放入 Gemini 4 Context Cache 可彻底消除切片丢失上下文的问题，且从第 2 次查询起成本即低于自建高可用向量集群。",
                    codeLabSnippet = """
// 黄金架构公式：
// 热数据 (<10M Tokens, 高频跨篇推理) -> Gemini 4 Context Caching
// 海量冷数据 (>100M Tokens, 历史归档) -> BigQuery Vector Search 粗排 + Gemini 4 500K 精排精读
                    """.trimIndent(),
                    sandboxPrompt = "请为我们律所的 3,000 份历史并购合同（约 600 万 Token）设计一套结合 Gemini 4 Context Caching 与增量检索的混合架构方案。"
                ),
                CourseLesson(
                    lessonNumber = 2,
                    title = "第二课：动态事实核查与可验证引用块 (Grounding Chunks)",
                    duration = "25 分钟",
                    coreConcepts = listOf("Grounding Support 置信度评分", "句级源码高亮回溯", "冲突证据消歧"),
                    detailContent = "通过解析响应中的 groundingMetadata，前端 UI 可以将模型生成的每一句话与原始 PDF 页码、代码行号或实时权威网页链接精确绑定，让用户点击角标即可核查原始出处。",
                    codeLabSnippet = """
val supports = response.groundingMetadata?.groundingSupports
supports?.forEach { support ->
    println("声明片段: ${'$'}{support.segment.text} -> 证据源索引: ${'$'}{support.groundingChunkIndices}")
}
                    """.trimIndent(),
                    sandboxPrompt = "请生成一段支持『句级证据溯源角标 [来源#1]』与『多源数据冲突告警』的投资尽调报告生成提示词。"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    question = "对于一个总量为 400 万 Token 的核心产品代码库与架构文档，哪种架构能提供最高的跨模块问答准确率？",
                    options = listOf(
                        "按每 500 个字切碎存入向量库，每次只取 Top-3 片段喂给 200K 窗口模型",
                        "使用 Gemini 4 Context Caching 一次性全量缓存 400 万 Token，保留 100% 全局调用链关系",
                        "只保留文件名删除所有代码实现",
                        "使用正则搜索"
                    ),
                    correctIndex = 1,
                    explanation = "正确！400 万 Token 完全在 Gemini 4 的单次上下文容量内，全量缓存可保留完整的跨文件引用关系，避免切片断裂。"
                )
            )
        ),
        AcademyCourseItem(
            id = "course-401-android-edge",
            track = "Android 端侧 AI 专家",
            level = "Google 独家 · L300",
            title = "Android AICore 与端云协同混合推理架构（Claude 无此能力）",
            subtitle = "利用 Gemini 4 Nano-Edge 实现手机端侧零流量、毫秒级隐私推理，并与云端 Gemini 4 Pro 无缝协同。",
            duration = "40 分钟",
            lessonCount = 2,
            claudeAcademyEquivalent = "Claude 学院无对应课程（Claude 无端侧模型与移动操作系统级集成）",
            googleAdvantage = "Google 独有的操作系统级壁垒：Android AICore 系统服务共享模型权重，应用无需打包数 GB 权重即可免费调用本地 NPU。",
            lessons = listOf(
                CourseLesson(
                    lessonNumber = 1,
                    title = "第一课：Android AICore 共享系统模型与隐私不出端架构",
                    duration = "20 分钟",
                    coreConcepts = listOf("AICore 系统级常驻", "LoRA 微调权重热加载", "飞行模式 100% 可用"),
                    detailContent = "在搭载 Android 15/16 的旗舰设备上，Gemini 4 Nano 由系统级 AICore 统一托管在安全飞地（Trusted Execution Environment）中。您的 App 零体积膨胀即可实现本地通话摘要、相册语义搜索与键盘智能润色，敏感隐私数据永远不离开手机。",
                    codeLabSnippet = """
// 端云智能分流器：隐私敏感或离线时走本地 Nano，重度复杂推理走云端 Flash/Pro
val useLocalNano = !networkMonitor.isOnline() || request.containsSensitivePii
                    """.trimIndent(),
                    sandboxPrompt = "请设计一个 Android 端云协同记账与合同扫描 App 的架构：哪些功能由端侧 Gemini 4 Nano 处理，哪些功能路由给云端 Gemini 4 Pro？"
                ),
                CourseLesson(
                    lessonNumber = 2,
                    title = "第二课：Jetpack Compose 流式生成式 UI (Generative UI)",
                    duration = "20 分钟",
                    coreConcepts = listOf("结构化流式增量渲染", "动态 Compose 卡片组装", "60fps 零卡顿状态更新"),
                    detailContent = "不要只把大模型输出渲染成枯燥的聊天气泡！利用 Gemini 4 的流式结构化输出，边接收 JSON 增量数据边在 Jetpack Compose 中动态组装交互式图表、可勾选任务卡与代码对比视图。",
                    codeLabSnippet = """
// 将 Gemini 4 返回的结构化组件树实时映射为 Compose 声明式 UI
when (uiBlock.type) {
    "BENCHMARK_CHART" -> InteractiveBenchmarkCard(uiBlock.metrics)
    "CODE_DIFF" -> SyntaxHighlightedDiffCard(uiBlock.before, uiBlock.after)
}
                    """.trimIndent(),
                    sandboxPrompt = "请给出在 Jetpack Compose 中处理 SSE 流式文本输出并避免高频重组（Recomposition）卡顿的 3 个关键性能优化技巧。"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    question = "为什么使用 Android AICore 调用端侧 Gemini 4 Nano 不会导致每个 App 安装包体积增加数 GB？",
                    options = listOf(
                        "因为模型是在云端运行的",
                        "因为 Android 操作系统底层通过 AICore 系统服务统一托管基座权重，多个 App 共享本地 NPU 推理服务",
                        "因为没有任何模型参数",
                        "因为只支持 10 个词"
                    ),
                    correctIndex = 1,
                    explanation = "正确！Android AICore 在操作系统层共享维护 Gemini Nano 权重与硬件加速，彻底解决了移动端分发大模型包体过大的痛点。"
                )
            )
        )
    )

    val useCases: List<UseCaseItem> = listOf(
        UseCaseItem(
            id = "uc-monorepo-refactoring",
            category = "编程与软件工程",
            title = "百万行级遗留代码库全量架构升级与自动补丁验证",
            subtitle = "一次性读入整个企业级 Monorepo，自动识别跨模块废弃 API、生成重构补丁并在沙盒中跑通全部单元测试。",
            difficulty = "工业级实战",
            roiMetric = "研发周期缩短 78% · 零回归故障",
            claudeUseCaseUrl = "academy.claude.com/use-cases/code-modernization",
            whyGeminiWins = listOf(
                "5M~10M 上下文一次性容纳全部源码与构建脚本，不像 Claude 200K 因切片丢失跨包调用关系。",
                "内置免费 Python / Bash 沙盒验算能力，SWE-bench Verified 94.8% 远超 Claude 85.2%。",
                "结合 Context Caching，团队连续 100 次追问重构细节的 API 成本仅为 Claude 的 12%。"
            ),
            workflowSteps = listOf(
                "1. 全仓快照缓存：将源码树、build.gradle.kts 与架构规范注入 Gemini 4 Context Cache。",
                "2. 全局依赖图分析：开启 Deep Think High 模式，输出受影响模块拓扑与迁移顺序。",
                "3. 原子化补丁生成：按模块输出严格符合编译规范的 Diff 补丁与配套 Robolectric 测试用例。",
                "4. 自动编译与自愈：若测试断言失败，自动捕获堆栈并在单轮会话内完成自愈修复。"
            ),
            systemPrompt = "你是 Google 首席软件现代化重构引擎。你必须保持 100% 向后兼容的公共接口契约，所有异步逻辑统一迁移至 Kotlin Coroutines + Flow，并附带完整边界单元测试。",
            userPromptTemplate = """
请将以下遗留的回调式（Callback Hell）订单同步管理器重构为线程安全的 Kotlin Coroutines + StateFlow 实现，并处理网络抖动时的指数退避重试与幂等性去重：
```kotlin
class LegacyOrderSync {
    fun syncOrders(orders: List<String>, cb: (Boolean, String?) -> Unit) {
        Thread { /* 旧版阻塞线程与无锁共享状态 */ }.start()
    }
}
```
            """.trimIndent(),
            sampleOutput = """
### Gemini 4 全量重构方案（含幂等去重 + 指数退避 + 完整协程安全）

```kotlin
class ModernOrderSyncManager(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val _syncState = MutableStateFlow<SyncUiState>(SyncUiState.Idle)
    val syncState: StateFlow<SyncUiState> = _syncState.asStateFlow()

    private val inFlightKeys = ConcurrentHashMap.newKeySet<String>()

    suspend fun syncOrders(orderIds: List<String>): Result<Int> = withContext(dispatcher) {
        val uniqueIds = orderIds.filter { inFlightKeys.add(it) }
        if (uniqueIds.isEmpty()) return@withContext Result.success(0)

        _syncState.value = SyncUiState.Running(total = uniqueIds.size)
        try {
            retryWithExponentialBackoff(maxAttempts = 3, initialDelayMs = 200L) {
                performBatchTransaction(uniqueIds)
            }.also {
                _syncState.value = SyncUiState.Success(syncedCount = uniqueIds.size)
            }
        } catch (e: CancellationException) {
            throw e // 严守协程结构化并发取消语义
        } catch (e: Exception) {
            _syncState.value = SyncUiState.Error(e.message ?: "Sync failed")
            Result.failure(e)
        } finally {
            uniqueIds.forEach { inFlightKeys.remove(it) }
        }
    }
}
```
            """.trimIndent(),
            recommendedModel = "Gemini 4 Pro (gemini-3.1-pro-preview)"
        ),
        UseCaseItem(
            id = "uc-video-surgical-qa",
            category = "多模态创作与视频",
            title = "4 小时高清视频流与音频同步理解：产品发布会/工业产线/手术复盘",
            subtitle = "无需抽帧转文字！直接输入数小时 MP4 视频与原生多轨音频，毫秒级定位关键动作、异常异响与金句切片。",
            difficulty = "多模态旗舰",
            roiMetric = "视频审核与切片效率提升 25 倍",
            claudeUseCaseUrl = "academy.claude.com/use-cases/multimodal-analysis",
            whyGeminiWins = listOf(
                "Claude 完全不支持原生视频流与音频波形输入，只能上传最高 20 张静态截图。",
                "Gemini 4 原生支持最长 8 小时视频连续动作与声音联合推理，Video-MME 得分 93.5% 行业第一。",
                "精确输出 `[HH:MM:SS]` 时间戳区间、画面三维空间关系与语音情绪转折。"
            ),
            workflowSteps = listOf(
                "1. 视频原件直传：通过 Files API 或多模态流直接传入数小时高清视频，无需 FFmpeg 手动抽帧。",
                "2. 视听跨模态对齐：同时检索画面中的视觉动作（如机械臂抖动、PPT 翻页）与音频中的语音/告警声。",
                "3. 结构化时间轴导出：按 JSON Schema 输出高光片段时间戳、多语种字幕与改进建议。"
            ),
            systemPrompt = "你是 Google Gemini 4 原生多模态视频结构化分析引擎。请输出精确到秒的时间戳，并同时结合视觉画面细节与音频语调变化给出证据链。",
            userPromptTemplate = "分析一段 90 分钟的智能手机跌落测试与拆解实验室视频：请生成结构化时间轴，标出屏幕出现微裂纹的精确帧时刻、冲击声分贝峰值特征，以及主讲工程师语气转为惊讶的片段。",
            sampleOutput = """
### 原生多模态视听联合分析报告

- **[00:14:22 - 00:14:25] 跌落冲击与声学特征**
  - **视觉轨迹**：手机以右下方 45° 角接触花岗岩测试台，第 `00:14:22.480` 帧在中框钛合金倒角处出现 0.3mm 形变，但超瓷晶面板未见裂纹。
  - **原生音频特征**：撞击瞬间捕获到 3.8kHz 高频清脆金属回音（无玻璃碎裂特有的 8kHz~12kHz 宽频散射噪声）。
- **[00:48:10 - 00:49:05] 拆解显微镜特写与情绪拐点**
  - **视觉画面**：显微镜放大至 50 倍展示主板均热板（VC）微水道结构。
  - **语音韵律分析**：主讲工程师语速加快 22%，基频上升，带有明显赞叹语气：「注意看这里双层毛细铜网的焊接密度，完全超出了预期」。
            """.trimIndent(),
            recommendedModel = "Gemini 4 Pro / Ultra"
        ),
        UseCaseItem(
            id = "uc-financial-due-diligence",
            category = "金融法律合规",
            title = "跨国并购 500 份财报与合同交叉尽调（零幻觉引用 + 代码验算）",
            subtitle = "一次性装入近 5 年全部审计底稿、扫描件 PDF 与监管新规，自动交叉核验表内表外负债并由内置 Python 沙盒重算全部财务比率。",
            difficulty = "高合规严苛场景",
            roiMetric = "尽调耗时从 2 周压缩至 18 分钟 · 计算准确率 100%",
            claudeUseCaseUrl = "academy.claude.com/use-cases/financial-analysis",
            whyGeminiWins = listOf(
                "原生 PDF 视觉布局理解 + 10M 上下文，可同时比对 500 份合同原件中的印章、手写批注与跨表勾稽关系。",
                "所有 IRR、NPV、EBITDA 调整项自动调用内置 Python 沙盒精确计算，杜绝纯文本模型心算进位错误。",
                "内置 Google Search Grounding 自动核验最新反垄断与跨境税务法规变动。"
            ),
            workflowSteps = listOf(
                "1. 原生多文档装入：将数百份 PDF 财报、补充协议与债务清单直接注入同一个长上下文窗口。",
                "2. 自动启动代码验算：模型识别所有财务表格后，在内置 Python 环境中编写 Pandas 脚本交叉复核现金流平衡。",
                "3. 风险条款红线扫描：定位所有控制权变更（Change of Control）触发赔偿条款与隐性担保。",
                "4. 句级溯源报告生成：每一处数字与结论均绑定 `[文件#页码#段落]` 精确引用。"
            ),
            systemPrompt = "你是顶级投行并购尽调与法务合规审计首席智能体。所有财务指标必须通过 Python 复核公式，任何风险提示必须附带原合同条款编号。",
            userPromptTemplate = "在标的公司的 2025 年 Q4 审计附注中，经营性现金流净额为 4.2 亿元，但应收账款周转天数从 42 天激增至 118 天，且前五大客户中新增了两家成立不足 6 个月的关联贸易商。请进行交叉财务舞弊风险排查并给出尽调核查清单。",
            sampleOutput = """
### 并购财务交叉尽调与舞弊风险排查报告

1. **核心勾稽异常量化验算（Python 沙盒复核通过）**
   - 应收账款周转天数（DSO）同比暴增 **+180.9%**（42天 $\to$ 118天），意味约 **3.15 亿元** 账面营收并未转化为实质回款，存在典型『期末突击塞渠道（Channel Stuffing）』特征。
   - 若剔除两家新增关联贸易商的授信销售额，经调整后的真实自由现金流（FCF）实为 **-0.68 亿元**。

2. **重点穿透核查指令单**
   - **[核查项 #A1 - 穿透终端销售]**：调取两家新设贸易商的海关提单、物流签收单及最终端客户完税凭证，排查『体外资金池自买自卖虚增收入』。
   - **[核查项 #A2 - 对赌协议触发条款]**：核查《补充协议三》第 14.2 条关于『关联交易占比超过 15% 时的一票否决与估值回拨（Clawback）机制』。
            """.trimIndent(),
            recommendedModel = "Gemini 4 Ultra (Deep Think High)"
        ),
        UseCaseItem(
            id = "uc-customer-live-agent",
            category = "客户体验与智能体",
            title = "全双工实时语音 + 视觉共享智能客服（220ms 响应 + 零配置工具调用）",
            subtitle = "客户在手机端直接开启摄像头对准故障家电或账单，Gemini 4 边看画面边用自然中文语音引导排查，并同步在后台自动创建维修工单。",
            difficulty = "实时多模态交互",
            roiMetric = "一次解决率 (FCR) 提升至 92% · 人工转接率下降 64%",
            claudeUseCaseUrl = "academy.claude.com/use-cases/customer-support-agent",
            whyGeminiWins = listOf(
                "Claude 仅支持传统文字聊天机器人；Gemini 4 Live API 原生支持实时视频流诊断 + 语音对话 + 随时打断。",
                "原生识别用户焦急情绪并自动切换安抚语调，同时在后台并发执行 ERP 查单与退换货接口。",
                "Gemini 4 Flash 成本极低，单通 5 分钟全模态音视频客服通话成本不足 $0.02。"
            ),
            workflowSteps = listOf(
                "1. 建立 Multimodal Live 双向流连接，实时接收客户麦克风音频与摄像头画面。",
                "2. 视觉故障识别：自动识别路由器指示灯闪烁频率或咖啡机错误代码屏幕。",
                "3. 并行工具触发：在语音对话不中断的同时，调用 checkWarrantyStatus 与 scheduleTechnician 接口。",
                "4. 会话结束秒级生成结构化 CRM 纪要与情感满意度评分。"
            ),
            systemPrompt = "你是温暖、专业、高效的 Google Gemini 4 全模态客户服务专家。当识别到客户语气焦急时先共情安抚，并结合视觉画面给出一步步极简操作指引。",
            userPromptTemplate = "客户打开摄像头对准一台智能浓缩咖啡机，画面显示压力表指针卡在 15 Bar 红区且蒸汽喷嘴滴水，客户着急地说：『我明天早上要招待重要客户，现在机器完全不出咖啡还一直响，怎么办？』",
            sampleOutput = """
### Gemini 4 Multimodal Live 实时双通道响应

1. **实时语音播报轨（温暖安抚语调 · 延迟 190ms）**
   「您别着急，我从画面里看到压力表目前停在 15 Bar 红区，这是萃取头细粉堵塞触发了安全过压保护，机器本身没有损坏，我们 2 分钟就能帮您恢复正常，绝对不耽误您明天的接待！」

2. **实时 AR 视觉引导标注**
   - 在画面右下角『泄压阀旋钮』生成高亮绿色引导框：*第一步：请逆时针缓慢旋转此旋钮半圈释放残余蒸汽压*。
   - 在冲煮手柄处生成蓝色引导框：*第二步：待压力表归零后取下手柄，用温水冲洗滤网背面*。

3. **后台并发 Function Calling 已自动执行**
   - `checkDeviceTelemetry(deviceId="ESP-9920")` $\to$ 确认水泵电机温度正常。
   - `sendMaintenanceKitCoupon(userId="U302", item="除垢清洁片")` $\to$ 已向客户账户免费派发快速清洁包。
            """.trimIndent(),
            recommendedModel = "Gemini 4 Flash Live (gemini-3.5-flash)"
        ),
        UseCaseItem(
            id = "uc-deep-research-scientist",
            category = "深度研究与分析",
            title = "自主多步科研文献综述、实验假设生成与生物/材料分子验证",
            subtitle = "结合 Google Search Scholar 实时学术检索与 AlphaFold 3 / 材料晶体工具链，自动阅读 300 篇最新顶刊论文并生成可执行验证方案。",
            difficulty = "科研前沿",
            roiMetric = "文献调研与实验设计提速 40 倍",
            claudeUseCaseUrl = "academy.claude.com/use-cases/research-synthesis",
            whyGeminiWins = listOf(
                "GPQA Diamond 博士级科学评测 89.7% 领先 Claude，且支持一次性通读 300 篇完整含图表 PDF 论文。",
                "精准解析论文中的复杂矢量反应机理图、电镜图与热力学相图，不漏掉任何补充材料（Supplementary Info）数据。",
                "内置 Google Search Grounding 实时抓取当天最新发表的 arXiv 与 Nature/Science 预印本。"
            ),
            workflowSteps = listOf(
                "1. 多跳学术检索规划：自动拆解研究课题，并发检索最新顶刊 PDF 与专利库。",
                "2. 图表与实验数据对齐：从数百篇论文的实验折线图与表格中抽取标准化数据集。",
                "3. 矛盾假说辨析：对比不同课题组实验条件差异，指出潜在混杂变量。",
                "4. Python 统计元分析（Meta-Analysis）：自动编写脚本计算效应量与置信区间。"
            ),
            systemPrompt = "你是跨学科首席科研智能体（AI Co-Scientist）。请严守科学证伪原则，区分『已证实实验结论』与『待验证推论』，并给出可量化的对照组实验设计。",
            userPromptTemplate = "请针对固态锂金属电池在低温（-30°C）下硫化物固态电解质/锂负极界面枝晶生长与阻抗激增问题，综合最新表面改性策略，提出一套具有创新性且可在实验室验证的双层梯度人工 SEI 膜构建方案。",
            sampleOutput = """
### AI Co-Scientist 固态电池低温界面工程研究方案

1. **核心机理瓶颈诊断**
   在 -30°C 下，Li₆PS₅Cl 硫化物电解质与金属锂界面处的去溶剂化能垒与界面电荷转移阻抗（R_ct）呈阿伦尼乌斯指数激增，局部电流密度不均诱发沿晶界穿透的锂枝晶。

2. **创新双层梯度人工 SEI 膜假设 (Gradient LiF / Li-In-Sn Alloy Interlayer)**
   - **顶层（亲电解质电子绝缘层）**：纳米级富 LiF-Li₃N 复合层（厚度 ~15 nm），提供高界面能（抑制枝晶机械刺穿）与高离子电导通道。
   - **底层（亲锂固溶体缓冲层）**：原位磁控溅射超薄 In-Sn 双金属层，反应生成零体积应变隐患的 LiₓIn / LiᵧSn 固溶体相，将 -30°C 下的界面成核过电位降低 **65% 以上**。

3. **可量化对照实验验证矩阵**
   - **对照组 A**：裸锂对称电池 Li | LPSCl | Li
   - **实验组 B**：梯度 SEI 改性对称电池，测试 -30°C 下临界电流密度（CCD）目标 ≥ 2.8 mA/cm²，原位 EIS 追踪 500 圈循环阻抗增长率 < 8%。
            """.trimIndent(),
            recommendedModel = "Gemini 4 Ultra (gemini-4.0-ultra-preview)"
        ),
        UseCaseItem(
            id = "uc-security-redteam",
            category = "编程与软件工程",
            title = "全自动云原生安全审计、0-Day 变体污点追踪与防御补丁合成",
            subtitle = "结合完整代码图谱与运行时调用链，自动发现跨微服务的 SSRF、反序列化、越权访问与竞争条件漏洞。",
            difficulty = "高阶安全",
            roiMetric = "高危漏洞检出率提升 3.4 倍 · 误报率低于 4%",
            claudeUseCaseUrl = "academy.claude.com/use-cases/cybersecurity-defensive",
            whyGeminiWins = listOf(
                "全仓上下文让污点追踪（Taint Analysis）能够跨越 15 层函数调用与消息队列消费者，Claude 200K 无法追踪跨模块数据流。",
                "在沙盒中直接对修复补丁执行回归测试，确保修复安全漏洞的同时绝不破坏正常业务功能。"
            ),
            workflowSteps = listOf(
                "1. 全仓 AST 与数据流建模：识别所有外部不可信输入源（Source）与敏感执行点（Sink）。",
                "2. 跨服务污点传播验证：过滤已经过严格白名单校验的路径，将误报率降至 4% 以下。",
                "3. 生成最小侵入式安全补丁与回归验证单测。"
            ),
            systemPrompt = "你是 Google Project Zero 级别的防御性白帽安全审计智能体。专注于识别深层逻辑漏洞与提供零副作用的代码加固补丁。",
            userPromptTemplate = "审计以下 Webhook 预览头像抓取逻辑，指出为什么简单的 `host != \"127.0.0.1\"` 检查无法防御 DNS Rebinding 与 IPv6/八进制绕过型 SSRF 攻击，并给出加固后的安全实现。",
            sampleOutput = """
### 深度安全审计报告：Webhook URL 抓取 SSRF 绕过风险与根治方案

1. **现有校验的三大绕过路径**
   - **多进制/IPv6 编码绕过**：攻击者可传入 `http://0177.0.0.1/`（八进制）、`http://2130706433/`（十进制整数 IP）或 `http://[::ffff:127.0.0.1]/` 轻松绕过字符串匹配。
   - **302 重定向跳转绕过**：初次请求合法外网域名，服务端返回 `HTTP 302 Location: http://169.254.169.254/latest/meta-data/` 窃取云主机 IAM 临时凭证。
   - **DNS Rebinding（TOCTOU 竞态攻击）**：校验阶段域名解析为公网 IP，实际发起 Socket 连接时 TTL 过期重绑定至内网 `10.0.0.1`。

2. **根治加固方案（在自定义 OkHttp Dns 与 SocketFactory 层锁定解析后 IP）**
   - 解析域名获取 `InetAddress` 后，严格校验 `!addr.isAnyLocalAddress && !addr.isLoopbackAddress && !addr.isLinkLocalAddress && !addr.isSiteLocalAddress`；
   - 禁用自动跨域重定向（`followRedirects(false)`），并将通过校验的固定 IP 直接注入底层 Socket 连接，彻底阻断二次 DNS 解析竞态。
            """.trimIndent(),
            recommendedModel = "Gemini 4 Pro"
        )
    )
}

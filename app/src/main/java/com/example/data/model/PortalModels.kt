package com.example.data.model

import androidx.compose.ui.graphics.Color

/**
 * Represents the 5 flagship websites replicated 1:1 and upgraded for Google Gemini 4:
 * 1. claude.dev -> gemini4.dev (开发者文档 & API 中心)
 * 2. claude.news -> gemini4.news (官方动态 & 研究资讯)
 * 3. claude.com/resources -> gemini4.com/resources (资源中心 & 迁移工具)
 * 4. academy.claude.com -> academy.gemini4.com (Gemini 学院 & 认证课程)
 * 5. academy.claude.com/use-cases -> academy.gemini4.com/use-cases (实战使用案例库)
 */
enum class PortalSection(
    val id: String,
    val title: String,
    val geminiDomain: String,
    val claudeCounterpart: String,
    val subtitle: String,
    val accentHex: Long
) {
    DEV(
        id = "dev",
        title = "开发者文档",
        geminiDomain = "gemini4.dev",
        claudeCounterpart = "对标并超越 claude.dev",
        subtitle = "1000万 Token 原生多模态 API · Deep Think 2.0 推理引擎 · 零延迟流式调用",
        accentHex = 0xFF1A73E8
    ),
    NEWS(
        id = "news",
        title = "官方动态",
        geminiDomain = "gemini4.news",
        claudeCounterpart = "对标并超越 claude.news",
        subtitle = "Gemini 4 全系模型发布、SWE-bench 94.8% 登顶报告与全球开发者生态突破",
        accentHex = 0xFFEA4335
    ),
    RESOURCES(
        id = "resources",
        title = "资源中心",
        geminiDomain = "gemini4.com/resources",
        claudeCounterpart = "对标并超越 claude.com/resources",
        subtitle = "提示词工程图谱、Claude 一键迁移转换器、架构白皮书与 TCO 成本计算器",
        accentHex = 0xFFFBBC04
    ),
    ACADEMY(
        id = "academy",
        title = "Gemini 学院",
        geminiDomain = "academy.gemini4.com",
        claudeCounterpart = "对标并超越 academy.claude.com",
        subtitle = "Google 官方认证课程体系：从多模态提示工程到企业级自主智能体集群架构",
        accentHex = 0xFF34A853
    ),
    USE_CASES(
        id = "use_cases",
        title = "使用案例",
        geminiDomain = "academy.gemini4.com/use-cases",
        claudeCounterpart = "对标并超越 academy.claude.com/use-cases",
        subtitle = "覆盖全栈工程、科研、视频流理解、金融合规的工业级蓝图，支持一键沙盒实测",
        accentHex = 0xFF9334E6
    )
}

data class GeminiModelSpec(
    val id: String,
    val name: String,
    val apiCode: String,
    val badge: String,
    val contextWindow: String,
    val maxOutput: String,
    val modalities: String,
    val latencyTtft: String,
    val priceInputPerM: String,
    val sweBenchScore: String,
    val claudeModelCompared: String,
    val claudeWinSummary: String,
    val googleColorHex: Long
)

data class CodeSnippetVariant(
    val language: String,
    val code: String
)

data class DocSectionItem(
    val id: String,
    val category: String,
    val title: String,
    val readTime: String,
    val summary: String,
    val claudeEquivalentPath: String,
    val superiorityPoints: List<String>,
    val architectureSteps: List<String>,
    val codeVariants: List<CodeSnippetVariant>,
    val sandboxPrompt: String,
    val sandboxSystemInstruction: String,
    val expectedBenchmarkOutput: String
)

data class BenchmarkMetric(
    val label: String,
    val gemini4Score: Float,
    val gemini4Display: String,
    val claudeScore: Float,
    val claudeDisplay: String,
    val unitNote: String
)

data class NewsArticleItem(
    val id: String,
    val category: String,
    val date: String,
    val title: String,
    val subtitle: String,
    val readTime: String,
    val highlightTag: String,
    val claudeNewsComparison: String,
    val keyTakeaways: List<String>,
    val bodyParagraphs: List<String>,
    val benchmarks: List<BenchmarkMetric>,
    val sandboxPrompt: String
)

data class ResourceAssetItem(
    val id: String,
    val type: String, // "提示词模板", "迁移工具", "技术白皮书", "系统指令库", "SDK与CLI"
    val title: String,
    val subtitle: String,
    val badgeText: String,
    val claudeResourceEquivalent: String,
    val whyBetterThanClaude: String,
    val contentMarkdown: String,
    val templateCode: String,
    val actionableSandboxPrompt: String
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class CourseLesson(
    val lessonNumber: Int,
    val title: String,
    val duration: String,
    val coreConcepts: List<String>,
    val detailContent: String,
    val codeLabSnippet: String,
    val sandboxPrompt: String
)

data class AcademyCourseItem(
    val id: String,
    val track: String,
    val level: String,
    val title: String,
    val subtitle: String,
    val duration: String,
    val lessonCount: Int,
    val claudeAcademyEquivalent: String,
    val googleAdvantage: String,
    val lessons: List<CourseLesson>,
    val quiz: List<QuizQuestion>
)

data class UseCaseItem(
    val id: String,
    val category: String,
    val title: String,
    val subtitle: String,
    val difficulty: String,
    val roiMetric: String,
    val claudeUseCaseUrl: String,
    val whyGeminiWins: List<String>,
    val workflowSteps: List<String>,
    val systemPrompt: String,
    val userPromptTemplate: String,
    val sampleOutput: String,
    val recommendedModel: String
)

sealed class ActiveDetailSheet {
    data class DocDetail(val item: DocSectionItem) : ActiveDetailSheet()
    data class NewsDetail(val item: NewsArticleItem) : ActiveDetailSheet()
    data class ResourceDetail(val item: ResourceAssetItem) : ActiveDetailSheet()
    data class CourseDetail(val item: AcademyCourseItem) : ActiveDetailSheet()
    data class UseCaseDetail(val item: UseCaseItem) : ActiveDetailSheet()
    data object ClaudeVsGeminiMatrix : ActiveDetailSheet()
}

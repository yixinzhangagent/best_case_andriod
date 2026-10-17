package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AcademyCourseItem
import com.example.data.model.ActiveDetailSheet
import com.example.data.model.CodeSnippetVariant
import com.example.data.model.DocSectionItem
import com.example.data.model.NewsArticleItem
import com.example.data.model.ResourceAssetItem
import com.example.data.model.UseCaseItem
import com.example.data.network.SandboxResult
import com.example.ui.components.BenchmarkComparisonBar
import com.example.ui.components.ClaudeComparisonCallout
import com.example.ui.components.CodeBlockTerminalCard
import com.example.ui.theme.GeminiPurple
import com.example.ui.theme.GoogleBlue
import com.example.ui.theme.GoogleBlueLight
import com.example.ui.theme.GoogleGreen
import com.example.ui.theme.GoogleRed
import com.example.ui.theme.GoogleYellow
import com.example.ui.theme.JetBrainsMonoFamily

@Composable
fun ActiveDetailScreenOverlay(
    detail: ActiveDetailSheet,
    completedLessonKeys: Set<String>,
    completedCourseIds: Set<String>,
    onClose: () -> Unit,
    onMarkLessonComplete: (String, Int) -> Unit,
    onMarkCourseCertified: (String) -> Unit,
    onRunSandbox: (title: String, model: String, sys: String, prompt: String, expected: String) -> Unit
) {
    BackHandler { onClose() }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("detail_screen_overlay"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Detail Navigation Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("detail_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                    Text(
                        text = when (detail) {
                            is ActiveDetailSheet.DocDetail -> "gemini4.dev · 开发者文档详情"
                            is ActiveDetailSheet.NewsDetail -> "gemini4.news · 官方研报详情"
                            is ActiveDetailSheet.ResourceDetail -> "gemini4.com/resources · 资源详情"
                            is ActiveDetailSheet.CourseDetail -> "academy.gemini4.com · 课程实验"
                            is ActiveDetailSheet.UseCaseDetail -> "academy.gemini4.com/use-cases · 案例蓝图"
                            ActiveDetailSheet.ClaudeVsGeminiMatrix -> "Google Gemini 4 vs Claude 4 全维度对比"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                when (detail) {
                    is ActiveDetailSheet.DocDetail -> DocDetailView(detail.item, onRunSandbox)
                    is ActiveDetailSheet.NewsDetail -> NewsDetailView(detail.item, onRunSandbox)
                    is ActiveDetailSheet.ResourceDetail -> ResourceDetailView(detail.item, onRunSandbox)
                    is ActiveDetailSheet.CourseDetail -> CourseDetailView(
                        course = detail.item,
                        completedLessonKeys = completedLessonKeys,
                        isCertified = detail.item.id in completedCourseIds,
                        onMarkLessonComplete = onMarkLessonComplete,
                        onMarkCourseCertified = onMarkCourseCertified,
                        onRunSandbox = onRunSandbox
                    )
                    is ActiveDetailSheet.UseCaseDetail -> UseCaseDetailView(detail.item, onRunSandbox)
                    ActiveDetailSheet.ClaudeVsGeminiMatrix -> ClaudeVsGeminiMatrixView(onRunSandbox)
                }
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
private fun DocDetailView(
    doc: DocSectionItem,
    onRunSandbox: (String, String, String, String, String) -> Unit
) {
    Text(doc.title, style = MaterialTheme.typography.headlineLarge)
    Text(doc.summary, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)

    ClaudeComparisonCallout(
        claudePathOrTitle = doc.claudeEquivalentPath,
        points = doc.superiorityPoints
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("生产级架构实施步骤", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            doc.architectureSteps.forEach { step ->
                Text(step, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    Text("多语言 SDK 与 REST 完整调用代码", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    CodeBlockTerminalCard(
        variants = doc.codeVariants,
        onRunInSandbox = {
            onRunSandbox(
                doc.title,
                "Gemini 4 Pro (gemini-3.1-pro-preview)",
                doc.sandboxSystemInstruction,
                doc.sandboxPrompt,
                doc.expectedBenchmarkOutput
            )
        }
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Gemini 4 基准输出参考", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(doc.expectedBenchmarkOutput, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun NewsDetailView(
    article: NewsArticleItem,
    onRunSandbox: (String, String, String, String, String) -> Unit
) {
    Text("${article.category} · ${article.date}", style = MaterialTheme.typography.labelLarge, color = GoogleRed)
    Text(article.title, style = MaterialTheme.typography.headlineLarge)
    Text(article.subtitle, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

    ClaudeComparisonCallout(
        claudePathOrTitle = "对比 claude.news 同期模型与技术指标",
        points = listOf(article.claudeNewsComparison) + article.keyTakeaways
    )

    if (article.benchmarks.isNotEmpty()) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Google Gemini 4 vs Claude 4 权威基准全表", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                article.benchmarks.forEach { metric ->
                    BenchmarkComparisonBar(metric = metric)
                }
            }
        }
    }

    article.bodyParagraphs.forEach { para ->
        Text(para, style = MaterialTheme.typography.bodyLarge)
    }

    Button(
        onClick = {
            onRunSandbox(
                article.title,
                "Gemini 4 Ultra (gemini-4.0-ultra-preview)",
                "你是 Google Gemini 4 官方发布解读与实测助手。",
                article.sandboxPrompt,
                ""
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = GoogleBlueLight, contentColor = Color.White)
    ) {
        Icon(Icons.Default.PlayArrow, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text("在实时沙盒中验证本篇新闻特性", fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ResourceDetailView(
    res: ResourceAssetItem,
    onRunSandbox: (String, String, String, String, String) -> Unit
) {
    Text(res.title, style = MaterialTheme.typography.headlineLarge)
    Text(res.subtitle, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)

    ClaudeComparisonCallout(
        claudePathOrTitle = res.claudeResourceEquivalent,
        points = listOf(res.whyBetterThanClaude)
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(res.contentMarkdown, style = MaterialTheme.typography.bodyMedium)
        }
    }

    CodeBlockTerminalCard(
        variants = listOf(CodeSnippetVariant("黄金模板与迁移配置", res.templateCode)),
        onRunInSandbox = {
            onRunSandbox(
                res.title,
                "Gemini 4 Pro (gemini-3.1-pro-preview)",
                "你是 Google Gemini 4 资源与迁移专家。",
                res.actionableSandboxPrompt,
                ""
            )
        }
    )
}

@Composable
private fun CourseDetailView(
    course: AcademyCourseItem,
    completedLessonKeys: Set<String>,
    isCertified: Boolean,
    onMarkLessonComplete: (String, Int) -> Unit,
    onMarkCourseCertified: (String) -> Unit,
    onRunSandbox: (String, String, String, String, String) -> Unit
) {
    var activeLessonIdx by remember { mutableIntStateOf(0) }
    val selectedAnswers = remember { mutableStateMapOf<Int, Int>() }
    var quizSubmitted by remember { mutableStateOf(false) }

    val activeLesson = course.lessons.getOrElse(activeLessonIdx) { course.lessons.first() }
    val isCurrentLessonDone = "${course.id}:${activeLesson.lessonNumber}" in completedLessonKeys

    Text("${course.track} · ${course.level}", style = MaterialTheme.typography.labelLarge, color = GoogleGreen)
    Text(course.title, style = MaterialTheme.typography.headlineLarge)

    ClaudeComparisonCallout(
        claudePathOrTitle = course.claudeAcademyEquivalent,
        points = listOf(course.googleAdvantage)
    )

    // Lesson Selector Tabs
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        course.lessons.forEachIndexed { idx, lesson ->
            val done = "${course.id}:${lesson.lessonNumber}" in completedLessonKeys
            FilterChip(
                selected = idx == activeLessonIdx,
                onClick = { activeLessonIdx = idx },
                label = { Text("第 ${lesson.lessonNumber} 课 ${if (done) "✓" else ""}") },
                modifier = Modifier.testTag("lesson_tab_${lesson.lessonNumber}")
            )
        }
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(activeLesson.title, style = MaterialTheme.typography.titleLarge)
            Text(
                text = "核心知识点：" + activeLesson.coreConcepts.joinToString(" · "),
                style = MaterialTheme.typography.labelLarge,
                color = GoogleBlueLight
            )
            Text(activeLesson.detailContent, style = MaterialTheme.typography.bodyLarge)

            CodeBlockTerminalCard(
                variants = listOf(CodeSnippetVariant("课程实验代码 (Code Lab)", activeLesson.codeLabSnippet)),
                onRunInSandbox = {
                    onRunSandbox(
                        activeLesson.title,
                        "Gemini 4 Pro (gemini-3.1-pro-preview)",
                        "你是 Google Gemini 学院实验指导助教。",
                        activeLesson.sandboxPrompt,
                        ""
                    )
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        onMarkLessonComplete(course.id, activeLesson.lessonNumber)
                        if (activeLessonIdx < course.lessons.lastIndex) {
                            activeLessonIdx++
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCurrentLessonDone) GoogleGreen else GoogleBlueLight,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("complete_lesson_btn")
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isCurrentLessonDone) "本课已完成 (点击下一课)" else "标记本课已学完")
                }
            }
        }
    }

    // Interactive Certification Quiz Section
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = GoogleYellow)
                Text("章节认证考核测验 (通过即可领取 Google Gemini 4 官方认证徽章)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            course.quiz.forEachIndexed { qIdx, q ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text("Q${qIdx + 1}. ${q.question}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    q.options.forEachIndexed { optIdx, optionText ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedAnswers[qIdx] = optIdx }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedAnswers[qIdx] == optIdx,
                                onClick = { selectedAnswers[qIdx] = optIdx }
                            )
                            Text(optionText, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    if (quizSubmitted) {
                        val isCorrect = selectedAnswers[qIdx] == q.correctIndex
                        Text(
                            text = if (isCorrect) "✓ 回答正确！${q.explanation}" else "参考解析：${q.explanation}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isCorrect) GoogleGreen else GoogleYellow,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    quizSubmitted = true
                    onMarkCourseCertified(course.id)
                },
                modifier = Modifier.fillMaxWidth().testTag("submit_course_quiz_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoogleGreen, contentColor = Color.White)
            ) {
                Icon(Icons.Default.Verified, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isCertified) "已颁发 Google Gemini 4 认证证书" else "提交答卷并领取官方课程认证", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun UseCaseDetailView(
    uc: UseCaseItem,
    onRunSandbox: (String, String, String, String, String) -> Unit
) {
    Text("${uc.category} · 推荐模型：${uc.recommendedModel}", style = MaterialTheme.typography.labelLarge, color = GeminiPurple)
    Text(uc.title, style = MaterialTheme.typography.headlineLarge)
    Text(uc.subtitle, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)

    ClaudeComparisonCallout(
        claudePathOrTitle = uc.claudeUseCaseUrl,
        points = uc.whyGeminiWins
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("端到端架构执行工作流", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            uc.workflowSteps.forEach { step ->
                Text(step, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }

    CodeBlockTerminalCard(
        variants = listOf(
            CodeSnippetVariant("System Instruction", uc.systemPrompt),
            CodeSnippetVariant("User Prompt 模板", uc.userPromptTemplate)
        ),
        onRunInSandbox = {
            onRunSandbox(
                uc.title,
                uc.recommendedModel,
                uc.systemPrompt,
                uc.userPromptTemplate,
                uc.sampleOutput
            )
        }
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Gemini 4 生产级输出范例", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(uc.sampleOutput, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ClaudeVsGeminiMatrixView(
    onRunSandbox: (String, String, String, String, String) -> Unit
) {
    Text("Google Gemini 4 全景生态 vs Claude 全系 1:1 巅峰对比", style = MaterialTheme.typography.headlineLarge)
    Text(
        "从开发者文档 (gemini4.dev)、官方资讯 (gemini4.news)、资源中心、Gemini 学院到工业级使用案例，全方位超越 Claude：",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    val rows = listOf(
        Triple("最大原生上下文窗口", "10,000,000 Tokens (10M)", "200,000 Tokens (200K)"),
        Triple("原生视频流与音频波形输入", "支持最长 8 小时 4K 视频 + 原生双向语音", "不支持视频流与原生实时语音"),
        Triple("端侧离线 NPU 模型 (Android)", "Gemini 4 Nano (AICore 0流量免费)", "无端侧模型"),
        Triple("SWE-bench Verified 编程得分", "94.8% (内置免费代码沙盒验证)", "85.2%"),
        Triple("实时搜索引擎事实锚定", "原生 Google Search Grounding", "外置基础网页抓取"),
        Triple("百万 Token 缓存读取成本", "$0.08 / 1M Tokens", "$0.30 ~ $1.50 / 1M Tokens"),
        Triple("智能体开放协议支持", "兼容 MCP + 原生 Google A2A 跨智能体协议", "仅支持单机 MCP")
    )

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            rows.forEach { (dim, geminiVal, claudeVal) ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(dim, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = GoogleGreen.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Gemini 4: $geminiVal",
                                style = MaterialTheme.typography.bodySmall,
                                color = GoogleGreen,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Claude: $claudeVal",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            }
        }
    }

    Button(
        onClick = {
            onRunSandbox(
                "Gemini 4 vs Claude 4 架构师迁移评估",
                "Gemini 4 Ultra (gemini-4.0-ultra-preview)",
                "你是精通 Google Gemini 4 与 Anthropic Claude 架构差异的独立首席评测官。",
                "请为我们团队生成一份从 Claude 3.7/4 Sonnet 迁移至 Google Gemini 4 的分阶段技术路线图。",
                ""
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = GoogleBlueLight, contentColor = Color.White)
    ) {
        Icon(Icons.Default.PlayArrow, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text("在沙盒中生成定制化 Claude -> Gemini 4 迁移方案", fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LiveGeminiSandboxOverlay(
    title: String,
    selectedModel: String,
    systemInstruction: String,
    prompt: String,
    isRunning: Boolean,
    result: SandboxResult?,
    onUpdateInputs: (model: String?, sys: String?, prompt: String?) -> Unit,
    onExecute: () -> Unit,
    onClose: () -> Unit
) {
    BackHandler { onClose() }
    val clipboard = LocalClipboardManager.current
    val modelChoices = listOf(
        "Gemini 4 Ultra (gemini-4.0-ultra-preview)",
        "Gemini 4 Pro (gemini-3.1-pro-preview)",
        "Gemini 4 Flash (gemini-3.5-flash)"
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("live_sandbox_overlay"),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Surface(color = Color(0xFF0F172A)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoogleYellow)
                        Column {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "gemini4.dev Interactive Playground · 支持实时 Gemini REST API",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("close_sandbox_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "关闭沙盒", tint = Color.White)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Prototype Security Caution Notice per gemini-api skill mandate
                Surface(
                    color = GoogleYellow.copy(alpha = 0.14f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = GoogleYellow, modifier = Modifier.size(18.dp))
                        Text(
                            text = stringResource(R.string.security_warning_prototype),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Text("选择 Gemini 4 目标引擎：", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    modelChoices.forEach { m ->
                        FilterChip(
                            selected = selectedModel == m,
                            onClick = { onUpdateInputs(m, null, null) },
                            label = { Text(m.substringBefore(" (")) }
                        )
                    }
                }

                OutlinedTextField(
                    value = systemInstruction,
                    onValueChange = { onUpdateInputs(null, it, null) },
                    label = { Text("System Instruction (系统角色与边界宪章)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                OutlinedTextField(
                    value = prompt,
                    onValueChange = { onUpdateInputs(null, null, it) },
                    label = { Text("User Prompt (输入多模态/代码/推理指令)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("sandbox_prompt_input"),
                    minLines = 3,
                    maxLines = 6
                )

                Button(
                    onClick = onExecute,
                    enabled = !isRunning && prompt.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("execute_sandbox_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoogleBlueLight, contentColor = Color.White)
                ) {
                    if (isRunning) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("正在通过 Gemini 4 Deep Think 引擎生成…")
                    } else {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("立即执行推理 (Run GenerateContent)", fontWeight = FontWeight.Bold)
                    }
                }

                if (result != null) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1322))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = result.modeLabel,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = GoogleGreen
                                    )
                                    Text(
                                        text = "耗时 ${result.latencyMs}ms · 约 ${result.tokenEstimate} Tokens",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF94A3B8)
                                    )
                                }
                                IconButton(
                                    onClick = { clipboard.setText(AnnotatedString(result.outputText)) }
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "复制输出", tint = Color.White)
                                }
                            }
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = result.outputText,
                                fontFamily = JetBrainsMonoFamily,
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                color = Color(0xFFE2E8F0)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun GlobalSearchResultsOverlay(
    query: String,
    docs: List<DocSectionItem>,
    news: List<NewsArticleItem>,
    resources: List<ResourceAssetItem>,
    courses: List<AcademyCourseItem>,
    useCases: List<UseCaseItem>,
    onOpenDetail: (ActiveDetailSheet) -> Unit,
    onCloseSearch: () -> Unit
) {
    val q = query.trim().lowercase()
    val matchedDocs = docs.filter { it.title.lowercase().contains(q) || it.summary.lowercase().contains(q) }
    val matchedNews = news.filter { it.title.lowercase().contains(q) || it.subtitle.lowercase().contains(q) }
    val matchedRes = resources.filter { it.title.lowercase().contains(q) || it.subtitle.lowercase().contains(q) }
    val matchedCourses = courses.filter { it.title.lowercase().contains(q) || it.subtitle.lowercase().contains(q) }
    val matchedUcs = useCases.filter { it.title.lowercase().contains(q) || it.subtitle.lowercase().contains(q) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "全站检索结果：「$query」",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                OutlinedButton(onClick = onCloseSearch) {
                    Text("返回门户")
                }
            }
        }

        items(matchedDocs) { item ->
            SearchResultRow("gemini4.dev 文档", item.title, item.summary, GoogleBlueLight) {
                onOpenDetail(ActiveDetailSheet.DocDetail(item))
            }
        }
        items(matchedNews) { item ->
            SearchResultRow("gemini4.news 动态", item.title, item.subtitle, GoogleRed) {
                onOpenDetail(ActiveDetailSheet.NewsDetail(item))
            }
        }
        items(matchedRes) { item ->
            SearchResultRow("gemini4.com/resources", item.title, item.subtitle, GoogleYellow) {
                onOpenDetail(ActiveDetailSheet.ResourceDetail(item))
            }
        }
        items(matchedCourses) { item ->
            SearchResultRow("academy.gemini4.com", item.title, item.subtitle, GoogleGreen) {
                onOpenDetail(ActiveDetailSheet.CourseDetail(item))
            }
        }
        items(matchedUcs) { item ->
            SearchResultRow("use-cases 实战案例", item.title, item.subtitle, GeminiPurple) {
                onOpenDetail(ActiveDetailSheet.UseCaseDetail(item))
            }
        }
    }
}

@Composable
private fun SearchResultRow(
    badge: String,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(badge, style = MaterialTheme.typography.labelMedium, color = color)
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
        }
    }
}

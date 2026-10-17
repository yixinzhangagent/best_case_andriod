package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.AcademyCourseItem
import com.example.data.model.ActiveDetailSheet
import com.example.data.model.CodeSnippetVariant
import com.example.data.model.PortalSection
import com.example.data.model.ResourceAssetItem
import com.example.data.model.UseCaseItem
import com.example.ui.components.CategoryFilterRow
import com.example.ui.components.ClaudeComparisonCallout
import com.example.ui.components.CodeBlockTerminalCard
import com.example.ui.components.GeminiPortalHeroBanner
import com.example.ui.theme.GeminiPurple
import com.example.ui.theme.GoogleBlue
import com.example.ui.theme.GoogleBlueLight
import com.example.ui.theme.GoogleGreen
import com.example.ui.theme.GoogleRed
import com.example.ui.theme.GoogleYellow
import java.util.Locale

@Composable
fun ResourcesPortalContent(
    resources: List<ResourceAssetItem>,
    selectedType: String,
    dailyRequestsMillion: Float,
    avgContextTokensK: Float,
    onSelectType: (String) -> Unit,
    onUpdateCalculator: (Float, Float) -> Unit,
    onOpenDetail: (ActiveDetailSheet) -> Unit,
    onRunSandbox: (title: String, model: String, sys: String, prompt: String, expected: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val types = listOf("全部", "迁移工具", "提示词模板", "系统指令库", "技术白皮书")
    val filtered = if (selectedType == "全部") resources else resources.filter { it.type == selectedType }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("resources_portal_list"),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            GeminiPortalHeroBanner(
                section = PortalSection.RESOURCES,
                onOpenComparisonMatrix = { onOpenDetail(ActiveDetailSheet.ClaudeVsGeminiMatrix) },
                onOpenQuickSandbox = {
                    val migrator = resources.first()
                    onRunSandbox(
                        migrator.title,
                        "Gemini 4 Pro (gemini-3.1-pro-preview)",
                        "你是 Claude -> Gemini 4 架构与提示词自动迁移转换引擎。",
                        migrator.actionableSandboxPrompt,
                        ""
                    )
                }
            )
        }

        // Interactive TCO & Context Window Calculator Card
        item {
            TcoAndContextCalculatorCard(
                dailyRequestsM = dailyRequestsMillion,
                avgContextK = avgContextTokensK,
                onUpdate = onUpdateCalculator,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            CategoryFilterRow(
                categories = types,
                selectedCategory = selectedType,
                onSelectCategory = onSelectType
            )
        }

        items(filtered, key = { it.id }) { res ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onOpenDetail(ActiveDetailSheet.ResourceDetail(res)) }
                    .testTag("resource_card_${res.id}"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = GoogleYellow.copy(alpha = 0.20f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = res.type,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Surface(
                            color = GoogleBlue.copy(alpha = 0.14f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = res.badgeText,
                                style = MaterialTheme.typography.labelSmall,
                                color = GoogleBlueLight,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = res.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = res.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    ClaudeComparisonCallout(
                        claudePathOrTitle = res.claudeResourceEquivalent,
                        points = listOf(res.whyBetterThanClaude)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    CodeBlockTerminalCard(
                        variants = listOf(CodeSnippetVariant("模板 / 代码示例", res.templateCode)),
                        onRunInSandbox = {
                            onRunSandbox(
                                res.title,
                                "Gemini 4 Pro (gemini-3.1-pro-preview)",
                                "你是 Google Gemini 4 资源中心首席顾问。",
                                res.actionableSandboxPrompt,
                                ""
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TcoAndContextCalculatorCard(
    dailyRequestsM: Float,
    avgContextK: Float,
    onUpdate: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    // Calculate realistic monthly token spend (in thousands USD)
    // Gemini 4 Pro cached blended cost vs Claude Sonnet 4
    val geminiMonthlyUsd = (dailyRequestsM * avgContextK * 0.12f).toInt().coerceAtLeast(12)
    val claudeMonthlyUsd = (dailyRequestsM * avgContextK * 0.45f).toInt().coerceAtLeast(48)
    val savingsPercent = (((claudeMonthlyUsd - geminiMonthlyUsd).toFloat() / claudeMonthlyUsd) * 100).toInt()
    val claudeContextOverflow = avgContextK > 200f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.5.dp, GoogleYellow.copy(alpha = 0.55f), RoundedCornerShape(20.dp))
            .testTag("tco_calculator_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Calculate,
                    contentDescription = null,
                    tint = GoogleYellow
                )
                Text(
                    text = "Gemini 4 vs Claude 4 成本与上下文容量实时测算器",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = String.format(Locale.US, "日均 API 调用规模：%.1f 万次/天", dailyRequestsM * 10f),
                style = MaterialTheme.typography.labelLarge
            )
            Slider(
                value = dailyRequestsM,
                onValueChange = { onUpdate(it, avgContextK) },
                valueRange = 0.5f..15f,
                colors = SliderDefaults.colors(
                    thumbColor = GoogleBlueLight,
                    activeTrackColor = GoogleBlueLight
                )
            )

            Text(
                text = "单次请求上下文长度：${avgContextK.toInt()}K Tokens (${if (claudeContextOverflow) "Claude 200K 已溢出报错!" else "Claude 200K 临界内"})",
                style = MaterialTheme.typography.labelLarge,
                color = if (claudeContextOverflow) GoogleRed else MaterialTheme.colorScheme.onSurface
            )
            Slider(
                value = avgContextK,
                onValueChange = { onUpdate(dailyRequestsM, it) },
                valueRange = 20f..1500f,
                colors = SliderDefaults.colors(
                    thumbColor = GoogleGreen,
                    activeTrackColor = GoogleGreen
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    color = GoogleGreen.copy(alpha = 0.14f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Gemini 4 (含缓存)",
                            style = MaterialTheme.typography.labelMedium,
                            color = GoogleGreen
                        )
                        Text(
                            text = "$$geminiMonthlyUsd / 月",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "立省 $savingsPercent% · 10M 无损装入",
                            style = MaterialTheme.typography.bodySmall,
                            color = GoogleGreen
                        )
                    }
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Claude 4 Sonnet",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "$$claudeMonthlyUsd / 月",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (claudeContextOverflow) "超过 200K 无法直接调用" else "单价高 · 缺少长视频支持",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (claudeContextOverflow) GoogleRed else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AcademyPortalContent(
    courses: List<AcademyCourseItem>,
    selectedTrack: String,
    completedLessonKeys: Set<String>,
    completedCourseIds: Set<String>,
    onSelectTrack: (String) -> Unit,
    onOpenDetail: (ActiveDetailSheet) -> Unit,
    onRunSandbox: (title: String, model: String, sys: String, prompt: String, expected: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tracks = listOf("全部", "全栈 AI 工程师", "企业级 Agent 架构师", "Android 端侧 AI 专家")
    val filtered = if (selectedTrack == "全部") courses else courses.filter { it.track == selectedTrack }
    val totalLessons = courses.sumOf { it.lessonCount }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("academy_portal_list"),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            GeminiPortalHeroBanner(
                section = PortalSection.ACADEMY,
                onOpenComparisonMatrix = { onOpenDetail(ActiveDetailSheet.ClaudeVsGeminiMatrix) },
                onOpenQuickSandbox = {
                    val firstLesson = courses.first().lessons.first()
                    onRunSandbox(
                        firstLesson.title,
                        "Gemini 4 Pro (gemini-3.1-pro-preview)",
                        "你是 Google Gemini 学院官方认证导师。",
                        firstLesson.sandboxPrompt,
                        ""
                    )
                }
            )
        }

        // Academy Certification Progress Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = GoogleGreen.copy(alpha = 0.12f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = GoogleYellow
                            )
                            Text(
                                text = "Google Gemini 4 官方学习进度与认证徽章",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "已完成课时：${completedLessonKeys.size} / $totalLessons · 已获认证证书：${completedCourseIds.size} 门",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { (completedLessonKeys.size.toFloat() / totalLessons.coerceAtLeast(1)).coerceIn(0.08f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = GoogleGreen,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }
        }

        item {
            CategoryFilterRow(
                categories = tracks,
                selectedCategory = selectedTrack,
                onSelectCategory = onSelectTrack
            )
        }

        items(filtered, key = { it.id }) { course ->
            val doneLessonsCount = course.lessons.count { "${course.id}:${it.lessonNumber}" in completedLessonKeys }
            val isCertified = course.id in completedCourseIds

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onOpenDetail(ActiveDetailSheet.CourseDetail(course)) }
                    .testTag("course_card_${course.id}"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                color = GoogleGreen.copy(alpha = 0.16f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = course.track,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = GoogleGreen,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = course.level,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        if (isCertified) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "已认证",
                                    tint = GoogleGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "已获认证",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = GoogleGreen
                                )
                            }
                        } else {
                            Text(
                                text = "${course.duration} · $doneLessonsCount/${course.lessonCount} 课",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = course.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    ClaudeComparisonCallout(
                        claudePathOrTitle = course.claudeAcademyEquivalent,
                        points = listOf(course.googleAdvantage)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    // Preview Lesson pills
                    course.lessons.forEach { lesson ->
                        val lessonDone = "${course.id}:${lesson.lessonNumber}" in completedLessonKeys
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = if (lessonDone) Icons.Default.CheckCircle else Icons.Default.School,
                                    contentDescription = null,
                                    tint = if (lessonDone) GoogleGreen else GoogleBlueLight,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = lesson.title,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = lesson.duration,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onOpenDetail(ActiveDetailSheet.CourseDetail(course)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoogleGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Text("进入交互式课程实验与认证测验", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun UseCasesPortalContent(
    useCases: List<UseCaseItem>,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    onOpenDetail: (ActiveDetailSheet) -> Unit,
    onRunSandbox: (title: String, model: String, sys: String, prompt: String, expected: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        "全部",
        "编程与软件工程",
        "多模态创作与视频",
        "金融法律合规",
        "客户体验与智能体",
        "深度研究与分析"
    )
    val filtered = if (selectedCategory == "全部") useCases else useCases.filter { it.category == selectedCategory }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("use_cases_portal_list"),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            GeminiPortalHeroBanner(
                section = PortalSection.USE_CASES,
                onOpenComparisonMatrix = { onOpenDetail(ActiveDetailSheet.ClaudeVsGeminiMatrix) },
                onOpenQuickSandbox = {
                    val topUc = useCases.first()
                    onRunSandbox(
                        topUc.title,
                        topUc.recommendedModel,
                        topUc.systemPrompt,
                        topUc.userPromptTemplate,
                        topUc.sampleOutput
                    )
                }
            )
        }

        item {
            CategoryFilterRow(
                categories = categories,
                selectedCategory = selectedCategory,
                onSelectCategory = onSelectCategory
            )
        }

        items(filtered, key = { it.id }) { uc ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onOpenDetail(ActiveDetailSheet.UseCaseDetail(uc)) }
                    .testTag("use_case_card_${uc.id}"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = GeminiPurple.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = uc.category,
                                style = MaterialTheme.typography.labelLarge,
                                color = GeminiPurple,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Surface(
                            color = GoogleGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = uc.roiMetric,
                                style = MaterialTheme.typography.labelSmall,
                                color = GoogleGreen,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = uc.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = uc.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    ClaudeComparisonCallout(
                        claudePathOrTitle = uc.claudeUseCaseUrl,
                        points = uc.whyGeminiWins
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { onOpenDetail(ActiveDetailSheet.UseCaseDetail(uc)) },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("查看架构蓝图与提示词")
                        }

                        Button(
                            onClick = {
                                onRunSandbox(
                                    uc.title,
                                    uc.recommendedModel,
                                    uc.systemPrompt,
                                    uc.userPromptTemplate,
                                    uc.sampleOutput
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoogleBlueLight,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.testTag("run_use_case_btn_${uc.id}")
                        ) {
                            Icon(Icons.Default.RocketLaunch, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("一键运行此案例")
                        }
                    }
                }
            }
        }
    }
}

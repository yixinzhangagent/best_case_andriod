package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.ActiveDetailSheet
import com.example.data.model.DocSectionItem
import com.example.data.model.GeminiModelSpec
import com.example.data.model.NewsArticleItem
import com.example.data.model.PortalSection
import com.example.ui.components.BenchmarkComparisonBar
import com.example.ui.components.CategoryFilterRow
import com.example.ui.components.ClaudeComparisonCallout
import com.example.ui.components.CodeBlockTerminalCard
import com.example.ui.components.GeminiPortalHeroBanner
import com.example.ui.theme.GoogleBlue
import com.example.ui.theme.GoogleBlueLight
import com.example.ui.theme.GoogleGreen
import com.example.ui.theme.GoogleRed
import com.example.ui.theme.GoogleYellow

@Composable
fun DevPortalContent(
    models: List<GeminiModelSpec>,
    docs: List<DocSectionItem>,
    selectedCategory: String,
    bookmarkedIds: Set<String>,
    onSelectCategory: (String) -> Unit,
    onOpenDetail: (ActiveDetailSheet) -> Unit,
    onToggleBookmark: (String) -> Unit,
    onRunSandbox: (title: String, model: String, sys: String, prompt: String, expected: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("全部", "快速开始", "核心架构", "推理引擎", "智能体与工具", "实时多模态", "生产可靠性")
    val filteredDocs = if (selectedCategory == "全部") docs else docs.filter { it.category == selectedCategory }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dev_portal_list"),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            GeminiPortalHeroBanner(
                section = PortalSection.DEV,
                onOpenComparisonMatrix = { onOpenDetail(ActiveDetailSheet.ClaudeVsGeminiMatrix) },
                onOpenQuickSandbox = {
                    val firstDoc = docs.first()
                    onRunSandbox(
                        "gemini4.dev 快速沙盒测试",
                        "Gemini 4 Pro (gemini-3.1-pro-preview)",
                        firstDoc.sandboxSystemInstruction,
                        firstDoc.sandboxPrompt,
                        firstDoc.expectedBenchmarkOutput
                    )
                }
            )
        }

        // Model Matrix Section
        item {
            Column(modifier = Modifier.padding(top = 4.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Memory,
                            contentDescription = null,
                            tint = GoogleBlueLight
                        )
                        Text(
                            text = "Gemini 4 全系模型矩阵 (超越 Claude 4 系列)",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    models.forEach { spec ->
                        ModelSpecCard(
                            spec = spec,
                            onTestModel = {
                                onRunSandbox(
                                    "实测 ${spec.name} (${spec.apiCode})",
                                    "${spec.name} (${spec.apiCode})",
                                    "你是 ${spec.name} 核心推理引擎，具备 ${spec.contextWindow} 上下文与 ${spec.modalities} 能力。",
                                    "请展示 ${spec.name} 相比 ${spec.claudeModelCompared} 在处理百万行代码重构与多模态实时交互时的架构优势，并附一段 Kotlin 示例。",
                                    ""
                                )
                            }
                        )
                    }
                }
            }
        }

        item {
            Column {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "gemini4.dev 开发者核心文档与 SDK 指南",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                CategoryFilterRow(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onSelectCategory = onSelectCategory
                )
            }
        }

        items(filteredDocs, key = { it.id }) { doc ->
            DocSectionCard(
                doc = doc,
                isBookmarked = doc.id in bookmarkedIds,
                onToggleBookmark = { onToggleBookmark(doc.id) },
                onClick = { onOpenDetail(ActiveDetailSheet.DocDetail(doc)) },
                onRunSandbox = {
                    onRunSandbox(
                        doc.title,
                        "Gemini 4 Pro (gemini-3.1-pro-preview)",
                        doc.sandboxSystemInstruction,
                        doc.sandboxPrompt,
                        doc.expectedBenchmarkOutput
                    )
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun ModelSpecCard(
    spec: GeminiModelSpec,
    onTestModel: () -> Unit
) {
    val accent = Color(spec.googleColorHex)
    Card(
        modifier = Modifier
            .width(305.dp)
            .border(1.5.dp, accent.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
            .testTag("model_card_${spec.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = accent.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = spec.badge,
                        style = MaterialTheme.typography.labelLarge,
                        color = accent,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = "TTFT ${spec.latencyTtft}",
                    style = MaterialTheme.typography.labelMedium,
                    color = GoogleGreen
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = spec.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = spec.apiCode,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
            Spacer(modifier = Modifier.height(10.dp))

            MetricRow("上下文容量", spec.contextWindow)
            MetricRow("SWE-bench", spec.sweBenchScore)
            MetricRow("输入定价", spec.priceInputPerM)
            MetricRow("支持模态", spec.modalities)

            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                color = GoogleGreen.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "对比 ${spec.claudeModelCompared}：${spec.claudeWinSummary}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onTestModel,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("在沙盒中调用 ${spec.name}", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun MetricRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun DocSectionCard(
    doc: DocSectionItem,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onClick: () -> Unit,
    onRunSandbox: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("doc_card_${doc.id}"),
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = GoogleBlue.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = doc.category,
                            style = MaterialTheme.typography.labelLarge,
                            color = GoogleBlueLight,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Text(
                        text = "阅读 ${doc.readTime}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onToggleBookmark) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "收藏文档",
                        tint = if (isBookmarked) GoogleYellow else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = doc.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = doc.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))
            ClaudeComparisonCallout(
                claudePathOrTitle = doc.claudeEquivalentPath,
                points = doc.superiorityPoints.take(2)
            )

            Spacer(modifier = Modifier.height(12.dp))
            CodeBlockTerminalCard(
                variants = doc.codeVariants,
                onRunInSandbox = onRunSandbox
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "点击展开完整架构步骤与基准输出",
                    style = MaterialTheme.typography.labelLarge,
                    color = GoogleBlueLight
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = GoogleBlueLight,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun NewsPortalContent(
    news: List<NewsArticleItem>,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    onOpenDetail: (ActiveDetailSheet) -> Unit,
    onRunSandbox: (title: String, model: String, sys: String, prompt: String, expected: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("全部", "重磅发布", "开发者生态", "技术报告", "基准评测")
    val filtered = if (selectedCategory == "全部") news else news.filter { it.category == selectedCategory }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("news_portal_list"),
        contentPadding = PaddingValues(bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            GeminiPortalHeroBanner(
                section = PortalSection.NEWS,
                onOpenComparisonMatrix = { onOpenDetail(ActiveDetailSheet.ClaudeVsGeminiMatrix) },
                onOpenQuickSandbox = {
                    val top = news.first()
                    onRunSandbox(
                        top.title,
                        "Gemini 4 Ultra (gemini-4.0-ultra-preview)",
                        "你是 Google Gemini 4 官方技术新闻解读与架构评估顾问。",
                        top.sandboxPrompt,
                        ""
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

        items(filtered, key = { it.id }) { article ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onOpenDetail(ActiveDetailSheet.NewsDetail(article)) }
                    .testTag("news_card_${article.id}"),
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
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = GoogleRed.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = article.category,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = GoogleRed,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Surface(
                                color = GoogleYellow.copy(alpha = 0.18f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = article.highlightTag,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Text(
                            text = "${article.date} · ${article.readTime}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = article.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = article.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    ClaudeComparisonCallout(
                        claudePathOrTitle = "claude.news 同期报道对比",
                        points = listOf(article.claudeNewsComparison) + article.keyTakeaways.take(2)
                    )

                    if (article.benchmarks.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "权威基准实测对比 (Gemini 4 vs Claude 4)",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                article.benchmarks.take(3).forEach { metric ->
                                    BenchmarkComparisonBar(metric = metric)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { onOpenDetail(ActiveDetailSheet.NewsDetail(article)) },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Newspaper, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("阅读完整官方研报")
                        }

                        Button(
                            onClick = {
                                onRunSandbox(
                                    article.title,
                                    "Gemini 4 Pro (gemini-3.1-pro-preview)",
                                    "你是 Google DeepMind 技术报告首席解读专家。",
                                    article.sandboxPrompt,
                                    ""
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoogleBlueLight,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("实测此特性")
                        }
                    }
                }
            }
        }
    }
}

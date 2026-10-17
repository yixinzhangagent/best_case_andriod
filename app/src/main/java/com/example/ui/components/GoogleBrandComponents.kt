package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BenchmarkMetric
import com.example.data.model.CodeSnippetVariant
import com.example.data.model.PortalSection
import com.example.ui.theme.GeminiPurple
import com.example.ui.theme.GoogleBlue
import com.example.ui.theme.GoogleBlueLight
import com.example.ui.theme.GoogleGreen
import com.example.ui.theme.GoogleRed
import com.example.ui.theme.GoogleYellow
import com.example.ui.theme.JetBrainsMonoFamily

@Composable
fun GoogleFourColorBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
    ) {
        Box(modifier = Modifier.weight(1f).height(4.dp).background(GoogleBlueLight))
        Box(modifier = Modifier.weight(1f).height(4.dp).background(GoogleRed))
        Box(modifier = Modifier.weight(1f).height(4.dp).background(GoogleYellow))
        Box(modifier = Modifier.weight(1f).height(4.dp).background(GoogleGreen))
    }
}

@Composable
fun DomainSwitcherBar(
    currentSection: PortalSection,
    onSelectSection: (PortalSection) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PortalSection.entries.forEach { section ->
            val selected = section == currentSection
            val accentColor = Color(section.accentHex)
            val bgColor by animateColorAsState(
                targetValue = if (selected) accentColor.copy(alpha = 0.16f)
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                label = "domain_chip_bg"
            )
            Surface(
                shape = RoundedCornerShape(50),
                color = bgColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .border(
                        width = if (selected) 1.5.dp else 1.dp,
                        color = if (selected) accentColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(50)
                    )
                    .clickable { onSelectSection(section) }
                    .testTag("domain_pill_${section.id}")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(accentColor)
                    )
                    Text(
                        text = section.geminiDomain,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GeminiPortalHeroBanner(
    section: PortalSection,
    onOpenComparisonMatrix: () -> Unit,
    onOpenQuickSandbox: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = Color(section.accentHex)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("portal_hero_banner"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F172A)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Custom Canvas Google Gemini 4 Aurora Mesh & Sparkle Art
            Canvas(
                modifier = Modifier
                    .matchParentSize()
            ) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(accent.copy(alpha = 0.36f), Color.Transparent),
                        center = Offset(size.width * 0.85f, size.height * 0.25f),
                        radius = size.width * 0.55f
                    ),
                    center = Offset(size.width * 0.85f, size.height * 0.25f),
                    radius = size.width * 0.55f
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(GoogleBlueLight.copy(alpha = 0.25f), Color.Transparent),
                        center = Offset(size.width * 0.15f, size.height * 0.85f),
                        radius = size.width * 0.45f
                    ),
                    center = Offset(size.width * 0.15f, size.height * 0.85f),
                    radius = size.width * 0.45f
                )
                // Draw subtle 4-pointed Gemini sparkle in top right corner
                val cx = size.width * 0.88f
                val cy = size.height * 0.30f
                val r = 34.dp.toPx()
                val sparklePath = Path().apply {
                    moveTo(cx, cy - r)
                    quadraticBezierTo(cx, cy, cx + r, cy)
                    quadraticBezierTo(cx, cy, cx, cy + r)
                    quadraticBezierTo(cx, cy, cx - r, cy)
                    quadraticBezierTo(cx, cy, cx, cy - r)
                    close()
                }
                drawPath(
                    path = sparklePath,
                    brush = Brush.linearGradient(
                        colors = listOf(GoogleBlueLight, GoogleRed, GoogleYellow, GoogleGreen),
                        start = Offset(cx - r, cy - r),
                        end = Offset(cx + r, cy + r)
                    ),
                    alpha = 0.55f
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = accent.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.border(1.dp, accent.copy(alpha = 0.7f), RoundedCornerShape(50))
                    ) {
                        Text(
                            text = section.geminiDomain,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Surface(
                        color = GoogleGreen.copy(alpha = 0.22f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = Color(0xFF81C995),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = section.claudeCounterpart,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFCEEAD6),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Google Gemini 4 · ${section.title}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = section.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFCBD5E1)
                )

                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onOpenQuickSandbox,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoogleBlueLight,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("hero_open_sandbox_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("启动 Gemini 4 实时沙盒", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onOpenComparisonMatrix,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF64748B)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        modifier = Modifier.testTag("hero_compare_claude_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CompareArrows,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("vs Claude 4 全维对比")
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryFilterRow(
    categories: List<String>,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val selected = category == selectedCategory
            FilterChip(
                selected = selected,
                onClick = { onSelectCategory(category) },
                label = {
                    Text(
                        text = category,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.testTag("filter_chip_$category")
            )
        }
    }
}

@Composable
fun ClaudeComparisonCallout(
    claudePathOrTitle: String,
    points: List<String>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = GoogleGreen.copy(alpha = 0.10f),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoogleGreen.copy(alpha = 0.40f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = GoogleGreen,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "为什么比 Claude 更好 ($claudePathOrTitle)",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            points.forEach { point ->
                Row(
                    modifier = Modifier.padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "✓ ",
                        color = GoogleGreen,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = point,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun CodeBlockTerminalCard(
    variants: List<CodeSnippetVariant>,
    onRunInSandbox: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    if (variants.isEmpty()) return
    var selectedTabIndex by remember(variants) { mutableIntStateOf(0) }
    var copied by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val activeVariant = variants.getOrElse(selectedTabIndex) { variants.first() }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1322))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top bar with traffic lights, language tabs, and copy button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF172036))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(GoogleRed))
                    Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(GoogleYellow))
                    Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(GoogleGreen))
                    Spacer(modifier = Modifier.width(6.dp))
                    variants.forEachIndexed { idx, variant ->
                        val isSelected = idx == selectedTabIndex
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) GoogleBlue else Color.Transparent,
                            modifier = Modifier.clickable {
                                selectedTabIndex = idx
                                copied = false
                            }
                        ) {
                            Text(
                                text = variant.language,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) Color.White else Color(0xFF94A3B8),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(activeVariant.code))
                        copied = true
                    },
                    modifier = Modifier.size(36.dp).testTag("copy_code_button")
                ) {
                    Icon(
                        imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = "复制代码",
                        tint = if (copied) GoogleGreen else Color(0xFFCBD5E1),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Text(
                text = activeVariant.code,
                fontFamily = JetBrainsMonoFamily,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = Color(0xFFE2E8F0),
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(14.dp)
            )

            if (onRunInSandbox != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF131C31))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onRunInSandbox,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoogleBlueLight,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("code_card_run_sandbox_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("在 Gemini 4 沙盒中实测此示例", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun BenchmarkComparisonBar(
    metric: BenchmarkMetric,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = metric.label,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = metric.unitNote,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        // Gemini 4 Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Gemini 4",
                style = MaterialTheme.typography.labelMedium,
                color = GoogleBlueLight,
                modifier = Modifier.width(72.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(metric.gemini4Score.coerceIn(0.08f, 1f))
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(GoogleBlue, GoogleGreen, GeminiPurple)
                            )
                        )
                )
            }
            Text(
                text = metric.gemini4Display,
                style = MaterialTheme.typography.labelMedium,
                color = GoogleGreen,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(86.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Claude Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Claude 4",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(72.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(10.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(metric.claudeScore.coerceIn(0.08f, 1f))
                        .height(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF94A3B8))
                )
            }
            Text(
                text = metric.claudeDisplay,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(86.dp)
            )
        }
    }
}

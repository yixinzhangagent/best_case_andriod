package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.ActiveDetailSheet
import com.example.data.model.PortalSection
import com.example.ui.MainViewModel
import com.example.ui.components.DomainSwitcherBar
import com.example.ui.components.GoogleFourColorBar
import com.example.ui.screens.AcademyPortalContent
import com.example.ui.screens.ActiveDetailScreenOverlay
import com.example.ui.screens.DevPortalContent
import com.example.ui.screens.GlobalSearchResultsOverlay
import com.example.ui.screens.LiveGeminiSandboxOverlay
import com.example.ui.screens.NewsPortalContent
import com.example.ui.screens.ResourcesPortalContent
import com.example.ui.screens.UseCasesPortalContent
import com.example.ui.theme.GoogleBlue
import com.example.ui.theme.GoogleBlueLight
import com.example.ui.theme.GoogleGreen
import com.example.ui.theme.GoogleRed
import com.example.ui.theme.GoogleYellow
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = uiState.isDarkTheme) {
                GeminiPortalApp(
                    viewModel = mainViewModel
                )
            }
        }
    }
}

@Composable
fun GeminiPortalApp(
    viewModel: MainViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.currentSection != PortalSection.DEV && uiState.activeDetail == null && !uiState.isSandboxOpen) {
        BackHandler {
            viewModel.selectSection(PortalSection.DEV)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 720.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                PortalTopHeader(
                    currentSection = uiState.currentSection,
                    searchQuery = uiState.searchQuery,
                    isDarkTheme = uiState.isDarkTheme,
                    onSelectSection = viewModel::selectSection,
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onClearSearch = viewModel::clearSearch,
                    onOpenCompareMatrix = {
                        viewModel.openDetail(ActiveDetailSheet.ClaudeVsGeminiMatrix)
                    },
                    onToggleTheme = viewModel::toggleTheme
                )
            },
            bottomBar = {
                if (!isWideScreen) {
                    PortalBottomNavBar(
                        currentSection = uiState.currentSection,
                        onSelectSection = viewModel::selectSection
                    )
                }
            },
            floatingActionButton = {
                if (uiState.activeDetail == null && !uiState.isSandboxOpen) {
                    ExtendedFloatingActionButton(
                        onClick = {
                            viewModel.openSandboxWithPreset(
                                title = "Google Gemini 4 实时交互沙盒",
                                modelLabel = "Gemini 4 Pro (gemini-3.1-pro-preview)",
                                systemInstruction = "你是 Google Gemini 4 全栈工程与多模态推理引擎。",
                                prompt = "请对比 Google Gemini 4 与 Claude 4 在 1000 万上下文、原生音视频流与 SWE-bench 工程修复上的核心差异，并给出一段 Kotlin 示例。"
                            )
                        },
                        containerColor = GoogleBlueLight,
                        contentColor = Color.White,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "打开 Gemini 4 沙盒"
                            )
                        },
                        text = {
                            Text("Gemini 4 沙盒", fontWeight = FontWeight.Bold)
                        },
                        modifier = Modifier.testTag("fab_open_sandbox")
                    )
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isWideScreen) {
                    PortalSideNavRail(
                        currentSection = uiState.currentSection,
                        onSelectSection = viewModel::selectSection
                    )
                }

                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    if (uiState.isSearchActive) {
                        GlobalSearchResultsOverlay(
                            query = uiState.searchQuery,
                            docs = viewModel.allDocs,
                            news = viewModel.allNews,
                            resources = viewModel.allResources,
                            courses = viewModel.allCourses,
                            useCases = viewModel.allUseCases,
                            onOpenDetail = viewModel::openDetail,
                            onCloseSearch = viewModel::clearSearch
                        )
                    } else {
                        when (uiState.currentSection) {
                            PortalSection.DEV -> DevPortalContent(
                                models = viewModel.modelSpecs,
                                docs = viewModel.allDocs,
                                selectedCategory = uiState.selectedDevCategory,
                                bookmarkedIds = uiState.bookmarkedItemIds,
                                onSelectCategory = viewModel::selectDevCategory,
                                onOpenDetail = viewModel::openDetail,
                                onToggleBookmark = viewModel::toggleBookmark,
                                onRunSandbox = viewModel::openSandboxWithPreset
                            )
                            PortalSection.NEWS -> NewsPortalContent(
                                news = viewModel.allNews,
                                selectedCategory = uiState.selectedNewsCategory,
                                onSelectCategory = viewModel::selectNewsCategory,
                                onOpenDetail = viewModel::openDetail,
                                onRunSandbox = viewModel::openSandboxWithPreset
                            )
                            PortalSection.RESOURCES -> ResourcesPortalContent(
                                resources = viewModel.allResources,
                                selectedType = uiState.selectedResourceType,
                                dailyRequestsMillion = uiState.dailyRequestsMillion,
                                avgContextTokensK = uiState.avgContextTokensK,
                                onSelectType = viewModel::selectResourceType,
                                onUpdateCalculator = viewModel::updateCalculatorParams,
                                onOpenDetail = viewModel::openDetail,
                                onRunSandbox = viewModel::openSandboxWithPreset
                            )
                            PortalSection.ACADEMY -> AcademyPortalContent(
                                courses = viewModel.allCourses,
                                selectedTrack = uiState.selectedAcademyTrack,
                                completedLessonKeys = uiState.completedLessonKeys,
                                completedCourseIds = uiState.completedCourseIds,
                                onSelectTrack = viewModel::selectAcademyTrack,
                                onOpenDetail = viewModel::openDetail,
                                onRunSandbox = viewModel::openSandboxWithPreset
                            )
                            PortalSection.USE_CASES -> UseCasesPortalContent(
                                useCases = viewModel.allUseCases,
                                selectedCategory = uiState.selectedUseCaseCategory,
                                onSelectCategory = viewModel::selectUseCaseCategory,
                                onOpenDetail = viewModel::openDetail,
                                onRunSandbox = viewModel::openSandboxWithPreset
                            )
                        }
                    }

                    // Active Detail Sheet Overlay
                    val activeDetail = uiState.activeDetail
                    if (activeDetail != null) {
                        ActiveDetailScreenOverlay(
                            detail = activeDetail,
                            completedLessonKeys = uiState.completedLessonKeys,
                            completedCourseIds = uiState.completedCourseIds,
                            onClose = viewModel::closeDetail,
                            onMarkLessonComplete = viewModel::markLessonCompleted,
                            onMarkCourseCertified = viewModel::markCourseCertified,
                            onRunSandbox = viewModel::openSandboxWithPreset
                        )
                    }

                    // Live Gemini 4 API Sandbox Overlay
                    if (uiState.isSandboxOpen) {
                        LiveGeminiSandboxOverlay(
                            title = uiState.sandboxTitle,
                            selectedModel = uiState.sandboxSelectedModel,
                            systemInstruction = uiState.sandboxSystemInstruction,
                            prompt = uiState.sandboxPrompt,
                            isRunning = uiState.isSandboxRunning,
                            result = uiState.sandboxResult,
                            onUpdateInputs = viewModel::updateSandboxInputs,
                            onExecute = viewModel::executeSandboxPrompt,
                            onClose = viewModel::closeSandbox
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PortalTopHeader(
    currentSection: PortalSection,
    searchQuery: String,
    isDarkTheme: Boolean,
    onSelectSection: (PortalSection) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onOpenCompareMatrix: () -> Unit,
    onToggleTheme: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            GoogleFourColorBar()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Google 4-Color Gemini Badge
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(GoogleBlueLight, GoogleRed, GoogleYellow, GoogleGreen)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "G4",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Text(
                            text = "Google Gemini 4 生态门户",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${currentSection.geminiDomain} · ${currentSection.claudeCounterpart}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onOpenCompareMatrix,
                        modifier = Modifier.testTag("top_compare_matrix_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CompareArrows,
                            contentDescription = "对比 Claude",
                            tint = GoogleGreen
                        )
                    }
                    IconButton(
                        onClick = onToggleTheme,
                        modifier = Modifier.testTag("toggle_theme_btn")
                    ) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "切换深浅色主题",
                            tint = GoogleYellow
                        )
                    }
                }
            }

            // Global Cross-Portal Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        text = "搜索 gemini4.dev 文档、新闻、提示词、课程或案例…",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = GoogleBlueLight
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = onClearSearch) {
                            Icon(Icons.Default.Clear, contentDescription = "清除搜索")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp)
                    .testTag("global_search_input")
            )

            // 1:1 Website Domain Switcher Pills
            DomainSwitcherBar(
                currentSection = currentSection,
                onSelectSection = onSelectSection
            )
        }
    }
}

@Composable
private fun PortalBottomNavBar(
    currentSection: PortalSection,
    onSelectSection: (PortalSection) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .testTag("portal_bottom_nav")
    ) {
        PortalSection.entries.forEach { section ->
            NavigationBarItem(
                selected = currentSection == section,
                onClick = { onSelectSection(section) },
                icon = {
                    Icon(
                        imageVector = section.icon(),
                        contentDescription = section.title
                    )
                },
                label = {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1
                    )
                },
                modifier = Modifier.testTag("nav_tab_${section.id}")
            )
        }
    }
}

@Composable
private fun PortalSideNavRail(
    currentSection: PortalSection,
    onSelectSection: (PortalSection) -> Unit
) {
    NavigationRail(
        modifier = Modifier
            .fillMaxHeight()
            .testTag("portal_side_rail")
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        PortalSection.entries.forEach { section ->
            NavigationRailItem(
                selected = currentSection == section,
                onClick = { onSelectSection(section) },
                icon = {
                    Icon(
                        imageVector = section.icon(),
                        contentDescription = section.title
                    )
                },
                label = {
                    Text(section.title, style = MaterialTheme.typography.labelSmall)
                },
                modifier = Modifier.testTag("rail_tab_${section.id}")
            )
        }
    }
}

private fun PortalSection.icon(): ImageVector = when (this) {
    PortalSection.DEV -> Icons.Default.Code
    PortalSection.NEWS -> Icons.Default.Newspaper
    PortalSection.RESOURCES -> Icons.Default.FolderSpecial
    PortalSection.ACADEMY -> Icons.Default.School
    PortalSection.USE_CASES -> Icons.Default.RocketLaunch
}

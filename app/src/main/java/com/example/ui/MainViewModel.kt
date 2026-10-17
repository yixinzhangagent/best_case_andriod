package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AcademyCourseItem
import com.example.data.model.ActiveDetailSheet
import com.example.data.model.DocSectionItem
import com.example.data.model.GeminiModelSpec
import com.example.data.model.NewsArticleItem
import com.example.data.model.PortalSection
import com.example.data.model.ResourceAssetItem
import com.example.data.model.UseCaseItem
import com.example.data.network.GeminiSandboxService
import com.example.data.network.SandboxResult
import com.example.data.repository.GeminiPortalRepository
import com.example.data.repository.UseCaseAndAcademyData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PortalUiState(
    val currentSection: PortalSection = PortalSection.DEV,
    val isDarkTheme: Boolean = true,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val selectedDevCategory: String = "全部",
    val selectedNewsCategory: String = "全部",
    val selectedResourceType: String = "全部",
    val selectedAcademyTrack: String = "全部",
    val selectedUseCaseCategory: String = "全部",
    val activeDetail: ActiveDetailSheet? = null,
    val completedLessonKeys: Set<String> = setOf("course-101-foundations:1"),
    val completedCourseIds: Set<String> = emptySet(),
    val bookmarkedItemIds: Set<String> = setOf("doc-quickstart", "res-claude-migrator", "uc-monorepo-refactoring"),
    // TCO Cost Calculator in Resources
    val dailyRequestsMillion: Float = 2.5f,
    val avgContextTokensK: Float = 180f,
    // Live Gemini 4 API Sandbox State
    val isSandboxOpen: Boolean = false,
    val sandboxTitle: String = "Gemini 4 实时 API 沙盒",
    val sandboxSelectedModel: String = "Gemini 4 Pro (gemini-3.1-pro-preview)",
    val sandboxSystemInstruction: String = "",
    val sandboxPrompt: String = "",
    val sandboxFallbackOutput: String = "",
    val isSandboxRunning: Boolean = false,
    val sandboxResult: SandboxResult? = null
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PortalUiState())
    val uiState: StateFlow<PortalUiState> = _uiState.asStateFlow()

    val modelSpecs: List<GeminiModelSpec> = GeminiPortalRepository.modelSpecs
    val allDocs: List<DocSectionItem> = GeminiPortalRepository.docSections
    val allNews: List<NewsArticleItem> = GeminiPortalRepository.newsArticles
    val allResources: List<ResourceAssetItem> = GeminiPortalRepository.resourceAssets
    val allCourses: List<AcademyCourseItem> = UseCaseAndAcademyData.academyCourses
    val allUseCases: List<UseCaseItem> = UseCaseAndAcademyData.useCases

    fun selectSection(section: PortalSection) {
        _uiState.update {
            it.copy(
                currentSection = section,
                activeDetail = null,
                isSearchActive = false
            )
        }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                isSearchActive = query.isNotBlank()
            )
        }
    }

    fun clearSearch() {
        _uiState.update { it.copy(searchQuery = "", isSearchActive = false) }
    }

    fun selectDevCategory(category: String) {
        _uiState.update { it.copy(selectedDevCategory = category) }
    }

    fun selectNewsCategory(category: String) {
        _uiState.update { it.copy(selectedNewsCategory = category) }
    }

    fun selectResourceType(type: String) {
        _uiState.update { it.copy(selectedResourceType = type) }
    }

    fun selectAcademyTrack(track: String) {
        _uiState.update { it.copy(selectedAcademyTrack = track) }
    }

    fun selectUseCaseCategory(category: String) {
        _uiState.update { it.copy(selectedUseCaseCategory = category) }
    }

    fun openDetail(detail: ActiveDetailSheet) {
        _uiState.update { it.copy(activeDetail = detail) }
    }

    fun closeDetail() {
        _uiState.update { it.copy(activeDetail = null) }
    }

    fun toggleBookmark(id: String) {
        _uiState.update { state ->
            val updated = if (id in state.bookmarkedItemIds) {
                state.bookmarkedItemIds - id
            } else {
                state.bookmarkedItemIds + id
            }
            state.copy(bookmarkedItemIds = updated)
        }
    }

    fun markLessonCompleted(courseId: String, lessonNumber: Int) {
        val key = "$courseId:$lessonNumber"
        _uiState.update { state ->
            state.copy(completedLessonKeys = state.completedLessonKeys + key)
        }
    }

    fun markCourseCertified(courseId: String) {
        _uiState.update { state ->
            state.copy(completedCourseIds = state.completedCourseIds + courseId)
        }
    }

    fun updateCalculatorParams(dailyRequestsM: Float, avgContextK: Float) {
        _uiState.update {
            it.copy(
                dailyRequestsMillion = dailyRequestsM.coerceIn(0.2f, 20f),
                avgContextTokensK = avgContextK.coerceIn(10f, 2000f)
            )
        }
    }

    fun openSandboxWithPreset(
        title: String,
        modelLabel: String = "Gemini 4 Pro (gemini-3.1-pro-preview)",
        systemInstruction: String,
        prompt: String,
        expectedOutput: String = ""
    ) {
        _uiState.update {
            it.copy(
                isSandboxOpen = true,
                sandboxTitle = title,
                sandboxSelectedModel = modelLabel,
                sandboxSystemInstruction = systemInstruction,
                sandboxPrompt = prompt,
                sandboxFallbackOutput = expectedOutput,
                sandboxResult = null
            )
        }
        executeSandboxPrompt()
    }

    fun updateSandboxInputs(
        modelLabel: String? = null,
        systemInstruction: String? = null,
        prompt: String? = null
    ) {
        _uiState.update {
            it.copy(
                sandboxSelectedModel = modelLabel ?: it.sandboxSelectedModel,
                sandboxSystemInstruction = systemInstruction ?: it.sandboxSystemInstruction,
                sandboxPrompt = prompt ?: it.sandboxPrompt
            )
        }
    }

    fun executeSandboxPrompt() {
        val snapshot = _uiState.value
        if (snapshot.sandboxPrompt.isBlank() || snapshot.isSandboxRunning) return

        _uiState.update { it.copy(isSandboxRunning = true) }
        viewModelScope.launch {
            val result = GeminiSandboxService.runPrompt(
                modelLabel = snapshot.sandboxSelectedModel,
                systemInstruction = snapshot.sandboxSystemInstruction,
                userPrompt = snapshot.sandboxPrompt,
                fallbackOutput = snapshot.sandboxFallbackOutput
            )
            _uiState.update {
                it.copy(
                    isSandboxRunning = false,
                    sandboxResult = result
                )
            }
        }
    }

    fun closeSandbox() {
        _uiState.update { it.copy(isSandboxOpen = false) }
    }
}

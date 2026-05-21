package com.codepad.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Input
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codepad.app.ui.components.CodeEditor
import com.codepad.app.ui.components.CodePadTopBar
import com.codepad.app.ui.components.ConsoleOutput
import com.codepad.app.ui.components.InputPanel
import com.codepad.app.ui.components.LanguageSelector
import com.codepad.app.ui.components.SettingsDialog

@Composable
fun CodePadScreen(
    viewModel: CodePadViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > 600

    Scaffold(
        topBar = {
            CodePadTopBar(
                onRun = viewModel::executeCode,
                onNew = viewModel::newFile,
                onSettings = viewModel::toggleSettings,
                isExecuting = uiState.isExecuting
            )
        },
        bottomBar = {
            if (!isLandscape) {
                BottomTabBar(
                    activeTab = uiState.activeTab,
                    onTabSelected = viewModel::switchTab,
                    isExecuting = uiState.isExecuting
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Language selector row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LanguageSelector(
                    selectedLanguage = uiState.selectedLanguage,
                    onLanguageSelected = viewModel::selectLanguage
                )
                if (uiState.isExecuting) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(start = 12.dp).size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            if (isLandscape) {
                LandscapeLayout(
                    uiState = uiState,
                    onCodeChange = viewModel::updateCode,
                    onInputChange = viewModel::updateInput,
                    onClearInput = viewModel::clearInput,
                    onClearOutput = viewModel::clearOutput
                )
            } else {
                PortraitLayout(
                    uiState = uiState,
                    onCodeChange = viewModel::updateCode,
                    onInputChange = viewModel::updateInput,
                    onClearInput = viewModel::clearInput,
                    onClearOutput = viewModel::clearOutput
                )
            }
        }

        if (uiState.showSettings) {
            SettingsDialog(
                serverUrl = uiState.serverUrl,
                onServerUrlChange = viewModel::updateServerUrl,
                onDismiss = viewModel::toggleSettings
            )
        }
    }
}

@Composable
private fun PortraitLayout(
    uiState: CodePadUiState,
    onCodeChange: (String) -> Unit,
    onInputChange: (String) -> Unit,
    onClearInput: () -> Unit,
    onClearOutput: () -> Unit
) {
    AnimatedContent(
        targetState = uiState.activeTab,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        modifier = Modifier.fillMaxSize(),
        label = "tab_content"
    ) { tab ->
        when (tab) {
            Tab.EDITOR -> CodeEditor(
                code = uiState.code,
                onCodeChange = onCodeChange
            )
            Tab.INPUT -> InputPanel(
                input = uiState.input,
                onInputChange = onInputChange,
                onClear = onClearInput
            )
            Tab.CONSOLE -> ConsoleOutput(
                output = uiState.output,
                onClear = onClearOutput
            )
        }
    }
}

@Composable
private fun LandscapeLayout(
    uiState: CodePadUiState,
    onCodeChange: (String) -> Unit,
    onInputChange: (String) -> Unit,
    onClearInput: () -> Unit,
    onClearOutput: () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        // Editor takes 2/3
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
        ) {
            CodeEditor(
                code = uiState.code,
                onCodeChange = onCodeChange
            )
        }

        VerticalDivider(color = MaterialTheme.colorScheme.outline)

        // Input + Console takes 1/3
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                InputPanel(
                    input = uiState.input,
                    onInputChange = onInputChange,
                    onClear = onClearInput
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                ConsoleOutput(
                    output = uiState.output,
                    onClear = onClearOutput
                )
            }
        }
    }
}

@Composable
private fun BottomTabBar(
    activeTab: Tab,
    onTabSelected: (Tab) -> Unit,
    isExecuting: Boolean
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Code, contentDescription = "Editor") },
            label = { Text("Editor") },
            selected = activeTab == Tab.EDITOR,
            onClick = { onTabSelected(Tab.EDITOR) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Input, contentDescription = "Input") },
            label = { Text("Input") },
            selected = activeTab == Tab.INPUT,
            onClick = { onTabSelected(Tab.INPUT) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Terminal, contentDescription = "Console") },
            label = { Text("Console") },
            selected = activeTab == Tab.CONSOLE,
            onClick = { onTabSelected(Tab.CONSOLE) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            )
        )
    }
}

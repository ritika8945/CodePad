package com.codepad.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codepad.app.ui.theme.CodeFontFamily

@Composable
fun CodeEditor(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()
    val lineCount = remember(code) { code.lines().size.coerceAtLeast(1) }

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Line numbers
        Box(
            modifier = Modifier
                .width(44.dp)
                .verticalScroll(verticalScroll)
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = (1..lineCount).joinToString("\n"),
                style = TextStyle(
                    fontFamily = CodeFontFamily,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    textAlign = TextAlign.End
                ),
                modifier = Modifier.padding(end = 8.dp)
            )
        }

        // Divider line
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        )

        // Code area
        Box(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(verticalScroll)
                .horizontalScroll(horizontalScroll)
                .padding(12.dp)
        ) {
            BasicTextField(
                value = code,
                onValueChange = onCodeChange,
                textStyle = TextStyle(
                    fontFamily = CodeFontFamily,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    Box {
                        if (code.isEmpty()) {
                            Text(
                                text = "Write your code here...",
                                style = TextStyle(
                                    fontFamily = CodeFontFamily,
                                    fontSize = 13.sp,
                                    lineHeight = 20.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

package presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.graphics.toColorInt
import data.colorMap
import data.defaultColor
import data.defaultColorName

@Composable
fun DynamicColorThemeScreen() {
    var colorInput by remember { mutableStateOf(TextFieldValue(defaultColorName)) }
    var showPopup by remember { mutableStateOf(false) }

    val parsedColor = remember(colorInput.text) {
        val input = colorInput.text.trim()

        val matchedColor = colorMap.entries
            .firstOrNull { it.key.equals(input, ignoreCase = true) }
            ?.value

        when {
            matchedColor != null -> matchedColor
            Regex("^#[0-9a-fA-F]{6}$").matches(input) -> {
                try {
                    Color(input.toColorInt())
                } catch (e: Exception) {
                    defaultColor
                }
            }
            else -> defaultColor
        }
    }


    val animatedBackground by animateColorAsState(
        targetValue = parsedColor!!,
        label = "BackgroundColor",
        animationSpec = tween(durationMillis = 1000)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBackground)
    ) {
        Text(
            text = "Dynamic color theme",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.W600),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 24.dp),
            textAlign = TextAlign.Center
        )

        IconButton(
            onClick = { showPopup = !showPopup },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info"
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.8f))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            BasicTextField(
                value = colorInput,
                onValueChange = { colorInput = it },
                singleLine = true,

                modifier = Modifier.fillMaxWidth(0.75f),
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
            )
        }

        if (showPopup) {
            Popup(
                alignment = Alignment.TopEnd,
                offset = IntOffset(x = -32, y = 72),
                properties = PopupProperties(
                    focusable = true,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = { showPopup = false }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .fillMaxHeight(0.3f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {
                    Column {
                        colorMap.entries.sortedBy { it.key }.forEach { (name, color) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        colorInput = TextFieldValue(name)
                                        showPopup = false
                                    }
                                    .padding(6.dp)
                            ) {
                                Text(
                                    text = name,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f),
                                    color = Color.DarkGray
                                )
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(color)
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}

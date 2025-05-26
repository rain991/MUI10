package presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.IntOffset
import androidx.core.graphics.toColorInt

@Composable
fun DynamicColorThemeScreen() {
    var colorInput by remember { mutableStateOf(TextFieldValue("")) }
    var showPopup by remember { mutableStateOf(false) }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val colorMap = remember {
        mapOf(
            "Red" to Color.Red, "Pink" to Color(0xFFFFC0CB), "Cyan" to Color.Cyan,
            "Blue" to Color.Blue, "Green" to Color.Green, "Yellow" to Color.Yellow,
            "Gray" to Color.Gray, "Magenta" to Color.Magenta, "Orange" to Color(0xFFFFA500),
            "Purple" to Color(0xFF800080), "Brown" to Color(0xFFA52A2A), "Maroon" to Color(0xFF800000),
            "Olive" to Color(0xFF808000), "Teal" to Color(0xFF008080), "Navy" to Color(0xFF000080),
            "Lime" to Color(0xFF00FF00), "Indigo" to Color(0xFF4B0082), "Gold" to Color(0xFFFFD700),
            "Beige" to Color(0xFFF5F5DC), "Ivory" to Color(0xFFFFFFF0), "Coral" to Color(0xFFFF7F50),
            "Salmon" to Color(0xFFFA8072), "Khaki" to Color(0xFFF0E68C), "Lavender" to Color(0xFFE6E6FA),
            "Mint" to Color(0xFF98FF98), "Turquoise" to Color(0xFF40E0D0), "Azure" to Color(0xFF007FFF),
            "Crimson" to Color(0xFFDC143C), "Plum" to Color(0xFFDDA0DD), "Slate" to Color(0xFF708090)
        )
    }

    val parsedColor = remember(colorInput.text) {
        when {
            colorMap.containsKey(colorInput.text.trim()) -> colorMap[colorInput.text.trim()]!!
            Regex("^#[0-9a-fA-F]{6}$").matches(colorInput.text.trim()) -> {
                try {
                    Color(colorInput.text.trim().toColorInt())
                } catch (e: Exception) {
                    Color.White
                }
            }
            else -> Color.White
        }
    }

    val animatedBackground by animateColorAsState(targetValue = parsedColor, label = "BackgroundColor")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBackground)
    ) {
        Text(
            text = "Dynamic color theme",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.W600),
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
                properties = PopupProperties(focusable = true, dismissOnBackPress = true, dismissOnClickOutside = true),
                onDismissRequest = { showPopup = false }
            ) {
                Box(
                    modifier = Modifier
                        .width(screenWidth * 0.3f)
                        .height(screenHeight * 3f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {
                    Column {
                        colorMap.keys.sorted().forEach { name ->
                            Text(
                                text = name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        colorInput = TextFieldValue(name)
                                        showPopup = false
                                    }
                                    .padding(6.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

package com.v2ray.ang.ui.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v2ray.ang.ui.compose.glass

private enum class NavIconKind { Home, Configs, Logs, Settings }

/**
 * Glass navigation bar at the bottom of the main screen: Home, Configs, Logs, Settings.
 * Home and Configs are tabs of the main screen; Logs and Settings open their own screens.
 */
@Composable
fun MainNavBar(
    selectedTab: Int,
    onHome: () -> Unit,
    onConfigs: () -> Unit,
    onLogs: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .glass(RoundedCornerShape(28.dp))
                .padding(horizontal = 6.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem("Home", NavIconKind.Home, selectedTab == 0, onHome, Modifier.weight(1f))
            NavItem("Configs", NavIconKind.Configs, selectedTab == 1, onConfigs, Modifier.weight(1f))
            NavItem("Logs", NavIconKind.Logs, false, onLogs, Modifier.weight(1f))
            NavItem("Settings", NavIconKind.Settings, false, onSettings, Modifier.weight(1f))
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    kind: NavIconKind,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (selected) MaterialTheme.colorScheme.secondary
    else MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .clickable(role = Role.Tab, onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavIcon(kind = kind, color = color)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = color,
            fontSize = 11.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun NavIcon(kind: NavIconKind, color: Color) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val sw = 2.dp.toPx()
        val stroke = Stroke(width = sw, cap = StrokeCap.Round, join = StrokeJoin.Round)
        when (kind) {
            NavIconKind.Home -> {
                val path = Path().apply {
                    moveTo(w * 0.12f, h * 0.48f)
                    lineTo(w * 0.5f, h * 0.14f)
                    lineTo(w * 0.88f, h * 0.48f)
                    moveTo(w * 0.22f, h * 0.42f)
                    lineTo(w * 0.22f, h * 0.84f)
                    lineTo(w * 0.78f, h * 0.84f)
                    lineTo(w * 0.78f, h * 0.42f)
                }
                drawPath(path = path, color = color, style = stroke)
            }

            NavIconKind.Configs -> {
                for (i in 0..2) {
                    val y = h * (0.25f + 0.25f * i)
                    drawCircle(color = color, radius = sw * 0.7f, center = Offset(w * 0.14f, y))
                    drawLine(
                        color = color,
                        start = Offset(w * 0.4f, y),
                        end = Offset(w * 0.9f, y),
                        strokeWidth = sw,
                        cap = StrokeCap.Round
                    )
                }
            }

            NavIconKind.Logs -> {
                drawCircle(color = color, radius = w * 0.4f, style = stroke)
                drawLine(
                    color = color,
                    start = Offset(w * 0.5f, h * 0.5f),
                    end = Offset(w * 0.5f, h * 0.28f),
                    strokeWidth = sw,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = color,
                    start = Offset(w * 0.5f, h * 0.5f),
                    end = Offset(w * 0.66f, h * 0.58f),
                    strokeWidth = sw,
                    cap = StrokeCap.Round
                )
            }

            NavIconKind.Settings -> {
                drawLine(
                    color = color,
                    start = Offset(w * 0.12f, h * 0.3f),
                    end = Offset(w * 0.88f, h * 0.3f),
                    strokeWidth = sw,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = color,
                    start = Offset(w * 0.12f, h * 0.7f),
                    end = Offset(w * 0.88f, h * 0.7f),
                    strokeWidth = sw,
                    cap = StrokeCap.Round
                )
                drawCircle(color = color, radius = w * 0.11f, center = Offset(w * 0.64f, h * 0.3f), style = stroke)
                drawCircle(color = color, radius = w * 0.11f, center = Offset(w * 0.36f, h * 0.7f), style = stroke)
            }
        }
    }
}

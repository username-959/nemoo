package com.v2ray.ang.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v2ray.ang.R
import com.v2ray.ang.ui.compose.GlassCard

/**
 * Home tab: app name on top, a big glowing power button in the middle and a "Select Server"
 * card at the bottom. Tapping the status text while connected tests the current server.
 */
@Composable
fun MainHome(
    isRunning: Boolean,
    statusText: String,
    onMenuClick: () -> Unit,
    onTogglePower: () -> Unit,
    onTestConnection: () -> Unit,
    onSelectServer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu_24dp),
                    contentDescription = stringResource(R.string.acc_open_menu)
                )
            }
            Text(
                text = stringResource(R.string.app_name),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(letterSpacing = 4.sp)
            )
            Spacer(modifier = Modifier.size(48.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        PowerButton(isRunning = isRunning, onClick = onTogglePower)

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = if (isRunning) "Connected" else "Disconnected",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = if (isRunning) statusText else "Tap to connect",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .then(if (isRunning) Modifier.clickable(onClick = onTestConnection) else Modifier)
        )

        Spacer(modifier = Modifier.weight(1f))

        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 12.dp),
            onClick = onSelectServer
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlobeIcon(color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Select Server",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "\u203A",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/** Big round power button. The ring and glow light up while connected. */
@Composable
fun PowerButton(
    isRunning: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = MaterialTheme.colorScheme.secondary
    val idle = MaterialTheme.colorScheme.outline
    val ringColor by animateColorAsState(
        targetValue = if (isRunning) accent else idle,
        animationSpec = tween(500),
        label = "powerRing"
    )
    val glowAlpha by animateFloatAsState(
        targetValue = if (isRunning) 0.55f else 0.16f,
        animationSpec = tween(600),
        label = "powerGlow"
    )
    val pulse = rememberInfiniteTransition(label = "powerPulse")
    val pulseScale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = 1.07f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "powerPulseScale"
    )
    val description = stringResource(if (isRunning) R.string.acc_stop else R.string.acc_start)

    Box(
        modifier = modifier.size(230.dp),
        contentAlignment = Alignment.Center
    ) {
        // Soft glow behind the button
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    val s = if (isRunning) pulseScale else 1f
                    scaleX = s
                    scaleY = s
                }
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(accent.copy(alpha = glowAlpha), Color.Transparent)
                    ),
                    shape = CircleShape
                )
        )
        // The button itself
        Box(
            modifier = Modifier
                .size(170.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0F1B30), Color(0xFF060C18))
                    )
                )
                .border(3.dp, ringColor, CircleShape)
                .semantics { contentDescription = description }
                .clickable(role = Role.Button, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            PowerGlyph(
                color = ringColor,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}

/** The classic power symbol: an open ring with a short line at the top. */
@Composable
private fun PowerGlyph(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.09f
        val radius = size.minDimension / 2f - strokeWidth
        val c = center
        drawArc(
            color = color,
            startAngle = -50f,
            sweepAngle = 280f,
            useCenter = false,
            topLeft = Offset(c.x - radius, c.y - radius),
            size = Size(radius * 2f, radius * 2f),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        drawLine(
            color = color,
            start = Offset(c.x, c.y - radius),
            end = Offset(c.x, c.y - radius * 0.1f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun GlobeIcon(color: Color) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val sw = 1.8.dp.toPx()
        val stroke = Stroke(width = sw)
        val r = size.minDimension / 2f - sw
        drawCircle(color = color, radius = r, style = stroke)
        drawOval(
            color = color,
            topLeft = Offset(center.x - r * 0.45f, center.y - r),
            size = Size(r * 0.9f, r * 2f),
            style = stroke
        )
        drawLine(
            color = color,
            start = Offset(center.x - r, center.y),
            end = Offset(center.x + r, center.y),
            strokeWidth = sw
        )
    }
}

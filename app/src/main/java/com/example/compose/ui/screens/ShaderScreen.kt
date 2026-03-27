package com.example.compose.ui.screens

import android.graphics.RuntimeShader
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// ── AGSL shader sources ─────────────────────────────────────────────────

private val AURORA_SHADER = """
    uniform float2 iResolution;
    uniform float  iTime;

    float hash(float2 p) {
        return fract(sin(dot(p, float2(127.1, 311.7))) * 43758.5453);
    }

    float noise(float2 p) {
        float2 i = floor(p);
        float2 f = fract(p);
        f = f * f * (3.0 - 2.0 * f);
        return mix(
            mix(hash(i), hash(i + float2(1.0, 0.0)), f.x),
            mix(hash(i + float2(0.0, 1.0)), hash(i + float2(1.0, 1.0)), f.x),
            f.y
        );
    }

    half4 main(float2 fragCoord) {
        float2 uv = fragCoord / iResolution;
        float t = iTime * 0.4;

        float n1 = noise(uv * 3.0 + float2(t, t * 0.7));
        float n2 = noise(uv * 5.0 - float2(t * 0.5, t));
        float n3 = noise(uv * 7.0 + float2(t * 0.3, -t * 0.6));

        float wave = sin(uv.x * 6.28 + t + n1 * 3.0) * 0.5 + 0.5;
        wave *= smoothstep(0.0, 0.4, uv.y) * smoothstep(1.0, 0.5, uv.y);

        float3 col1 = float3(0.1, 0.8, 0.6);  // teal
        float3 col2 = float3(0.3, 0.2, 0.9);  // purple
        float3 col3 = float3(0.9, 0.3, 0.5);  // pink

        float3 color = mix(col1, col2, n1);
        color = mix(color, col3, n2 * 0.5);
        color *= wave * (0.8 + n3 * 0.4);
        color += 0.02;  // slight ambient so it's never pure black

        return half4(color, 1.0);
    }
""".trimIndent()

private val LIQUID_SHADER = """
    uniform float2 iResolution;
    uniform float  iTime;

    half4 main(float2 fragCoord) {
        float2 uv = (fragCoord * 2.0 - iResolution) / min(iResolution.x, iResolution.y);
        float t = iTime * 0.6;

        // Layered sine distortion
        for (float i = 1.0; i < 8.0; i += 1.0) {
            uv.x += 0.6 / i * sin(i * 2.0 * uv.y + t);
            uv.y += 0.6 / i * cos(i * 2.0 * uv.x + t);
        }

        float r = 0.5 + 0.5 * sin(uv.x + t);
        float g = 0.5 + 0.5 * cos(uv.y + t * 0.7);
        float b = 0.5 + 0.5 * sin(uv.x + uv.y + t * 1.3);

        float3 color = float3(r, g, b);
        color = pow(color, float3(1.2));
        return half4(color, 1.0);
    }
""".trimIndent()

private val VORONOI_SHADER = """
    uniform float2 iResolution;
    uniform float  iTime;

    float2 random2(float2 p) {
        return fract(sin(float2(
            dot(p, float2(127.1, 311.7)),
            dot(p, float2(269.5, 183.3))
        )) * 43758.5453);
    }

    half4 main(float2 fragCoord) {
        float2 uv = fragCoord / iResolution;
        uv.x *= iResolution.x / iResolution.y;
        float2 st = uv * 5.0;

        float2 i_st = floor(st);
        float2 f_st = fract(st);

        float minDist = 1.0;
        float2 minPoint;

        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                float2 neighbor = float2(float(x), float(y));
                float2 point = random2(i_st + neighbor);
                point = 0.5 + 0.5 * sin(iTime * 0.8 + 6.2831 * point);
                float2 diff = neighbor + point - f_st;
                float dist = length(diff);
                if (dist < minDist) {
                    minDist = dist;
                    minPoint = point;
                }
            }
        }

        float3 col1 = float3(0.05, 0.05, 0.15);
        float3 col2 = float3(0.2, 0.6, 1.0);
        float3 col3 = float3(1.0, 0.4, 0.7);

        float3 color = mix(col1, col2, minDist);
        color = mix(color, col3, minPoint.x * 0.6);
        color += (1.0 - minDist) * 0.15;

        return half4(color, 1.0);
    }
""".trimIndent()

// ── Composable screen ───────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShaderScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shaders (AGSL)") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        ShaderContent(modifier = Modifier.padding(padding))
    }
}

@Composable
private fun ShaderContent(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "shader")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 100_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "time",
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        ShaderCard(title = "Aurora Borealis", shaderSrc = AURORA_SHADER, time = time)
        ShaderCard(title = "Liquid Warp", shaderSrc = LIQUID_SHADER, time = time)
        ShaderCard(title = "Voronoi Cells", shaderSrc = VORONOI_SHADER, time = time)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun ShaderCard(title: String, shaderSrc: String, time: Float) {
    val shader = remember(shaderSrc) { RuntimeShader(shaderSrc) }
    val brush = remember(shaderSrc) { ShaderBrush(shader) }

    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp)),
        ) {
            shader.setFloatUniform("iResolution", size.width, size.height)
            shader.setFloatUniform("iTime", time)
            drawRect(brush = brush)
        }
    }
}

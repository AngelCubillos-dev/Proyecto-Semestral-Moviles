package com.example.mastermartini.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mastermartini.model.ContentItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/** Foto de perfil (archivo local) o, si no hay, un círculo con las iniciales. */
@Composable
fun UserAvatar(photoPath: String, name: String, size: androidx.compose.ui.unit.Dp, modifier: Modifier = Modifier) {
    val bitmap by produceState<Bitmap?>(initialValue = null, photoPath) {
        value = if (photoPath.isBlank()) null else withContext(Dispatchers.IO) {
            runCatching {
                val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                BitmapFactory.decodeFile(photoPath, bounds)
                val sample = (bounds.outWidth / 512).coerceAtLeast(1)
                BitmapFactory.decodeFile(photoPath, BitmapFactory.Options().apply { inSampleSize = sample })
            }.getOrNull()
        }
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        val current = bitmap
        if (current != null) {
            Image(
                bitmap = current.asImageBitmap(),
                contentDescription = "Foto de perfil de $name",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            val initial = name.trim().firstOrNull()?.uppercase()
            if (initial != null) {
                Text(initial, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            } else {
                Icon(Icons.Filled.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

/**
 * Reproductor de video técnico.
 *
 * - Si existe res/raw/<rawVideoName>.mp4 se reproduce con un VideoView real.
 * - Si no existe, se muestra un reproductor simulado (portada + progreso animado),
 *   ideal para probar la organización de contenidos sin material audiovisual.
 */
@Composable
fun VideoPlayer(video: ContentItem, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val rawId = remember(video.rawVideoName) {
        video.rawVideoName?.let { context.resources.getIdentifier(it, "raw", context.packageName) } ?: 0
    }
    if (rawId != 0) {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(MaterialTheme.shapes.medium)
                .background(Color.Black),
            factory = { ctx ->
                VideoView(ctx).apply {
                    val controller = MediaController(ctx)
                    controller.setAnchorView(this)
                    setMediaController(controller)
                    setVideoURI(Uri.parse("android.resource://${ctx.packageName}/$rawId"))
                }
            }
        )
    } else {
        SimulatedPlayer(video, modifier)
    }
}

@Composable
private fun SimulatedPlayer(video: ContentItem, modifier: Modifier = Modifier) {
    var playing by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(progress, tween(250, easing = LinearEasing), label = "videoProgress")

    LaunchedEffect(playing) {
        while (playing && progress < 1f) {
            delay(250)
            progress = (progress + 0.01f).coerceAtMost(1f)
            if (progress >= 1f) playing = false
        }
    }

    Column(modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(MaterialTheme.shapes.medium)
        ) {
            Image(
                painter = painterResource(video.imageRes),
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(Modifier.fillMaxSize().background(Color(0x66000000)))
            Surface(
                modifier = Modifier.align(Alignment.Center),
                shape = CircleShape,
                color = Color(0xCCFFFFFF)
            ) {
                IconButton(onClick = { if (progress >= 1f) progress = 0f; playing = !playing }, modifier = Modifier.size(72.dp)) {
                    AnimatedContent(targetState = playing, label = "playPause") { isPlaying ->
                        Icon(
                            if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
        )
        Row(Modifier.fillMaxWidth().padding(top = 4.dp)) {
            Text(
                "Reproductor simulado: agrega el video en res/raw/${video.rawVideoName ?: "tu_video"}.mp4",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

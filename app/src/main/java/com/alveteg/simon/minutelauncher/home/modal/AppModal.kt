package com.alveteg.simon.minutelauncher.home.modal

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alveteg.simon.minutelauncher.Event
import com.alveteg.simon.minutelauncher.data.AppInfo
import com.alveteg.simon.minutelauncher.theme.archivoBlackFamily
import com.alveteg.simon.minutelauncher.theme.archivoFamily
import kotlinx.coroutines.delay
import timber.log.Timber


@Composable
fun AppModal(
  appInfo: AppInfo,
  onEvent: (Event) -> Unit,
  onConfirmation: () -> Unit,
  onCancel: () -> Unit,
  onChangeTimer: () -> Unit
) {
  var enabled by remember { mutableStateOf(false) }
  var timer by remember { mutableIntStateOf(0) }
  val usage by remember { mutableStateOf(appInfo.usage) }
  var confirmationText by remember { mutableStateOf("") }
  val animationPeriod = when (appInfo.app.timer) {
    2 -> 700
    5 -> 500
    10 -> 250
    15 -> 150
    else -> Int.MAX_VALUE
  }
  val infiniteTransition = rememberInfiniteTransition(label = "Put the phone down button")
  val scale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.03f,
    animationSpec = infiniteRepeatable(
      animation = tween(animationPeriod, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "Pulsating Button"
  )

  LaunchedEffect(appInfo) {
    Timber.d("New timer: ${appInfo.app.timer}")
    timer = appInfo.app.timer
  }

  LaunchedEffect(key1 = timer) {
    confirmationText = "Wait ${timer}s.."
    if (timer > 0 && !enabled) {
      delay(1000L)
      timer -= 1
    } else {
      confirmationText = "Open Anyway"
      enabled = true
    }
  }
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .padding(start = 8.dp, end = 8.dp)
      .animateContentSize()
  ) {
    Text(
      text = appInfo.app.appTitle,
      style = MaterialTheme.typography.headlineSmall,
      fontFamily = archivoBlackFamily,
      modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
    )
    AppModalActionBar(appInfo = appInfo, enabled = enabled, onChangeTimer = onChangeTimer, onEvent = onEvent)
    Surface(
      modifier = Modifier
        .height(220.dp)
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      color = MaterialTheme.colorScheme.background,
      shape = MaterialTheme.shapes.large,
      tonalElevation = 8.dp
    ) {
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)
        .navigationBarsPadding(),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.Bottom
    ) {
      TextButton(onClick = onConfirmation, enabled = enabled) {
        Box(contentAlignment = Alignment.Center) {
          // Transparent copy for alignment consistency
          Text(
            text = "Open Anyway",
            color = Color.Transparent,
            fontFamily = archivoFamily,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = confirmationText,
            fontFamily = archivoFamily,
            fontWeight = FontWeight.Bold
          )
        }
      }
      Button(
        modifier = Modifier.scale(scale),
        onClick = onCancel
      ) {
        Text(
          text = "Put the phone down",
          fontFamily = archivoFamily,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}
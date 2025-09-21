package com.android.packetsniffer.ui.animations

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

fun navEnterTransition(): EnterTransition =
    scaleIn(
        animationSpec = tween(300),
        initialScale = 0.95f
    ) + fadeIn(
        animationSpec = tween(300)
    )


fun navExitTransition(): ExitTransition =
    scaleOut(
        animationSpec = tween(300),
        targetScale = 0.95f
    ) + fadeOut(
        animationSpec = tween(300)
    )
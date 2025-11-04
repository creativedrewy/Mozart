package com.creativedrewy.mozartexamples.wallpapers

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.creativedrewy.mozart.MozartWallpaperService
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.PartySystem
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class ConfettiWallpaperService: MozartWallpaperService() {

    fun makeAParty(xPos: Float, yPos: Float): Party {
        return Party(
            speed = 0f,
            maxSpeed = 20f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
            position = Position.Absolute(xPos, yPos)
        )
    }

    override val wallpaperContents: @Composable ((OffsetValues) -> Unit)
        get() = { offsetValues ->
            var tapOffset by remember { mutableStateOf<List<Party>>(listOf()) }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            tapOffset = listOf(makeAParty(offset.x, offset.y))
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tap the screen!"
                )

                if (!tapOffset.isEmpty()) {
                    KonfettiView(
                        modifier = Modifier
                            .fillMaxSize(),
                        parties = tapOffset,
                        updateListener =
                            object : OnParticleSystemUpdateListener {
                                override fun onParticleSystemEnded(
                                    system: PartySystem,
                                    activeSystems: Int,
                                ) {
                                    if (activeSystems == 0) tapOffset = listOf()
                                }
                            },
                    )
                }

            }
        }
}
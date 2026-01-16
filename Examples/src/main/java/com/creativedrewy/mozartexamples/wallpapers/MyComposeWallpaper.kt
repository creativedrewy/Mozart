package com.creativedrewy.mozartexamples.wallpapers

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.creativedrewy.mozart.MozartWallpaperService

class MyComposeWallpaper: MozartWallpaperService() {

    override val wallpaperContents: @Composable ((OffsetValues) -> Unit)
        get() = { offsets ->
            Box(
                modifier = Modifier
            ) {
                // Your wallpaper content goes here
            }
        }
}
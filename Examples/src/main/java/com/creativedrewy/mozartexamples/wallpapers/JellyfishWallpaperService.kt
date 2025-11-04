package com.creativedrewy.mozartexamples.wallpapers

import android.util.Log
import androidx.compose.runtime.Composable
import com.creativedrewy.mozart.MozartWallpaperService
import com.creativedrewy.mozartexamples.composables.JellyfishAnimation

class JellyfishWallpaperService : MozartWallpaperService() {

    override val wallpaperContents: @Composable ((OffsetValues) -> Unit)
        get() = { offsetValues ->
            Log.v("creativedrewy", "::: You are getting offserts ${offsetValues.xPixelOffset} :::")

            JellyfishAnimation()
        }
}
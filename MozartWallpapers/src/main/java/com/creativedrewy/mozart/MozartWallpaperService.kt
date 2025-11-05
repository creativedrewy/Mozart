package com.creativedrewy.mozart

import android.app.Presentation
import android.content.Context
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.service.wallpaper.WallpaperService
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

abstract class MozartWallpaperService  : WallpaperService() {

    data class OffsetValues(
        val screenWidth: Int = 0,
        val screenHeight: Int = 0,
        val xOffset: Float = 0f,
        val yOffset: Float = 0f,
        val xOffsetStep: Float = 0f,
        val yOffsetStep: Float = 0f,
        val xPixelOffset: Int = 0,
        val yPixelOffset: Int = 0
    )

    abstract val wallpaperContents: @Composable (OffsetValues) -> Unit

    override fun onCreateEngine(): Engine {
        return ComposeEngine(this)
    }

    inner class ComposeEngine(
        private val context: Context
    ): Engine() {

        private var virtualDisplay: VirtualDisplay? = null
        private var presentation: Presentation? = null

        private val offsets = mutableStateOf(OffsetValues())

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)

            val flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY
            val density = DisplayMetrics.DENSITY_DEFAULT

            val displayManager = getSystemService(DISPLAY_SERVICE) as DisplayManager
            virtualDisplay = displayManager.createVirtualDisplay("ComposeDisp", width, height, density, holder.surface, flags)

            val customLifecycleOwner = WallpaperLifecycleOwner()
            customLifecycleOwner.performRestore(null)
            customLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            customLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
            customLifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

            val composeView = ComposeView(context).apply {
                this.setViewTreeLifecycleOwner(customLifecycleOwner)
                this.setViewTreeSavedStateRegistryOwner(customLifecycleOwner)

                setContent {
                    wallpaperContents(offsets.value.copy(
                        screenWidth = width,
                        screenHeight = height
                    ))
                }
            }

            virtualDisplay?.let { virtualDisp ->
                presentation = Presentation(context, virtualDisp.display)
                presentation?.setContentView(composeView)
                presentation?.show()
            }
        }

        override fun onOffsetsChanged(
            xOffset: Float,
            yOffset: Float,
            xOffsetStep: Float,
            yOffsetStep: Float,
            xPixelOffset: Int,
            yPixelOffset: Int
        ) {
            super.onOffsetsChanged(
                xOffset,
                yOffset,
                xOffsetStep,
                yOffsetStep,
                xPixelOffset,
                yPixelOffset
            )

            offsets.value = offsets.value.copy(
                xOffset = xOffset,
                yOffset = yOffset,
                xOffsetStep = xOffsetStep,
                yOffsetStep = yOffsetStep,
                xPixelOffset = xPixelOffset,
                yPixelOffset = yPixelOffset
            )
        }

        override fun onTouchEvent(event: MotionEvent?) {
            presentation?.dispatchTouchEvent(event!!)

            super.onTouchEvent(event)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)

            if (visible) {
                presentation?.show()
            } else {
                presentation?.dismiss()
            }
        }

        override fun onDestroy() {
            super.onDestroy()

            presentation?.dismiss()
            virtualDisplay?.release()
        }
    }
}
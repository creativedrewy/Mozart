package com.creativedrewy.mozartexamples.wallpapers

import android.content.Context
import android.view.LayoutInflater
import android.view.SurfaceView
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.viewinterop.AndroidView
import com.creativedrewy.mozart.MozartWallpaperService
import com.google.android.filament.Skybox
import com.google.android.filament.utils.KTX1Loader
import com.google.android.filament.utils.ModelViewer
import java.nio.ByteBuffer
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.delay

class FilamentWallpaperService: MozartWallpaperService() {

    override val wallpaperContents: @Composable ((OffsetValues) -> Unit)
        get() = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                var modelViewer by remember { mutableStateOf<ModelViewer?>(null) }
                var baseUnitTransform by remember { mutableStateOf<FloatArray?>(null) }
                var rotationAngleRadians by remember { mutableStateOf(0.0f) }

                fun readCompressedAsset(context: Context, assetName: String): ByteBuffer {
                    val input = context.assets.open(assetName)
                    val bytes = ByteArray(input.available())
                    input.read(bytes)

                    return ByteBuffer.wrap(bytes)
                }

                fun multiplyMat4(a: FloatArray, b: FloatArray): FloatArray {
                    val out = FloatArray(16)
                    for (i in 0 until 4) {
                        for (j in 0 until 4) {
                            var sum = 0f
                            for (k in 0 until 4) {
                                sum += a[k * 4 + i] * b[j * 4 + k]
                            }

                            out[j * 4 + i] = sum
                        }
                    }

                    return out
                }

                LaunchedEffect(modelViewer) {
                    while (true) {
                        delay(16)

                        withFrameNanos { frameTimeNanos ->
                            rotationAngleRadians += (5f * (Math.PI.toFloat() / 180f)) * (16f / 1000f)

                            modelViewer?.let { viewer ->
                                val asset = viewer.asset
                                if (asset != null) {
                                    val transformManager = viewer.engine.transformManager
                                    val assetRoot = transformManager.getInstance(asset.root)
                                    val base = baseUnitTransform ?: floatArrayOf(
                                        1f, 0f, 0f, 0f,
                                        0f, 1f, 0f, 0f,
                                        0f, 0f, 1f, 0f,
                                        0f, 0f, 0f, 1f
                                    )

                                    val rot = floatArrayOf(cos(rotationAngleRadians), 0f, -sin(rotationAngleRadians), 0f, 0f, 1f, 0f, 0f, sin(rotationAngleRadians), 0f, cos(rotationAngleRadians), 0f, 0f, 0f, 0f, 1f)
                                    val composed = multiplyMat4(base, rot)

                                    transformManager.setTransform(assetRoot, composed)
                                }
                            }

                            modelViewer?.render(frameTimeNanos)
                        }
                    }
                }

                AndroidView(
                    { context ->
                        val surfaceView = SurfaceView(context).apply {
                            layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT
                            )
                        }

                        modelViewer = ModelViewer(surfaceView)
                        modelViewer?.let { viewer ->
                            val ibl = readCompressedAsset(context, "courtyard_8k_ibl.ktx")
                            viewer.scene.indirectLight = KTX1Loader.createIndirectLight(viewer.engine, ibl).indirectLight
                            viewer.scene.indirectLight?.intensity = 30_000.0f

                            viewer.scene.skybox = Skybox.Builder()
                                .color(0.035f, 0.035f, 0.035f, 1.0f)
                                .build(viewer.engine)

                            viewer.loadModelGlb(readCompressedAsset(context, "helmet.glb"))
                            viewer.transformToUnitCube()

                            viewer.asset?.let { asset ->
                                val tm = viewer.engine.transformManager
                                val ti = tm.getInstance(asset.root)
                                val current = FloatArray(16)

                                tm.getTransform(ti, current)
                                baseUnitTransform = current
                            }
                        }

                        surfaceView
                    },
                    modifier = Modifier.pointerInteropFilter {
//                        modelViewer?.onTouchEvent(it)
                        true
                    }
                )
            }
        }
}
package com.creativedrewy.mozartexamples

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.creativedrewy.mozartexamples.wallpapers.ConfettiWallpaperService
import com.creativedrewy.mozartexamples.wallpapers.FilamentWallpaperService
import com.creativedrewy.mozartexamples.wallpapers.DependencyInjectionWallpaper
import com.creativedrewy.mozartexamples.wallpapers.JellyfishWallpaperService
import com.creativedrewy.mozartexamples.wallpapers.SimpleComposeWallpaper
import com.google.android.filament.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Utils.init()

        val simpleIntent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
        simpleIntent.putExtra(
            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            ComponentName(this, SimpleComposeWallpaper::class.java)
        )

        val jellyfishIntent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
        jellyfishIntent.putExtra(
            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            ComponentName(this, JellyfishWallpaperService::class.java)
        )

        val fireworksWallpaper = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
        fireworksWallpaper.putExtra(
            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            ComponentName(this, ConfettiWallpaperService::class.java)
        )

        val filamentWallpaper = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
        filamentWallpaper.putExtra(
            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            ComponentName(this, FilamentWallpaperService::class.java)
        )

        val hiltWallpaper = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
        hiltWallpaper.putExtra(
            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            ComponentName(this, DependencyInjectionWallpaper::class.java)
        )
0
        setContent {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
            ) { _ ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                                startActivity(simpleIntent)
                            }
                        ) {
                            Text(text = "Simple Compose")
                        }

                        Button(
                            onClick = {
                                startActivity(jellyfishIntent)
                            }
                        ) {
                            Text(text = "Jellyfish")
                        }

                        Button(
                            onClick = {
                                startActivity(fireworksWallpaper)
                            }
                        ) {
                            Text(text = "Confetti")
                        }

                        Button(
                            onClick = {
                                startActivity(filamentWallpaper)
                            }
                        ) {
                            Text(text = "Filament")
                        }

                        Button(
                            onClick = {
                                startActivity(hiltWallpaper)
                            }
                        ) {
                            Text(text = "Dependency Injection")
                        }
                    }
                }
            }
        }
    }
}
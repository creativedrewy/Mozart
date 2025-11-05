package com.creativedrewy.mozartexamples.wallpapers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.creativedrewy.mozart.MozartWallpaperService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DIViewModel @Inject constructor(
    private val  injected: SomeInjectable
): ViewModel() {

    val rotation = MutableStateFlow(45f)
    val text = injected.dependencyString

    init {
        viewModelScope.launch {
            while (true) {
                rotation.update {
                    rotation.value + 1f
                }

                delay(33)
            }
        }
    }
}

class SomeInjectable @Inject constructor() {
    val dependencyString = "Hello from dependency"
}

@AndroidEntryPoint
class DependencyInjectionWallpaper: MozartWallpaperService() {

    @Inject
    lateinit var viewModel: DIViewModel

    override val wallpaperContents: @Composable ((OffsetValues) -> Unit)
        get() = {
            val rotation by viewModel.rotation.collectAsState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = rotation
                        }
                        .size(200.dp)
                        .background(Color.Blue)
                )

                Text(
                    modifier = Modifier
                        .padding(top = 16.dp),
                    text = viewModel.text,
                    fontSize = 20.sp,
                )
            }
        }
}
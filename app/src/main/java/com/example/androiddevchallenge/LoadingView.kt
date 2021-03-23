/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.red_500

enum class WeatherError {
    NONETWORK, NOGPS, NOPERMISSION, NONE
}

@Composable
fun LoadingView(model: WeatherViewModel) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = getWeatherColor("")),

    ) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            when (model.error) {
                WeatherError.NONE ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                        Box(modifier = Modifier.padding(bottom = 100.dp).fillMaxSize(), contentAlignment = Alignment.Center) {

                            LoadingAnimation()
                        }
                    }

                WeatherError.NOPERMISSION ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                        Box(modifier = Modifier.padding(bottom = 100.dp).fillMaxSize(), contentAlignment = Alignment.Center) {

                            LoadingAnimation()
                        }
                    }

                WeatherError.NONETWORK -> Column(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(200.dp))
                    Icon(
                        Icons.TwoTone.Warning, modifier = Modifier.size(50.dp), contentDescription = "", tint = red_500
                    )
                    Text(
                        "Error while getting your data, please check your internet connection.",
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.White,

                        ),
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
                WeatherError.NOGPS -> Column(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(200.dp))
                    Icon(
                        Icons.TwoTone.Warning, modifier = Modifier.size(50.dp), contentDescription = "", tint = red_500
                    )
                    Text(
                        "There was an error getting your location, please try again.",
                        style = MaterialTheme.typography.body2.copy(
                            color = Color.White,

                        ),
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoadingAnimation() {
    var enabled by remember { mutableStateOf(false) }

    val images = listOf<Int>(R.drawable.sun, R.drawable.clouds, R.drawable.rain, R.drawable.snow)

    val currentImage = animateIntAsState(
        targetValue = if (enabled) 4 else 0,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    LaunchedEffect(key1 = "Animate") {
        enabled = true
    }

    Crossfade(targetState = currentImage.value, animationSpec = tween(1600, easing = LinearEasing)) { animImage ->
        when (animImage) {
            0 -> LoadingImage(image = images[0])

            1 -> LoadingImage(image = images[1])
            2 -> LoadingImage(image = images[2])
            3 -> LoadingImage(image = images[3])
        }
    }
}
@Composable
fun LoadingImage(image: Int) {
    var enabled by remember { mutableStateOf(false) }
    val currentSize = animateIntAsState(
        targetValue = if (enabled) 70 else 50,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800),
            repeatMode = RepeatMode.Reverse
        )
    )
    val opacity = animateFloatAsState(
        targetValue = if (enabled) 1f else 0f, animationSpec = tween(600, easing = LinearEasing)
    )

    LaunchedEffect(key1 = "Animate") {
        enabled = true
    }
    Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = image), contentDescription = "",
            modifier = Modifier.size(currentSize.value.dp),
            alpha = opacity.value
        )
    }
}

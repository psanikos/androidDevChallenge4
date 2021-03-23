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
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.blue_600
import com.example.androiddevchallenge.ui.theme.green_400
import com.example.androiddevchallenge.ui.theme.green_800
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun FirstScreen(model: WeatherViewModel) {

    val permissionGranted = model.permissionGranted.observeAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.cloud1), contentDescription = "",
                modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .padding(top = 110.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("DarkWeather", style = MaterialTheme.typography.h2.copy(color = Color.White, fontSize = 50.sp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12))
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.White.copy(alpha = 0.1f)
                            )
                        )
                    )
            ) {
                Text(
                    "Welcome to DarkWeather!\nYou can use the app with or without access to your location. To see your current location please allow access or add places from the add page. To see your saved places swipe left or right on the main screen.",
                    style = MaterialTheme.typography.body2.copy(color = Color.White), modifier = Modifier.padding(16.dp), lineHeight = 24.sp, textAlign = TextAlign.Center, color = Color.White
                )
            }
            if (permissionGranted.value!!) {
                Button(
                    onClick = {
                        model.hasRun()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = green_800, backgroundColor = green_400.copy(alpha = 0.85f)
                    ),
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp), shape = RoundedCornerShape(30)
                ) {
                    Text("Continue", style = MaterialTheme.typography.button)
                }
            } else {
                Column() {

                    Button(
                        onClick = {
                            model.askPermission()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = green_800, backgroundColor = green_400.copy(alpha = 0.85f)
                        ),
                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp), shape = RoundedCornerShape(30)
                    ) {
                        Text("Tap to give access", style = MaterialTheme.typography.button)
                    }

                    Button(
                        onClick = {
                            model.askContinueWithout()
                            GlobalScope.launch {
                                model.hasRun()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = blue_600,
                            backgroundColor = Color.Transparent
                        ),
                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Continue without", style = MaterialTheme.typography.button)
                    }
                }
            }
        }
    }
}

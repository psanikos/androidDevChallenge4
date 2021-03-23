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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
@Composable
fun MainPageView(model: WeatherViewModel) {
    LaunchedEffect(key1 = "GetData") {
        model.getCurrentLocationWeather()
    }

    var index: Int by remember { mutableStateOf(0) }
    var offset: Float by remember { mutableStateOf(0f) }
    val state = rememberScaffoldState()
    var currentPage by remember { mutableStateOf("Main") }

    val swipableModifier = Modifier.draggable(
        orientation = Orientation.Horizontal,
        state = rememberDraggableState { delta ->
            offset = delta
        },
        onDragStopped = {
            if (offset > 0) {
                if (index > 0) {
                    index --
                }
                offset = 0f
            } else if (offset < 0) {
                if (index < model.locations.count() - 1) {
                    index++
                }
                offset = 0f
            }
        }

    )

    Scaffold(
        modifier = swipableModifier.fillMaxSize(),
        scaffoldState = state,
        topBar = {
            TopAppBar(

                backgroundColor = if (currentPage == "AddNew") Color(0xFF9AABBC) else if (!model.isLoading && model.locations.isNotEmpty()) getWeatherColor(input = model.locations[index].data.currently.icon!!) else getWeatherColor(input = ""),
                elevation = 0.dp,

                modifier = Modifier.height(90.dp), contentPadding = PaddingValues(top = 30.dp, start = 8.dp, end = 8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            GlobalScope.launch {
                                state.drawerState.open()
                            }
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.menu), contentDescription = "",
                            colorFilter = ColorFilter.tint(color = Color.White),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Row(

                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (currentPage == "AddNew") {
                            Text(
                                text = "Add new location",
                                style = MaterialTheme.typography.body2.copy(color = Color.White)
                            )
                        } else {
                            if (!model.isLoading && model.locations.isNotEmpty()) {

                                if (model.locations[index].isCurrent) {
                                    Icon(
                                        Icons.Filled.LocationOn,
                                        contentDescription = "",
                                        modifier = Modifier.size(18.dp),
                                        tint = Color.White
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = model.locations[index].name,
                                    style = MaterialTheme.typography.body2.copy(color = Color.White)
                                )
                            } else {
                                Text(
                                    text = "No location",
                                    style = MaterialTheme.typography.body2.copy(color = Color.White)
                                )
                            }
                            IconButton(
                                onClick = {
                                    model.getCurrentLocationWeather()
                                }
                            ) {
                                Icon(Icons.Filled.Refresh, tint = Color.White, contentDescription = "")
                            }
                        }
                    }

                    IconButton(
                        onClick = {
                            currentPage = if (currentPage == "Main") {
                                "AddNew"
                            } else {
                                "Main"
                            }
                        }
                    ) {
                        Icon(if (currentPage == "AddNew") Icons.Filled.ArrowBack else Icons.Filled.Add, tint = Color.White, modifier = Modifier.size(18.dp), contentDescription = "")
                    }
                }
            }
        },
        drawerContent = {
            SettingsView(model = model)
        },
        drawerBackgroundColor = Color(0xFFD7E0EB),
        drawerElevation = 0.dp,
        drawerShape = RoundedCornerShape(0),
        drawerGesturesEnabled = false

    ) {
        when (model.isLoading) {
            true -> LoadingView(model = model)

            false -> Crossfade(targetState = currentPage) { screen ->
                when (screen) {

                    "Main" -> if (model.locations.count() > 0) {
                        MainWeatherCard(
                            locationData = model.locations[index].data,

                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .background(color = getWeatherColor(""))
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column() {
                                Text(
                                    "No places added yet and there is no access to your location.",
                                    style = MaterialTheme.typography.body1.copy(
                                        color = Color.White,

                                    ),
                                    modifier = Modifier.padding(20.dp),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }
                    }
                    "AddNew" -> AddPlaceView(
                        model = model,
                        resetIndex = {
                            index = 0
                        }
                    )
                }
            }
        }
    }
}

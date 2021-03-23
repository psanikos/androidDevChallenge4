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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CheckBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.green_400
import com.example.androiddevchallenge.ui.theme.red_400
import com.example.androiddevchallenge.ui.theme.red_800

@ExperimentalFoundationApi
@Composable
fun AddPlaceView(model: WeatherViewModel, resetIndex: () -> Unit) {
    var searchTerm: String by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF9AABBC),
                        Color(0xFF4A526D)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(30.dp))
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()

                    .height(40.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(12)),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = searchTerm,
                    onValueChange = {
                        searchTerm = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            model.getCoordinatesFromLocation(searchTerm)
                        }
                    ),
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .fillMaxWidth(0.9f),
                    textStyle = MaterialTheme.typography.caption,

                )
                if (searchTerm == "") {
                    Text(
                        "Search a new place",
                        style = MaterialTheme.typography.caption.copy(color = Color.DarkGray),
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            model.searchedAdresses.forEach {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 5.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12)
                        )
                        .clickable {
                            model.saveLocation(address = it)
                            searchTerm = ""
                            model.searchedAdresses.clear()
                        },
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(if (it.subLocality != null) it.subLocality else it.locality, style = MaterialTheme.typography.body2, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                    Icon(
                        Icons.TwoTone.CheckBox, contentDescription = "", tint = green_400,
                        modifier = Modifier
                            .size(25.dp)
                            .padding(end = 10.dp)
                    )
                }
            }
            LazyVerticalGrid(
                cells = GridCells.Fixed(count = 2),
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .fillMaxWidth()
            ) {

                model.myLocations.forEach {
                    item {
                        Box(
                            modifier = Modifier
                                .height(160.dp)
                                .width(120.dp)
                                .padding(10.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.White.copy(
                                                alpha = 0.4f
                                            ),
                                            Color.White.copy(alpha = 0.2f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(6)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(it.name, style = MaterialTheme.typography.body2)
                                Button(
                                    onClick = {
                                        resetIndex()
                                        model.remove(it)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = red_800, backgroundColor = red_400.copy(alpha = 0.85f)
                                    ),
                                    shape = RoundedCornerShape(topStartPercent = 0, topEndPercent = 0, bottomStartPercent = 6, bottomEndPercent = 6)
                                ) {
                                    Text("Remove", style = MaterialTheme.typography.body2)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

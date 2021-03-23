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
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.androiddevchallenge.ui.theme.MyTheme

@SuppressLint("StaticFieldLeak")
object MyApp {
    lateinit var context: Context
    lateinit var activity: Activity
    fun setAppContext(con: Context) {
        context = con
    }
    fun setAppActivity(act: Activity) {
        activity = act
    }
}

class MainActivity : ComponentActivity() {

    private val model by viewModels<WeatherViewModel>()

    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        MyApp.setAppContext(this)
        MyApp.setAppActivity(act = this)

        setContent {

            MyTheme {
                if (model.hasInit) {
                    MainPageView(model = model)
                } else {
                    FirstScreen(model = model)
                }
            }
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        println("GAVE PERMISSION")
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            model.onPermissionGranted()
            model.hasRun()
            if (model.error == WeatherError.NOPERMISSION) {
                model.error = WeatherError.NONE
            }
        }
    }
}

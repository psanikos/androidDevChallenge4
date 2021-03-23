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
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat

class LocationHelper {

    private val LOCATION_REFRESH_TIME = 1000 // 3 seconds. The Minimum Time to get location update
    private val LOCATION_REFRESH_DISTANCE = 300 // 30 meters. The Minimum Distance to be changed to get location update
    private val MY_PERMISSIONS_REQUEST_LOCATION = 100

    var myLocationListener: MyLocationListener? = null

    interface MyLocationListener {
        fun onLocationChanged(location: Location)
    }

    fun startListeningUserLocation(context: Context, myListener: MyLocationListener) {
        myLocationListener = myListener

        val mLocationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

        val mLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // your code here
                myLocationListener!!.onLocationChanged(location) // calling listener to inform that updated location is available
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
// check for permissions
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (mLocationManager.isLocationEnabled) {
                    mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        LOCATION_REFRESH_TIME.toLong(),
                        LOCATION_REFRESH_DISTANCE.toFloat(),
                        mLocationListener
                    )
                } else {
                    mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        LOCATION_REFRESH_TIME.toLong(),
                        LOCATION_REFRESH_DISTANCE.toFloat(),
                        mLocationListener
                    )
                }
            } else {
                mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_REFRESH_TIME.toLong(),
                    LOCATION_REFRESH_DISTANCE.toFloat(),
                    mLocationListener
                )
            }
        } else {
            return
        }
    }
}

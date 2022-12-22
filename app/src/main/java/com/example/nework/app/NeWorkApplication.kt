package com.example.nework.app

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NeWorkApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("MAPS_API_KEY")
    }
}
package com.franshizastr.ariadne.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.franshizastr.ariadne.di.AriadneAppComponent
import com.franshizastr.ariadne.di.DaggerAriadneAppComponent
import com.franshizastr.ariadne.services.LocationService
import com.franshizastr.core.contextInterfaces.AndroidNotificationChannelSetupInterface
import com.franshizastr.core.contextInterfaces.AndroidNotificationChannelSetupInterface.Companion.CHANNEL_DESCRIPTION
import com.franshizastr.core.contextInterfaces.AndroidNotificationChannelSetupInterface.Companion.CHANNEL_ID
import com.franshizastr.core.contextInterfaces.AndroidNotificationChannelSetupInterface.Companion.CHANNEL_NAME
import com.franshizastr.core.contextInterfaces.AndroidServiceLauncherContextInterface

class AriadneApplication :
    Application(),
    AndroidServiceLauncherContextInterface,
    AndroidNotificationChannelSetupInterface
{

    lateinit var ariadneAppComponent: AriadneAppComponent

    override fun onCreate() {
        super.onCreate()
        setupNotificationChannel()
        ariadneAppComponent = DaggerAriadneAppComponent
            .builder()
            .ariadneApplication(this)
            .build()
    }

    override fun startService() {
        val intent = Intent(
            this,
            LocationService::class.java
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun stopService() {
        val intent = Intent(
            this,
            LocationService::class.java
        )
        stopService(intent)
    }

    override fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            mChannel.description = CHANNEL_DESCRIPTION
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}

val Context.appComponent: AriadneAppComponent
    get() = when(this) {
        is AriadneApplication -> this.ariadneAppComponent
        else -> this.applicationContext.appComponent
    }

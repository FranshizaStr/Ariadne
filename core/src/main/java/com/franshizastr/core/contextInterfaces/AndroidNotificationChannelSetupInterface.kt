package com.franshizastr.core.contextInterfaces

interface AndroidNotificationChannelSetupInterface {

    fun setupNotificationChannel()

    companion object {
        const val CHANNEL_ID = "NotificationChannelId"
        const val CHANNEL_DESCRIPTION = "NotificationChannelDescription"
        const val CHANNEL_NAME = "NotificationChannel"
    }
}
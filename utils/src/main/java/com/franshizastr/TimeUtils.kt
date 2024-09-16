package com.franshizastr

import android.os.Build
import java.time.Instant

object TimeUtils {

    fun getTimeInstant(): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant.now().toEpochMilli()
        } else {
            System.currentTimeMillis()
        }
    }
}
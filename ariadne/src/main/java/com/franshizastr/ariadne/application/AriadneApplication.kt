package com.franshizastr.ariadne.application

import android.app.Application
import android.content.Context
import com.franshizastr.ariadne.di.AriadneAppComponent
import com.franshizastr.ariadne.di.DaggerAriadneAppComponent

class AriadneApplication : Application() {

    lateinit var ariadneAppComponent: AriadneAppComponent

    override fun onCreate() {
        super.onCreate()

        ariadneAppComponent = DaggerAriadneAppComponent
            .builder()
            .context(this)
            .build()
    }
}

val Context.appComponent: AriadneAppComponent
    get() = when(this) {
        is AriadneApplication -> this.ariadneAppComponent
        else -> this.applicationContext.appComponent
    }

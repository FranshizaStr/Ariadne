package com.franshizastr.ariadne.app

import android.app.Application
import com.franshizastr.ariadne.di.AriadneAppComponent
import com.franshizastr.ariadne.di.DaggerAriadneAppComponent

class AriadneApplication : Application() {

    private lateinit var ariadneAppComponent: AriadneAppComponent

    override fun onCreate() {
        super.onCreate()
        ariadneAppComponent = DaggerAriadneAppComponent.builder().build()
    }
}
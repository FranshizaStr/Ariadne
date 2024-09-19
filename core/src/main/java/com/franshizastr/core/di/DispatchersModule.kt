package com.franshizastr.core.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
class DispatchersModule {

    @Provides
    fun bindIoDispatchers(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}
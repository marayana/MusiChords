package com.jsdisco.soundscompose

import android.app.Application
import com.jsdisco.soundscompose.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SoundApp: Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@SoundApp)
            modules(appModule)
        }
    }
}

/*
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SoundApp : Application()

 */
package com.jsdisco.musichords

import android.app.Application
import com.jsdisco.musichords.di.appModule
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
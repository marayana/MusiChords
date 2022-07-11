package com.jsdisco.soundscompose.di

import android.media.SoundPool
import com.jsdisco.soundscompose.data.ChordsRepository
import com.jsdisco.soundscompose.data.SoundsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSoundsRepo() : SoundsRepository {
        return SoundsRepository()
    }

    @Provides
    @Singleton
    fun provideChordsRepo() : ChordsRepository {
        return ChordsRepository()
    }

    @Provides
    @Singleton
    fun provideSoundPool(): SoundPool {
        val builder: SoundPool.Builder = SoundPool.Builder().setMaxStreams(7)
        return builder.build()
    }
}
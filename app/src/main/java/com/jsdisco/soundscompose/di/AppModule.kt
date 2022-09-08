package com.jsdisco.soundscompose.di

import android.media.SoundPool
import androidx.room.Room
import com.jsdisco.soundscompose.data.ChordsRepository
import com.jsdisco.soundscompose.data.SoundsRepository
import com.jsdisco.soundscompose.data.local.AppDatabase
import com.jsdisco.soundscompose.presentation.chords.ChordsViewModel
import com.jsdisco.soundscompose.presentation.chordsinfo.ChordsInfoViewModel
import com.jsdisco.soundscompose.presentation.relativepitch.RelativePitchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val appModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "AppDb"
        ).build()
    }

    single {
        SoundsRepository()
    }

    single {
        ChordsRepository(get())
    }

    single {
        val builder: SoundPool.Builder = SoundPool.Builder().setMaxStreams(7)
        builder.build()
    }

    viewModel{
        RelativePitchViewModel(get(), get())
    }

    viewModel{
        ChordsViewModel(get(), get(), get())
    }

    viewModel{
        ChordsInfoViewModel(get(), get(), get())
    }
}


/*
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

 */
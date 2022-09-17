package com.jsdisco.musichords.di

import android.media.SoundPool
import androidx.room.Room
import com.jsdisco.musichords.data.ChordsRepository
import com.jsdisco.musichords.data.RelativePitchRepository
import com.jsdisco.musichords.data.SoundsRepository
import com.jsdisco.musichords.data.local.AppDatabase
import com.jsdisco.musichords.presentation.chords.ChordsViewModel
import com.jsdisco.musichords.presentation.chordsinfo.ChordsInfoViewModel
import com.jsdisco.musichords.presentation.relativepitch.RelativePitchViewModel
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
        RelativePitchRepository(get())
    }

    single {
        val builder: SoundPool.Builder = SoundPool.Builder().setMaxStreams(7)
        builder.build()
    }

    viewModel{
        RelativePitchViewModel(get(), get(), get())
    }

    viewModel{
        ChordsViewModel(get(), get(), get())
    }

    viewModel{
        ChordsInfoViewModel(get(), get(), get())
    }
}
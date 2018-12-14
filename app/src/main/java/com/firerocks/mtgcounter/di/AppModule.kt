package com.firerocks.mtgcounter.di

import android.app.Application
import android.content.Context
import com.firerocks.mtgcounter.main.MtgCounterActivity
import com.firerocks.mtgcounter.root.App
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    abstract fun provideContext(app: Application): Context
}
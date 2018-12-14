package com.firerocks.mtgcounter.root

import android.app.Application
import com.firerocks.mtgcounter.di.AppComponent
import com.firerocks.mtgcounter.di.AppModule
import com.firerocks.mtgcounter.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)

        return  appComponent
    }
}
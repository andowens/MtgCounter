package com.firerocks.mtgcounter.root

import android.app.Application

class App : Application() {
    lateinit var appComponent : AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
    }

    private fun initDagger(app: App) : AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()
}
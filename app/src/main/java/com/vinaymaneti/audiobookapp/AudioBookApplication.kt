package com.vinaymaneti.audiobookapp

import android.app.Application
import android.content.Context

class AudioBookApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: AudioBookApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = AudioBookApplication.applicationContext()
    }
}
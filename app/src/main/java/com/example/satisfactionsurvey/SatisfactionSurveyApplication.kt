package com.example.satisfactionsurvey

import android.app.Application
import com.example.satisfactionsurvey.data.AppContainer
import com.example.satisfactionsurvey.data.AppDataContainer

class SatisfactionSurveyApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
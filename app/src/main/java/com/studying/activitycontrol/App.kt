package com.studying.activitycontrol

import android.app.Application
import com.studying.activitycontrol.db.DataBase

class App: Application() {
lateinit var dataBase: DataBase
    override fun onCreate() {
        super.onCreate()
        dataBase = DataBase.getInstance(this)
    }
}
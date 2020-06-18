package com.studying.activitycontrol.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.studying.activitycontrol.SensorLogic


class MyService : Service() {

    companion object {
        val sensorLogic = SensorLogic()
    }

    override fun onCreate() {
        sensorLogic.context = this
        if (application != null) {
            sensorLogic.application = application
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sensorLogic.startSensing()
        return START_NOT_STICKY
    }


    override fun onBind(intent: Intent): IBinder? = null

}


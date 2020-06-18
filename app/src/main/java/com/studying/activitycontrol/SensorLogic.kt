package com.studying.activitycontrol


import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.studying.activitycontrol.db.ActivityEntity
import com.studying.activitycontrol.db.ActivityEntityDao
import com.studying.activitycontrol.db.DataBase
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.roundToInt

class SensorLogic : SensorEventListener {

    lateinit var sensorManager: SensorManager
    lateinit var context: Context
    var application: Application? = null
    lateinit var disposable: Disposable

    companion object {
        val activeValuesX = mutableListOf<Float>()
        val activeValuesY = mutableListOf<Float>()
        val activeValuesZ = mutableListOf<Float>()
    }

    fun startSensing() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        activeValuesX.add(event.values[0])
        activeValuesY.add(event.values[1])
        activeValuesZ.add(event.values[2])

        if (activeValuesX.size > 10) {
            sensorManager.unregisterListener(this)

            GlobalScope.launch {
                var float = 0f
                for (i in 0..activeValuesX.size.minus(1)) {
                    float += (activeValuesX[i].pow(2) + activeValuesY[i].pow(2) + activeValuesZ[i].pow(
                        2
                    )).pow(0.5f)
                }
                val activity = ((float / activeValuesX.size) * 10).roundToInt()
                addToDataBase(activity)
            }
        }

    }


    private fun addToDataBase(activity: Int) {
        activeValuesX.clear()
        activeValuesY.clear()
        activeValuesZ.clear()

        val myActivityDao: ActivityEntityDao = if (application != null) {
            (application as App).dataBase.getActivityDao()
        } else {
            DataBase.getInstance(context).getActivityDao()
        }

        disposable = myActivityDao.selectAll()
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it.size < 144) {
                    myActivityDao.insert(ActivityEntity(activity))
                    disposable.dispose()
                } else {
                    myActivityDao.delete(it.first())
                    myActivityDao.insert(ActivityEntity(activity))
                    disposable.dispose()
                }
            }, {
                it.printStackTrace()
            })
    }
}

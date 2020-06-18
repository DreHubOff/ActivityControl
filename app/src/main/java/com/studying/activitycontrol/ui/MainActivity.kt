package com.studying.activitycontrol.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.studying.activitycontrol.App
import com.studying.activitycontrol.R
import com.studying.activitycontrol.service.MyService
import io.reactivex.Flowable.just
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    private lateinit var alarmManager: AlarmManager
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val screenWidth: Int = point.x

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyService::class.java)
        val pendingIntent =
            PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            1000 * 60 * 10,
            pendingIntent
        )

        disposable = (application as App).dataBase.getActivityDao()
            .selectAll()
            .flatMap { list ->
                var result = 0
                list.forEach { result += (it.num - 97) }
                if (list.isNotEmpty()) {
                    result /= list.size
                }
                return@flatMap just(result)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MyFragment(it, screenWidth))
                    .commit()
                println(it)
            }, {
                it.printStackTrace()
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            })

    }


    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }

}

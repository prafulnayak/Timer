package com.yougov.aevum

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext


class TimerService: Service(), CoroutineScope {
    private val job: Job by lazy { Job() }
    override val coroutineContext: CoroutineContext
        get() = job +Dispatchers.Default

    private val CHANNEL_ID = "Timer Notification"
    private var isRunning: Boolean = true
    private val timerQueue:Queue<TimerData> = LinkedList<TimerData>()
    private val listTimer = mutableListOf<TimerData>()

    inner class MyBinder : Binder(){
        val service:TimerService
            get() = this@TimerService
    }

    private val binder: IBinder = MyBinder()
    val timerLiveList : MutableLiveData<List<TimerData>> = MutableLiveData()

    override fun onCreate() {
        super.onCreate()
        startNotification()
        runTimer()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    private fun setTimerData(intent: Intent?) {
        val timerData = intent?.getSerializableExtra("timer") as TimerData
        timerQueue.add(timerData)
        timerLiveList.value = timerLiveList.value?.plus(timerData) ?: listOf(timerData)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setTimerData(intent)
        return START_NOT_STICKY
    }

    private fun runTimer() {
        CoroutineScope(Dispatchers.IO).launch {
            while (isRunning){
                if(!timerQueue.isEmpty()){
                    val timerData = timerQueue.poll()
                    launch {
                        timerData?.let {
                            startTimer(it)
                        }
                    }
                }
            }
            if(!isRunning){
                stopService()
            }
        }
    }

    private fun startTimer(timerData: TimerData) {
        val handler = Handler(Looper.getMainLooper())
        handler.post(Runnable {
            object : CountDownTimer(timerData.time, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timerLiveList.value?.find { it.id == timerData.id }.apply {
                        this?.time = millisUntilFinished
                    }
                    timerLiveList.value = timerLiveList.value
                }

                override fun onFinish() {
                    timerLiveList.value?.find { it.id == timerData.id }.apply {
                        this?.time = 0
                    }
                    timerLiveList.postValue(timerLiveList.value)
                }
            }.start()
        })

    }

    private fun stopService(){
        isRunning = false
    }

    private fun startNotification() {
        val channel = NotificationChannel(CHANNEL_ID, "Timer", NotificationManager.IMPORTANCE_HIGH)
        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        val notification:Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Timer Notification")
            .setContentText("Timer Running").build()

        startForeground(111, notification)
    }
}
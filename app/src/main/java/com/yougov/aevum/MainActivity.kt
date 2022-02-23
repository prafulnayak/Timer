package com.yougov.aevum

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yougov.aevum.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var timerService: TimerService? = null
    private lateinit var timerAdapter: TimerAdapter

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
            val binder = iBinder as TimerService.MyBinder
            timerService = binder.service
            observeRunningTimerList()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            unbindTimerService()
        }

    }

    private fun unbindTimerService() {
        unbindService(serviceConnection)
    }

    private fun observeRunningTimerList() {
        timerService?.timerLiveList?.observe(this, Observer {
            timerAdapter.submitTimerList(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timerAdapter = TimerAdapter()
        binding.timerRv.layoutManager = LinearLayoutManager(this)
        binding.timerRv.adapter = timerAdapter

        val intent = Intent(this, TimerService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        setDefaultValue()
        binding.start.setOnClickListener {
            try {
                val time = (binding.hours.text.toString().toLong() * 60 * 60 * 1000) +
                        (binding.minutes.text.toString().toLong() * 60 * 1000) +
                        (binding.seconds.text.toString().toLong() * 1000)
                intent.putExtra("timer", TimerData(Random().nextInt(), time))
                startForegroundService(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Enter Right Format", Toast.LENGTH_LONG).show()
            }

            setDefaultValue()
        }
    }

    private fun setDefaultValue() {
        binding.hours.setText("00")
        binding.minutes.setText("00")
        binding.seconds.setText("00")
    }

    override fun onPause() {
        super.onPause()
        unbindService(serviceConnection)
    }


}

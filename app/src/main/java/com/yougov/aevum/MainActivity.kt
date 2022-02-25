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
    private lateinit var serviceConnection: ServiceConnection
    private var isBound = false

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
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
                iBinder?.let {
                    val binder = iBinder as TimerService.MyBinder
                    timerService = binder.service
                    isBound = true
                    observeRunningTimerList()
                }
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                isBound = false
            }

        }
        val intent = Intent(this, TimerService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        setDefaultValue()
        binding.start.setOnClickListener {

            val time = (Utility.getTimeInLong(
                binding.hours.text.toString(),
                binding.minutes.text.toString(),
                binding.seconds.text.toString()
            )) ?: 0
            if (time > 1000) {
                intent.putExtra(TIMER, TimerData(Random().nextInt(), time))
                startForegroundService(intent)
            } else {
                Toast.makeText(this, getString(R.string.wrong_input_message), Toast.LENGTH_LONG).show()
            }
            setDefaultValue()
        }
    }

    private fun setDefaultValue() {
        binding.hours.setText(getString(R.string.reset_value))
        binding.minutes.setText(getString(R.string.reset_value))
        binding.seconds.setText(getString(R.string.reset_value))
    }

    private fun unbindTimerService() {
        unbindService(serviceConnection)
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindTimerService()
            isBound = false
        }
    }

    companion object {
        const val TIMER = "timer"
    }
}

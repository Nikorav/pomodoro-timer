package ru.imto.pomodorotimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ru.imto.pomodorotimer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),PomodoroTimerListener {

    private lateinit var binding: ActivityMainBinding

    private val stopwatchAdapter = PomodoroTimerAdapter(this)
    private val stopwatches = mutableListOf<PomodoroTimer>()
    private var nextId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stopwatchAdapter
        }

        binding.addNewStopwatchButton.setOnClickListener {
            stopwatches.add(PomodoroTimer(nextId++, 0, 0 ,true))
            stopwatchAdapter.submitList(stopwatches.toList())
        }
    }
    override fun start(id: Int) {
        changeStopwatch(id, null, true)
    }

    override fun stop(id: Int, currentMs: Long) {
        changeStopwatch(id, currentMs, false)
    }

    override fun reset(id: Int) {
        changeStopwatch(id, 0L, false)
    }

    override fun delete(id: Int) {
        stopwatches.remove(stopwatches.find { it.id == id })
        stopwatchAdapter.submitList(stopwatches.toList())
    }

    private fun changeStopwatch(id: Int, currentMs: Long?, isStarted: Boolean) {
        val newTimers = mutableListOf<PomodoroTimer>()
        stopwatches.forEach {
            if (it.id == id) {
                newTimers.add(PomodoroTimer(it.id, currentMs ?: it.currentMs,it.startMs, isStarted))
            } else {
                newTimers.add(it)
            }
        }
        stopwatchAdapter.submitList(newTimers)
        stopwatches.clear()
        stopwatches.addAll(newTimers)
    }
}

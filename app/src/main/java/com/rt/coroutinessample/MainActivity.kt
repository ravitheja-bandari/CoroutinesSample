package com.rt.coroutinessample

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.rt.coroutinessample.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            binding.tvHello.text = "Hello Coroutines - CoroutineScope.Main"
        }

        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            withContext(Dispatchers.Main) {
                binding.tvHello.text = "Hello Coroutines - CoroutineScope.IO WithContext.Main"
                binding.tvHello.textSize = 25f
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            doSomething()
        }
        CoroutineScope(Dispatchers.IO).launch {

            val result2 = async {
                doSomethingNext()
            }
            val result1 = async {
                doSomethingFirst()
            }

            withContext(Dispatchers.Main) {
                binding.tvHello.text = result1.await()
                binding.tvHello.setTextColor(result2.await())
            }
        }
    }

    private suspend fun doSomething() {
        delay(3000)
        binding.tvHello.text = "Hello Coroutines - CoroutineScope.Main Suspend Function"
        binding.tvHello.setTextColor(Color.RED)
    }

    private suspend fun doSomethingFirst(): String {
        delay(6000)
        return "Hello Coroutines - CoroutineScope.Main Suspend Function Async"
    }

    private suspend fun doSomethingNext(): Int {
        delay(8000)
        return Color.BLUE
    }
}
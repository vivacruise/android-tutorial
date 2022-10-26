/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.tiptime

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

/**
 * Activity that displays a tip calculator.
 */
class MainActivity : AppCompatActivity() {

    // Binding object instance with access to the views in the activity_main.xml layout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout XML file and return a binding object instance
        //膨胀布局 XML 文件并返回绑定对象实例
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Set the content view of the Activity to be the root view of the layout
        //设置Activity的内容视图为布局的根视图
        setContentView(binding.root)

        // Setup a click listener on the calculate button to calculate the tip
        //在计算按钮上设置点击监听器来计算小费
        binding.calculateButton.setOnClickListener { calculateTip() }

        // Set up a key listener on the EditText field to listen for "enter" button presses
        //在 EditText 字段上设置一个键侦听器以侦听“输入”按钮的按下
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(
                view,
                keyCode
            )
        }
    }

    /**
     * Calculates the tip based on the user input. 根据用户输入计算小费。
     */
    private fun calculateTip() {
        // Get the decimal value from the cost of service EditText field
        //从服务成本 EditText 字段中获取十进制值
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        val cost = stringInTextField.toDoubleOrNull()

        // If the cost is null or 0, then display 0 tip and exit this function early.
        //如果 cost 为 null 或 0，则显示 0 提示并提前退出此功能。
        if (cost == null || cost == 0.0) {
            displayTip(0.0)
            return
        }

        // Get the tip percentage based on which radio button is selected
        //根据选择的单选按钮获取小费百分比
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        // Calculate the tip 计算小费
        var tip = tipPercentage * cost

        // If the switch for rounding up the tip toggled on (isChecked is true), then round up the
        // tip. Otherwise do not change the tip value.
        //如果向上舍入的开关打开（isChecked 为真），则向上舍入。 否则不要改变小费值。
        val roundUp = binding.roundUpSwitch.isChecked
        if (roundUp) {
            // Take the ceiling of the current tip, which rounds up to the next integer, and store
            // the new value in the tip variable.
            //取当前小费的上限，四舍五入到下一个整数，并将新值存储在小费变量中。
            tip = kotlin.math.ceil(tip)
        }

        // Display the formatted tip value onscreen 在屏幕上显示格式化的提示值
        displayTip(tip)
    }

    /**
     * Format the tip amount according to the local currency and display it onscreen.
     * Example would be "Tip Amount: $10.00".
     * 根据当地货币格式化小费金额并在屏幕上显示。例如“小费金额：10.00 美元”。
     */
    private fun displayTip(tip: Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }

    /**
     * Key listener for hiding the keyboard when the "Enter" button is tapped.
     * 点击“Enter”按钮时隐藏键盘的按键监听器。
     */
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}
package com.yeseul.part2.chapter02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import java.util.*

class MainActivity : AppCompatActivity() {

    private val numberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.numberPicker)
    }

    private val addButton: Button by lazy {
        findViewById<Button>(R.id.addButton)
    }

    private val clearButton: Button by lazy {
        findViewById<Button>(R.id.clearButton)
    }

    private val runButton: Button by lazy {
        findViewById<Button>(R.id.runButton)
    }

    private val numberTextViewList : List<TextView> by lazy {
        listOf<TextView>(
            findViewById<TextView>(R.id.TextView1),
            findViewById<TextView>(R.id.TextView2),
            findViewById<TextView>(R.id.TextView3),
            findViewById<TextView>(R.id.TextView4),
            findViewById<TextView>(R.id.TextView5),
            findViewById<TextView>(R.id.TextView6)
        )
    }

    private val pickNumberSet = hashSetOf<Int>()

    private var didRun = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numberPicker.minValue = 1
        numberPicker.maxValue = 45

        initAddButton()
        initClearButton()
        initRunButton()
    }

    private fun initAddButton() {
        addButton.setOnClickListener{
            if(didRun) {    // 모든 번호 선택 완료
                Toast.makeText(this, "초기화 후에 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickNumberSet.size >= 5) {  // 최대 5개 번호 선택 가능
                Toast.makeText(this, "번호는 5개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pickNumberSet.contains(numberPicker.value)) {   // 번호 중복 선택
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val textView = numberTextViewList[pickNumberSet.size]
            textView.text = numberPicker.value.toString()
            textView.isVisible = true
            setNumberBackground(numberPicker.value, textView)

            pickNumberSet.add(numberPicker.value)

        }
    }

    private fun initClearButton() {
        clearButton.setOnClickListener {
            didRun = false
            pickNumberSet.clear()
            numberTextViewList.forEach{
                it.isVisible = false
            }
        }
    }

    private fun initRunButton() {
        runButton.setOnClickListener{
            val list = getRandomNumber()    // 수동 선택 번호 포함하여 sort 후 return
            didRun = true

            list.forEachIndexed { index, number ->
                val textView = numberTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumberBackground(number, textView)
            }
        }
    }

    private fun getRandomNumber(): List<Int> {
        // shuffle() 사용 -> 강의 구현
        val numberList = mutableListOf<Int>().apply {
            for (i in 1..45) {
                if (pickNumberSet.contains(i)) {
                    continue
                }
                this.add(i)
            }
        }
        numberList.shuffle()
        val newList = pickNumberSet.toList() + numberList.subList(0, 6-pickNumberSet.size)
        return newList.sorted() // 수동 선택 번호 포함하여 sort 후 return

        // Random() 사용 -> 직접 구현
//        val random = Random()
//        val numberList = mutableListOf<Int>()
//        val size = 6 - pickNumberSet.size
//        while (numberList.size < size) {
//            val randomnumber = random.nextInt(45) + 1
//            numberList.add(randomnumber)
//        }
//
//        val newList = pickNumberSet.toList() + numberList
//        return newList.sorted()
    }

    private fun setNumberBackground(number : Int, textView : TextView) {
        when(number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }
    }
}

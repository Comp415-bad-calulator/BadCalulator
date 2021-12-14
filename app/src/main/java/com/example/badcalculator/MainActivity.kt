package com.example.badcalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.badCalculator.R
import java.util.*
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var currentDisplay = ""
        var resultDisplay = ""
        val numberButtons = getNumberButtons()
        for((index, numberButton) in numberButtons.withIndex()){
            numberButton.setOnClickListener {
                currentDisplay += index
            }
        }

        val addButton: Button = findViewById(R.id.plus)
        val subtractButton: Button = findViewById(R.id.minus)
        val multiplyButton: Button = findViewById(R.id.multiply)
        val divideButton: Button = findViewById(R.id.divide)

        addButton.setOnClickListener { currentDisplay += "+" }
        subtractButton.setOnClickListener { currentDisplay += "-" }
        multiplyButton.setOnClickListener { currentDisplay += "*" }
        divideButton.setOnClickListener { currentDisplay += "/" }

        val clearButton: Button = findViewById(R.id.clear)
        val backspaceButton: Button = findViewById(R.id.delete)
        val equalsButton: Button = findViewById(R.id.equal)

        clearButton.setOnClickListener { currentDisplay = "" }
        backspaceButton.setOnClickListener { currentDisplay = currentDisplay.dropLast(1)}
        equalsButton.setOnClickListener {
            currentDisplay = calculate(currentDisplay).toString()
            resultDisplay = currentDisplay
        }

        val expressionTextView: TextView = findViewById(R.id.expression)
        val resultTextView: TextView = findViewById(R.id.result)

        expressionTextView.text = currentDisplay
        resultTextView.text = resultDisplay

    }

    private fun getNumberButtons(): MutableList<Button>{
        val numberButtons = mutableListOf<Button>()
        numberButtons.add(findViewById(R.id.zero))
        numberButtons.add(findViewById(R.id.one))
        numberButtons.add(findViewById(R.id.two))
        numberButtons.add(findViewById(R.id.three))
        numberButtons.add(findViewById(R.id.four))
        numberButtons.add(findViewById(R.id.five))
        numberButtons.add(findViewById(R.id.six))
        numberButtons.add(findViewById(R.id.seven))
        numberButtons.add(findViewById(R.id.eight))
        numberButtons.add(findViewById(R.id.nine))
        return numberButtons
    }

    private fun calculate(input: String): Double {
        val postfix = parse(input)
        val stack = ArrayDeque<Double>()

        for (item in postfix.split(" ")) {
            val parsedChar: Double? = item.toDoubleOrNull()

            parsedChar?.let {
                stack.push(parsedChar)
            } ?: run {
                val num1 = stack.pop()
                val num2 = stack.pop()
                when (item) {
                    "+" -> stack.push(num1 + num2)
                    "-" -> stack.push(num1 - num2)
                    "/" -> stack.push(num1 / num2)
                    "*" -> stack.push(num1 * num2)
                    "^" -> stack.push(num1.pow(num2))
                    else -> throw UnsupportedOperationException()
                }
            }
        }
        return stack.pop()
    }

    private fun parse(input: String): String {

        val cleanInput = input.trim()
        val stack = ArrayDeque<Char>()
        var postfix: String = ""
        var currentChar: Char

        for (i in cleanInput.indices) {
            currentChar = cleanInput[i]

            if (currentChar.isDigit() || currentChar == '.') {
                postfix += currentChar
                continue
            } else if (postfix.isNotEmpty() && postfix.last() != ' ') {
                postfix += " "
            }

            when (currentChar) {
                '(' -> {
                    stack.push(currentChar)
                }
                ')' -> {
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        postfix += stack.pop()
                    }
                    stack.pop()
                }
                else -> {
                    while (!stack.isEmpty() && getPrecedence(currentChar) >= getPrecedence(stack.peek())) {
                        postfix += stack.pop()
                    }
                    stack.push(currentChar)
                }
            }
        }
        while (!stack.isEmpty()) {
            postfix += " "
            postfix += stack.pop()
        }
        return postfix

    }

    private fun getPrecedence(op: Char): Int {
        val result: Int = when (op) {
            '+' -> 1
            '-' -> 1
            '*' -> 2
            '/' -> 2
            else -> 3
        }
        return result
    }
}
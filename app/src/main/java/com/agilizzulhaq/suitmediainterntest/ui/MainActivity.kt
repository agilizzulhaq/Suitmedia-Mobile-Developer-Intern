package com.agilizzulhaq.suitmediainterntest.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.agilizzulhaq.suitmediainterntest.R
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }

        val nameInput = findViewById<EditText>(R.id.tie_name)
        val palindromeInput = findViewById<EditText>(R.id.tie_palindrome)
        val checkButton = findViewById<Button>(R.id.button_check)
        val nextButton = findViewById<Button>(R.id.button_next)

        checkButton.setOnClickListener {
            val palindrome = palindromeInput.text.toString()

            if (palindrome.isEmpty()) {
                palindromeInput.error = "Please write the palindrome"
                palindromeInput.requestFocus()
            } else {
                val isPalindrome = checkPalindrome(palindrome)
                val message = if (isPalindrome) "isPalindrome" else "not palindrome"
                showDialog(message)
            }
        }

        nextButton.setOnClickListener {
            val name = nameInput.text.toString()

            if (name.isEmpty()) {
                nameInput.error = "Please enter your name"
                nameInput.requestFocus()
            } else {
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("nameFromFirstActivity", name)
                startActivity(intent)
            }
        }
    }

    private fun checkPalindrome(sentence: String): Boolean {
        val cleanedSentence = sentence.replace(" ", "").lowercase(Locale.ROOT)
        return cleanedSentence == cleanedSentence.reversed()
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
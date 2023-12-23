package com.agilizzulhaq.suitmediainterntest.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.agilizzulhaq.suitmediainterntest.R

class SecondActivity : AppCompatActivity() {

    private lateinit var selectedUserNameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        setupActionBar()

        val nameFromFirstActivity = intent.getStringExtra("nameFromFirstActivity")
        Log.d("TAG", "Name from Intent: $nameFromFirstActivity")
        updateNameTextView(nameFromFirstActivity)

        selectedUserNameTextView = findViewById(R.id.selected_user_name)

        val chooseUser: Button = findViewById(R.id.button_choose_user)

        chooseUser.setOnClickListener {
            startActivityForResult(
                Intent(this, ThirdActivity::class.java),
                THIRD_ACTIVITY_REQUEST_CODE
            )
        }
    }

    private fun updateNameTextView(name: String?) {
        val tvName: TextView = findViewById(R.id.tv_name)
        tvName.text = name
    }

    private fun updateSelectedUserNameTextView(selectedUserName: String?) {
        selectedUserNameTextView.text = selectedUserName
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == THIRD_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedUserName = data?.getStringExtra("selectedUserName")
            updateSelectedUserNameTextView(selectedUserName)
        }
    }

    private fun setupActionBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_second_screen)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setStatusBarColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color
            if (ColorUtils.calculateLuminance(color) > 0.5) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            } else {
                window.decorView.systemUiVisibility = 0
            }
        }
    }

    companion object {
        const val THIRD_ACTIVITY_REQUEST_CODE = 1
    }
}
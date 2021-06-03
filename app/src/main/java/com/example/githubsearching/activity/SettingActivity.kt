package com.example.githubsearching.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import com.example.githubsearching.alarm.AlarmReceiver
import com.example.githubsearching.R
import java.util.*

class SettingActivity : AppCompatActivity(){

    private lateinit var reminderReceiver: AlarmReceiver
    private lateinit var switch: Switch
    private lateinit var btnLang: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        reminderReceiver = AlarmReceiver()

        switch = findViewById(R.id.switchAlarm)
        val  sharedPreferences = getSharedPreferences("com.example.anubhaw.socialtwitter", MODE_PRIVATE)
        val  preferences = sharedPreferences.edit()
        switch.setChecked(sharedPreferences.getBoolean("isChecked", false))
        val repeatTime = "09:00"
        val repeatMessage = "Daily Reminder buka aplikasimu"
        switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                preferences.putBoolean("isChecked", true)
                reminderReceiver.setRepeatingAlarm(
                        this, AlarmReceiver.TYPE_REPEATING,
                        repeatTime, repeatMessage
                )
                Toast.makeText(this@SettingActivity, "Alarm Reminder Aktif", Toast.LENGTH_SHORT)
                        .show()
            } else {
                preferences.putBoolean("isChecked", false)
                Toast.makeText(this@SettingActivity, "Alarm Reminder Mati", Toast.LENGTH_SHORT)
                        .show()
            }
            preferences.commit()
        })

        val actionbar = supportActionBar

        actionbar!!.title = "Setting"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
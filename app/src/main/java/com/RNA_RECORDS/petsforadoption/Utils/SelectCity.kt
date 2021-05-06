package com.RNA_RECORDS.petsforadoption.Utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.RNA_RECORDS.petsforadoption.MainActivity
import com.RNA_RECORDS.petsforadoption.R
import com.google.firebase.messaging.FirebaseMessaging

class SelectCity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_city)

        val cityButton = findViewById<Button>(R.id.cityButton)
        val cityEditText = findViewById<EditText>(R.id.cityEditText)

        cityButton.setOnClickListener {
            if(cityEditText.text.isNullOrBlank())
            {
                Toast.makeText(this,"City name cannot be empty",Toast.LENGTH_LONG).show()
            }else
            {
                FirebaseMessaging.getInstance().subscribeToTopic(cityEditText.text.toString().toLowerCase().trim())
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("city",cityEditText.text.toString().toLowerCase().trim()).apply()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

    }
}
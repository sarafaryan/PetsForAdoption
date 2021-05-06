package com.RNA_RECORDS.petsforadoption

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.RNA_RECORDS.petsforadoption.Utils.AuthorizationFrag

class StartupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

         val SPLASH_TIME_OUT:Long = 3000 // 1 sec

            Handler().postDelayed({
                // This method will be executed once the timer is over
                // Start your app main activity

                startActivity(Intent(this,AuthorizationFrag::class.java))

                // close this activity
                finish()
            }, SPLASH_TIME_OUT)
        }
    }



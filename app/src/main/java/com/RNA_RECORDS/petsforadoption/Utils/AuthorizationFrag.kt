package com.RNA_RECORDS.petsforadoption.Utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.RNA_RECORDS.petsforadoption.MainActivity
import com.RNA_RECORDS.petsforadoption.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import java.util.jar.Manifest

const val REQUEST_CODE = 1
class AuthorizationFrag : AppCompatActivity()
{


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {

                if(PreferenceManager.getDefaultSharedPreferences(this).getString("city","").isNullOrBlank())
                {
                    startActivity(Intent(this, SelectCity::class.java))
                    finish()
                }
                else
                {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }




            } else {
                Toast.makeText(this, response!!.error!!.errorCode, Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build())
// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                REQUEST_CODE)

    }


}
package com.RNA_RECORDS.petsforadoption

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class ApplicationClass: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        val posts = FirebaseDatabase.getInstance().reference.child("users")
        posts.keepSynced(true)

    }
}
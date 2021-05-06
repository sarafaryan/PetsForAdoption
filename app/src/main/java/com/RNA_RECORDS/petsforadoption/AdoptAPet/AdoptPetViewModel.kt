package com.RNA_RECORDS.petsforadoption.AdoptAPet

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.RNA_RECORDS.petsforadoption.Utils.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdoptPetViewModel :ViewModel()
{
    var reference = FirebaseDatabase.getInstance().reference.child("users")
    val postList = mutableListOf<Post>()
    val postListLiveData = MutableLiveData<List<Post>>()

    init{
        reference.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Log.e("ERROR",p0.message)

                    }
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        postList.clear()
                        for (snapshot in dataSnapshot.children) {
                            val value: Post? = snapshot.getValue(Post::class.java)
                            if (value != null) {
                                postList.add(value)
                            }
                        }
                        postListLiveData.value = postList
                    }
                }) }


}
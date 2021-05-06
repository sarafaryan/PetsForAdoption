package com.RNA_RECORDS.petsforadoption.Options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.RNA_RECORDS.petsforadoption.Adapter.Adapter
import com.RNA_RECORDS.petsforadoption.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OptionsFragment :Fragment()
{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var reference:DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
        val user:FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val view:View = inflater.inflate(R.layout.activity_option,container,false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = Adapter(requireContext(),reference,user)
        val layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        return view
    }

}
package com.RNA_RECORDS.petsforadoption.GiveForAdoption

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.RNA_RECORDS.petsforadoption.R
import com.RNA_RECORDS.petsforadoption.Utils.Post
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FormForAdoptionFrag :Fragment()
{
    var reference = FirebaseDatabase.getInstance().reference.child("users")
    var currentUser = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val bundle = FormForAdoptionFragArgs.fromBundle(requireArguments())
        val view:View = inflater.inflate(R.layout.form_for_adoption,container,false)
        val button = view.findViewById<Button>(R.id.button)
        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)
        val phoneEditText = view.findViewById<EditText>(R.id.phoneEditText)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val typeEditText = view.findViewById<EditText>(R.id.typeEditText)
        val breedEditText = view.findViewById<EditText>(R.id.breedEditText)
        val ageEditText = view.findViewById<EditText>(R.id.ageEditText)
        val detailsEditText = view.findViewById<EditText>(R.id.detailsEditText)

        button.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val type = typeEditText.text.toString()
            val breed = breedEditText.text.toString()
            val age = ageEditText.text.toString()
            val details = detailsEditText.text.toString()
            val city = PreferenceManager.getDefaultSharedPreferences(context).getString("city","notEntered")

            val post:Post = Post(name,phone,email,details,breed,type,bundle.coordinates.latitude
                                                            ,bundle.coordinates.longitude,age, city!!)
             postAPost(post)
        }

        return view
    }

    private fun postAPost(post: Post) {

        reference.child(currentUser!!.uid).setValue(post)
            if(activity !=null && isAdded ) {
                val snackbar = Snackbar.make(requireActivity().findViewById(android.R.id.content), "Your post is live!", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
            view?.findNavController()?.navigate(FormForAdoptionFragDirections.actionFormForAdoptionFragToOptionsFragment())

}}
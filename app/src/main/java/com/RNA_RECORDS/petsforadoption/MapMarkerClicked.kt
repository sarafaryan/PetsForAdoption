package com.RNA_RECORDS.petsforadoption

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.RNA_RECORDS.petsforadoption.Utils.Post
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.form_for_adoption.*


class MapMarkerClicked :Fragment()
{
    lateinit var mapMarkerClickedView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mapMarkerClickedView = inflater.inflate(R.layout.marker_clicked_layout,container,false)

        val bundle = arguments?.let { MapMarkerClickedArgs.fromBundle(it) }
        val postClicked: Post? = bundle?.postOfMarkerClicked

        findViewByIdandDisplayText(postClicked)

        return mapMarkerClickedView
    }
    private fun findViewByIdandDisplayText(postClicked: Post?)
    {
        val nameEditText = mapMarkerClickedView.findViewById<EditText>(R.id.nameEditText)
        val phoneEditText = mapMarkerClickedView.findViewById<EditText>(R.id.phoneEditText)
        val emailEditText = mapMarkerClickedView.findViewById<EditText>(R.id.emailEditText)
        val typeEditText = mapMarkerClickedView.findViewById<EditText>(R.id.typeEditText)
        val breedEditText = mapMarkerClickedView.findViewById<EditText>(R.id.breedEditText)
        val ageEditText = mapMarkerClickedView.findViewById<EditText>(R.id.ageEditText)
        val detailsEditText = mapMarkerClickedView.findViewById<EditText>(R.id.detailsEditText)
        val phoneTextField = mapMarkerClickedView.findViewById<TextInputLayout>(R.id.phoneTextField)
        val emailTextField = mapMarkerClickedView.findViewById<TextInputLayout>(R.id.emailTextField)
        val button = mapMarkerClickedView.findViewById<Button>(R.id.button)

        button.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_mapMarkerClicked_to_optionsFragment)
        }

        if (postClicked != null) {
            nameEditText.setText(postClicked.name)
            phoneEditText.setText(postClicked.phone)
            emailEditText.setText(postClicked.email)
            typeEditText.setText(postClicked.type)
            breedEditText.setText(postClicked.breed)
            ageEditText.setText(postClicked.age)
            detailsEditText.setText(postClicked.details)

            phoneTextField.setEndIconOnClickListener {
                val clipboard = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label",phoneEditText.text )
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context,"Copied to clipboard.",Toast.LENGTH_LONG).show()
            }
            emailTextField.setEndIconOnClickListener{
                val clipboard = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label",emailEditText.text )
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context,"Copied to clipboard.",Toast.LENGTH_LONG).show()
            }



        }
    }
}
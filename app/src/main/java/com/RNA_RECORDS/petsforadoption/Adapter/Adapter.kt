package com.RNA_RECORDS.petsforadoption.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.RNA_RECORDS.petsforadoption.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference

class Adapter(val context:Context,val reference:DatabaseReference,val user:FirebaseUser?) : RecyclerView.Adapter<Adapter.OptionsViewHolder>()
{
    val listOfImages = listOf<Int>(R.drawable.dog_pic_1,R.drawable.dog_pic_2,R.drawable.dog_pic3)
    val listOfTitles = listOf<String>("Adopt a Pet","Give a Pet for Adoption","Remove a Previous Post")
    val listOfDesc = listOf<String>("Adopt a cute, lovable pet that is available around your location.","Do you have or know about a pet that can be given for adoption to a caring owner? Put up a post now.",
                                        "Have a previously active post of a pet who has found an owner? Remove that post.")

    class OptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val titleTextView = itemView.findViewById<TextView>(R.id.titleTextView)
        val descTextView = itemView.findViewById<TextView>(R.id.descTextView)
        val card = itemView.findViewById<View>(R.id.card)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        val layoutInflater =  LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.recyclerview_itemholder,parent,false)
        return OptionsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        holder.imageView.setImageResource(listOfImages[position])
        holder.titleTextView.text = listOfTitles[position]
        holder.descTextView.text = listOfDesc[position]

        holder.card.setOnClickListener {
         if(position==0)
         {
             it.findNavController().navigate(R.id.action_optionsFragment_to_adoptAPetFragment)
         }
         else if(position==1)
         {
             it.findNavController().navigate(R.id.action_optionsFragment_to_chooseLocation)

         }
         else if(position==2)
            {
                showDialog()
            }
        }
    }
    fun showDialog()
    {
        MaterialAlertDialogBuilder(context)
                .setTitle("Are you sure?")
                .setMessage("This action cannot be undone")
                .setNeutralButton("Cancel") { dialog, which ->
                    // Respond to neutral button press
                }
                .setNegativeButton("Decline") { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton("Accept") { dialog, which ->
                    if (user != null) {
                        reference.child(user.uid).removeValue()
                        makeDeleteSuccessfulToast()
                    }
                }
                .show()
    }
    private fun makeDeleteSuccessfulToast()
    {
        Toast.makeText(context,"Any existing post has been successfully deleted",Toast.LENGTH_LONG).show()
    }
}
package com.example.exercises.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exercises.R
import com.example.exercises.models.PhoneBookUser
import java.util.*
import kotlin.collections.ArrayList

class UserDbAdapter (
     var phoneBook: ArrayList<PhoneBookUser>,
     var onEditClick: (PhoneBookUser) -> Unit,
     var onDeleteClick: (PhoneBookUser) -> Unit,
     var onImageClick: (PhoneBookUser) -> Unit
): RecyclerView.Adapter<UserAdapter.MyViewHolder>(){

    private lateinit var itemView: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.MyViewHolder {
        itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserAdapter.MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: UserAdapter.MyViewHolder, position: Int) {
        if (position == 0
            || phoneBook[position].firstName[0].lowercaseChar()
            != phoneBook[position - 1].firstName[0].lowercaseChar()){
            holder.letter!!.visibility = View.VISIBLE
            holder.letter!!.text = phoneBook[position].firstName[0].toString()
                .uppercase(Locale.getDefault())
        }else{
            holder.letter!!.visibility = View.GONE
        }
        holder.firstName!!.text = phoneBook[position].firstName
        holder.lastName!!.text = phoneBook[position].lastName
        holder.phone!!.text = phoneBook[position].phone

        holder.editBtn!!.setOnClickListener {
            onEditClick(phoneBook[position])
        }
        holder.deleteBtn!!.setOnClickListener {
            onDeleteClick(phoneBook[position])
        }

        holder.userImage!!.setOnClickListener {
            onImageClick(phoneBook[position])
        }

            Glide.with(itemView)
                .load(phoneBook[position].image)
                .centerCrop()
                .placeholder(R.drawable.ic_person_24)
                .into(holder.userImage!!)
            Log.e("ups", phoneBook[position].image  + phoneBook[position].firstName)
    }

    override fun getItemCount() = phoneBook.size

}
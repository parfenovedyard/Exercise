package com.example.exercises.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.exercises.R
import com.example.exercises.models.PhoneBookUser

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
        if (position == 0 || phoneBook[position].firstName[0] != phoneBook[position - 1].firstName[0]){
            holder.letter!!.visibility = View.VISIBLE
            holder.letter!!.text = phoneBook[position].firstName[0].toString()
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

        if (phoneBook[position].image.isNotEmpty()) {
            Glide.with(itemView)
                .load(phoneBook[position].image)
                .centerCrop()
                .placeholder(R.drawable.ic_person_24)
                .into(holder.userImage!!)
        }
    }

    override fun getItemCount() = phoneBook.size

}
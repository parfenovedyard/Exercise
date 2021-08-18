package com.example.exercises.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exercises.models.PhoneBookUser
import com.example.exercises.R
import com.example.exercises.databinding.ItemUserBinding

class UserAdapter( private val phoneBook: ArrayList<PhoneBookUser>): RecyclerView.Adapter<UserAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val itemView = LayoutInflater.from(parent.context)
           .inflate(R.layout.item_user, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position == 0 || phoneBook[position].firstName[0] != phoneBook[position - 1].firstName[0]){
            holder.letter!!.visibility = View.VISIBLE
            holder.letter!!.text = phoneBook[position].firstName[0].toString()
        }else{
            holder.letter!!.visibility = View.GONE
        }
        holder.firstName!!.text = phoneBook[position].firstName
        holder.lastName!!.text = phoneBook[position].lastName
        holder.phone!!.text = phoneBook[position].phone
    }

    override fun getItemCount() = phoneBook.size


    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){

        val binding = ItemUserBinding.bind(view)

        var firstName: TextView? = null
        var lastName: TextView? = null
        var phone: TextView? = null
        var letter: TextView? = null
        var editBtn: ImageView? = null
        var deleteBtn: ImageView? = null

        init {
            firstName = binding.tvFirstName
            lastName = binding.tvLastName
            phone = binding.tvPhoneNumber
            letter = binding.tvLetter
            editBtn = binding.ivEdit
            deleteBtn = binding.ivDelete
        }
    }
}
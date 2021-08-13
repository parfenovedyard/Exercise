package com.example.exercises.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exercises.models.PhoneBookUser
import com.example.exercises.R

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
        var firstName: TextView? = null
        var lastName: TextView? = null
        var phone: TextView? = null
        var letter: TextView? = null

        init {
            firstName = view.findViewById(R.id.tv_firstName)
            lastName = view.findViewById(R.id.tv_lastName)
            phone = view.findViewById(R.id.tv_phoneNumber)
            letter = view.findViewById(R.id.tv_letter)

        }
    }
}
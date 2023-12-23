package com.agilizzulhaq.suitmediainterntest.data.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agilizzulhaq.suitmediainterntest.R
import com.squareup.picasso.Picasso

class UserAdapter(
    private val userList: MutableList<User>,
    private val itemClickListener: (User) -> Unit
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = userList.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        private val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val user = userList[position]
                    itemClickListener.invoke(user)
                }
            }
        }

        fun bind(user: User) {
            Picasso.get().load(user.avatar).into(avatarImageView)
            nameTextView.text = "${user.first_name} ${user.last_name}"
            emailTextView.text = user.email
        }
    }

    fun addUsers(users: List<User>) {
        userList.addAll(users)
        notifyDataSetChanged()
    }

    fun clearUsers() {
        userList.clear()
        notifyDataSetChanged()
    }
}
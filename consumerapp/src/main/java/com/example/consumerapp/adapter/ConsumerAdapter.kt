package com.example.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp.R
import com.example.consumerapp.database.entity.Favorite
import de.hdodenhof.circleimageview.CircleImageView

class ConsumerAdapter (private val listUser : List<Favorite> ): RecyclerView.Adapter<ConsumerAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size


    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userName = itemView.findViewById<TextView>(R.id.name_profile_consumer)
        private val typeUser = itemView.findViewById<TextView>(R.id.type_profile_consumer)
        private val avatar = itemView.findViewById<CircleImageView>(R.id.profile_image_consumer)


        internal fun bind(fav: Favorite) {
            Glide.with(itemView.context)
                .load(fav.avatar)
                .apply(RequestOptions().override(55,55))
                .into(avatar)

            userName.text = fav.login
            typeUser.text = fav.type

        }

    }

}
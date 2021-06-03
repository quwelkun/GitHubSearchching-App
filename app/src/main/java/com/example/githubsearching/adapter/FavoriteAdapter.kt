package com.example.githubsearching.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubsearching.R
import com.example.githubsearching.database.Favorite
import com.example.githubsearching.databinding.ListItemBinding

class FavoriteAdapter internal constructor(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val listFav = ArrayList<Favorite>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setListFav(listFav: List<Favorite>) {
        this.listFav.clear()
        this.listFav.addAll(listFav)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFav[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listFav[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return listFav.size
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemBinding.bind(itemView)
        fun bind(userSource: Favorite) {
            with(itemView){
                binding.nameProfile.text = userSource.login
                binding.usernameProfile.text = userSource.type
                Glide.with(context)
                        .load(userSource.avatar)
                        .apply(RequestOptions().override(350,550))
                        .into(binding.profileImage)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Favorite)
    }
}
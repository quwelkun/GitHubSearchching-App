package com.example.githubsearching.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubsearching.R
import com.example.githubsearching.UserSource
import com.example.githubsearching.databinding.ListItemBinding

class ListViewAdapter: RecyclerView.Adapter<ListViewAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private val mData = ArrayList<UserSource>()

    fun setData(items: ArrayList<UserSource>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemBinding.bind(itemView)
        fun bind(userSource: UserSource) {
            with(itemView){
                binding.nameProfile.text = userSource.name
                binding.usernameProfile.text = userSource.type
                Glide.with(context)
                        .load(userSource.avatar)
                        .apply(RequestOptions().override(350,550))
                        .into(binding.profileImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(mData[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserSource)
    }
}
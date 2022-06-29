package com.rezaharisz.competencytest.pages.guest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rezaharisz.competencytest.data.local.model.UserListEntity
import com.rezaharisz.competencytest.databinding.ItemGuestBinding

class GuestAdapter: PagedListAdapter<UserListEntity, GuestAdapter.GuestViewHolder>(DIFF_CALLBACK) {

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserListEntity>(){
            override fun areItemsTheSame(oldItem: UserListEntity, newItem: UserListEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: UserListEntity, newItem: UserListEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val view = ItemGuestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null){
            holder.bind(user)
        }
    }

    class GuestViewHolder(private val binding: ItemGuestBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(userListEntity: UserListEntity){
            Glide.with(itemView)
                .load(userListEntity.avatar)
                .override(90, 90)
                .into(binding.ivUser)
            binding.tvName.text = StringBuilder("${userListEntity.firstName} ${userListEntity.lastName}")
        }
    }

}
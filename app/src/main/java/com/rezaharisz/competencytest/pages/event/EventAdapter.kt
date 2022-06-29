package com.rezaharisz.competencytest.pages.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rezaharisz.competencytest.R
import com.rezaharisz.competencytest.databinding.ItemEventBinding
import com.rezaharisz.competencytest.helper.EventClickListener
import com.rezaharisz.competencytest.utils.Event

class EventAdapter(private val clickListener: EventClickListener) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private var listEvent = ArrayList<Event>()

    fun set(data: List<Event>) {
        listEvent.clear()
        listEvent.addAll(data)
        notifyDataSetChanged()
    }

    fun clear(){
        listEvent.clear()
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemEventBinding.bind(itemView)

        fun bind(data: Event, clickListener: (data: Event) -> Unit) {
            binding.tvTitle.text = data.title
            binding.tvDescription.text = data.description
            binding.tvDate.text = data.date
            binding.tvTime.text = data.time
            itemView.setOnClickListener { clickListener(data) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(listEvent[position], clickListener.clickListener)
    }

    override fun getItemCount(): Int {
        return listEvent.size
    }
}
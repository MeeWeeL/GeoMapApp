package com.meeweel.mapgeoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meeweel.mapgeoapp.databinding.MarkRecyclerItemBinding
import com.yandex.mapkit.map.PlacemarkMapObject

class MarksRecyclerAdapter(private val collection: MutableList<PlacemarkMapObject>) :
    RecyclerView.Adapter<MarksRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarksRecyclerAdapter.ViewHolder {
        val binding = MarkRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(collection[position], position)
    }

    override fun getItemCount(): Int {
        return collection.size
    }

    inner class ViewHolder(private val bind: MarkRecyclerItemBinding) : RecyclerView.ViewHolder(bind.root) {
        fun bind(mark: PlacemarkMapObject, position: Int) {
            bind.p1.text = mark.geometry.latitude.toString()
            bind.p2.text = mark.geometry.longitude.toString()
            bind.btn.setOnClickListener {
                collection.remove(mark)
                notifyItemRemoved(position)
            }
        }
    }
}
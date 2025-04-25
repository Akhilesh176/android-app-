package com.example.trackwheel.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trackwheel.databinding.ItemMaintenanceBinding
import com.example.trackwheel.db.entities.MaintenanceTask
import java.text.SimpleDateFormat
import java.util.Locale

class MaintenanceAdapter : ListAdapter<MaintenanceTask, MaintenanceAdapter.MaintenanceViewHolder>(MaintenanceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaintenanceViewHolder {
        val binding = ItemMaintenanceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MaintenanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaintenanceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MaintenanceViewHolder(private val binding: ItemMaintenanceBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(task: MaintenanceTask) {
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            
            binding.maintenanceDescription.text = task.description
            binding.maintenanceDueDate.text = "Due: ${dateFormat.format(task.dueDate)}"
            binding.maintenanceNotes.text = task.notes ?: ""
        }
    }

    class MaintenanceDiffCallback : DiffUtil.ItemCallback<MaintenanceTask>() {
        override fun areItemsTheSame(oldItem: MaintenanceTask, newItem: MaintenanceTask): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MaintenanceTask, newItem: MaintenanceTask): Boolean {
            return oldItem == newItem
        }
    }
}

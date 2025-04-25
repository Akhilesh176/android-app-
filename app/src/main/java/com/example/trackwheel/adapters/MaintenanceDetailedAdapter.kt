package com.example.trackwheel.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trackwheel.databinding.ItemMaintenanceDetailedBinding
import com.example.trackwheel.db.entities.MaintenanceTask
import java.text.SimpleDateFormat
import java.util.Locale

class MaintenanceDetailedAdapter(
    private val onDeleteClick: (MaintenanceTask) -> Unit,
    private val onCompletedChange: (MaintenanceTask, Boolean) -> Unit
) : ListAdapter<MaintenanceTask, MaintenanceDetailedAdapter.MaintenanceViewHolder>(MaintenanceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaintenanceViewHolder {
        val binding = ItemMaintenanceDetailedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MaintenanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaintenanceViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClick, onCompletedChange)
    }

    class MaintenanceViewHolder(private val binding: ItemMaintenanceDetailedBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(
            task: MaintenanceTask, 
            onDeleteClick: (MaintenanceTask) -> Unit,
            onCompletedChange: (MaintenanceTask, Boolean) -> Unit
        ) {
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            
            binding.maintenanceDescription.text = task.description
            binding.maintenanceDueDate.text = "Due: ${dateFormat.format(task.dueDate)}"
            binding.maintenanceNotes.text = task.notes ?: ""
            binding.completedCheckbox.isChecked = task.isCompleted
            
            binding.completedCheckbox.setOnCheckedChangeListener { _, isChecked ->
                onCompletedChange(task, isChecked)
            }
            
            binding.deleteButton.setOnClickListener {
                onDeleteClick(task)
            }
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

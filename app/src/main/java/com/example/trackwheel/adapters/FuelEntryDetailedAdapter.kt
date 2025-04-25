package com.example.trackwheel.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trackwheel.databinding.ItemFuelEntryDetailedBinding
import com.example.trackwheel.db.entities.FuelEntry
import java.text.SimpleDateFormat
import java.util.Locale

class FuelEntryDetailedAdapter(
    private val onDeleteClick: (FuelEntry) -> Unit
) : ListAdapter<FuelEntry, FuelEntryDetailedAdapter.FuelEntryViewHolder>(FuelEntryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuelEntryViewHolder {
        val binding = ItemFuelEntryDetailedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FuelEntryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FuelEntryViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClick)
    }

    class FuelEntryViewHolder(private val binding: ItemFuelEntryDetailedBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(fuelEntry: FuelEntry, onDeleteClick: (FuelEntry) -> Unit) {
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            
            binding.fuelDate.text = dateFormat.format(fuelEntry.date)
            binding.fuelAmount.text = String.format("%.2f L", fuelEntry.amount)
            binding.fuelPrice.text = String.format("$%.2f", fuelEntry.price)
            binding.odometerReading.text = String.format("%,d km", fuelEntry.odometer)
            
            binding.deleteButton.setOnClickListener {
                onDeleteClick(fuelEntry)
            }
        }
    }

    class FuelEntryDiffCallback : DiffUtil.ItemCallback<FuelEntry>() {
        override fun areItemsTheSame(oldItem: FuelEntry, newItem: FuelEntry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FuelEntry, newItem: FuelEntry): Boolean {
            return oldItem == newItem
        }
    }
}

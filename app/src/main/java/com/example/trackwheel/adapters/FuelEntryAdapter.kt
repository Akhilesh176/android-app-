package com.example.trackwheel.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trackwheel.databinding.ItemFuelEntryBinding
import com.example.trackwheel.db.entities.FuelEntry
import java.text.SimpleDateFormat
import java.util.Locale

class FuelEntryAdapter : ListAdapter<FuelEntry, FuelEntryAdapter.FuelEntryViewHolder>(FuelEntryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuelEntryViewHolder {
        val binding = ItemFuelEntryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FuelEntryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FuelEntryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FuelEntryViewHolder(private val binding: ItemFuelEntryBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(fuelEntry: FuelEntry) {
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            
            binding.fuelDate.text = dateFormat.format(fuelEntry.date)
            binding.fuelAmount.text = String.format("%.2f L", fuelEntry.amount)
            binding.fuelPrice.text = String.format("$%.2f", fuelEntry.price)
            binding.odometerReading.text = String.format("%,d km", fuelEntry.odometer)
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

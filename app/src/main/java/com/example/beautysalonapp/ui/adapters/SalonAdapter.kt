package com.example.beautysalonapp.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beautysalonapp.R
import com.example.beautysalonapp.ui.Salon
import com.example.beautysalonapp.ui.Service
import com.example.beautysalonapp.ui.salon.SalonDetailsActivity
import com.example.beautysalonapp.ui.service.ServiceDetailsActivity
import com.example.beautysalonapp.ui.viewHolders.SalonViewHolder
import com.example.beautysalonapp.databinding.ItemSalonsBinding
import com.example.beautysalonapp.databinding.ItemServiceBinding
import com.example.beautysalonapp.ui.viewHolders.ServiceViewHolder
import com.google.gson.Gson

class SalonAdapter(val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val binding =
                ItemSalonsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return SalonViewHolder(binding)
        }else{
            val binding =
                ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ServiceViewHolder(binding)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        if (items.get(position) is Salon) return 0
        if (items.get(position) is Service) return 1
        return 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is SalonViewHolder) {
            val Salon = items.get(position) as Salon
            holder.binding.title.text = Salon.title
            holder.binding.desc.text = Salon.description
            holder.binding.price.text = "RS: " + Salon.price
            holder.binding.rating.text = String.format("⭐ %.1f", Salon.rating)

            Glide.with(holder.itemView.context)
                .load(Salon.image)
                .error(R.drawable.logo)
                .placeholder(R.drawable.logo)
                .into(holder.binding.productImage)

            holder.itemView.setOnClickListener {
                holder.itemView.context.startActivity(
                    Intent(
                        holder.itemView.context,
                        SalonDetailsActivity::class.java
                    ).putExtra("data", Gson().toJson(Salon))
                )
            }


        }

        else if (holder is ServiceViewHolder) {
            val service = items.get(position) as Service
            holder.binding.productTitle.text = service.item?.title
            holder.binding.productPrice.text = "$${service.item?.price!!*service.rooms}"
            holder.binding.productQuantity.text = "Quantity: ${service.rooms}"

            holder.binding.status.text =service.status

            holder.itemView.setOnClickListener {
                holder.itemView.context.startActivity(
                    Intent(
                        holder.itemView.context,
                        ServiceDetailsActivity::class.java
                    ).putExtra("data", Gson().toJson(service))
                )
            }


        }

    }
}
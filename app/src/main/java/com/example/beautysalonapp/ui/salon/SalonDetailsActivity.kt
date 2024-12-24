package com.example.beautysalonapp.ui.salon

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.beautysalonapp.databinding.ActivitySalonDetailsBinding
import com.example.beautysalonapp.ui.Salon
import com.example.beautysalonapp.ui.service.ServiceBookingActivity
import com.google.gson.Gson

class SalonDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySalonDetailsBinding;
    lateinit var salon:Salon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySalonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        salon = Gson().fromJson(intent.getStringExtra("data"),Salon::class.java)

        binding.title.text = salon.title
        binding.description.text = salon.description
        binding.price.text = "${salon.price} Rs."
        binding.ratingBar.text = String.format("⭐ %.1f", salon.rating)

        binding.orderButton.setOnClickListener {
            startActivity(Intent(this,ServiceBookingActivity::class.java).putExtra("data", Gson().toJson(salon)))
            finish()
        }
    }
}
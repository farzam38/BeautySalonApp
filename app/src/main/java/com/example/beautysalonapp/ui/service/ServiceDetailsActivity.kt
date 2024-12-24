package com.example.beautysalonapp.ui.service

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.beautysalonapp.databinding.ActivityServicesDetailsBinding
import com.example.beautysalonapp.model.repositories.AuthRepository
import com.example.beautysalonapp.model.repositories.NotificationsRepository
import com.example.beautysalonapp.ui.Service
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ServiceDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityServicesDetailsBinding;
    lateinit var service: Service
    lateinit var viewModel: ServiceDetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServicesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ServiceDetailsViewModel()
        service = Gson().fromJson(intent.getStringExtra("data"), Service::class.java)

        binding.orderId.text = service.id
        binding.orderDate.text = service.orderDate
        binding.specialRequirements.text = service.specialRequirements
        binding.userName.text = service.userName
        binding.userEmail.text = service.userEmail
        binding.userContact.text = service.userContact
        binding.status.text = service.status

        val user: FirebaseUser = AuthRepository().getCurrentUser()!!
        var isAdmin = false
        if (user.email.equals("kk2072863@gmail.com"))
            isAdmin = true

        if (!service.status.equals("Service Booked") || !isAdmin)
            binding.confirmOrder.visibility = View.GONE


        binding.confirmOrder.setOnClickListener {
            service.status = "Service Booking Confirmed"
            viewModel.updateService(service)
        }

        lifecycleScope.launch {
            viewModel.isUpdated.collect {
                it?.let {
                    if (service.status.equals("Service Booking Confirmed")) {
                        NotificationsRepository().sendNotification(service.userId, "Service Booking Confirmed", "Your order of ${service.item?.title} has been confirmed", this@ServiceDetailsActivity)
                    }
                    Toast.makeText(this@ServiceDetailsActivity, "Updated", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.failureMessage.collect {
                it?.let {
                    Toast.makeText(this@ServiceDetailsActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
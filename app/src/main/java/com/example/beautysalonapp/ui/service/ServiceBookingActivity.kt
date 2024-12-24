package com.example.beautysalonapp.ui.service

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.beautysalonapp.model.repositories.NotificationsRepository
import com.example.beautysalonapp.ui.Salon
import com.example.beautysalonapp.ui.Service
import com.google.gson.Gson
import kotlinx.coroutines.launch
import com.example.beautysalonapp.ui.main.MainActivity
import com.example.beautysalonapp.databinding.ActivityBookServiceBinding
import java.text.SimpleDateFormat

class ServiceBookingActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookServiceBinding;
    lateinit var salon:Salon
    lateinit var viewModel: ServiceBookingViewModel
    lateinit var progressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityBookServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel=ServiceBookingViewModel()
        salon = Gson().fromJson(intent.getStringExtra("data"),Salon::class.java)

        binding.title.text = salon.title
        binding.price.text = "${salon.price} Rs."

        progressDialog=ProgressDialog(this)
        progressDialog.setMessage("Saving your booking...")
        progressDialog.setCancelable(false)

        lifecycleScope.launch {
            viewModel.isSaving.collect{
                if (it==true)
                    progressDialog.show()
                else
                    progressDialog.dismiss()
            }
        }
        lifecycleScope.launch {
            viewModel.failureMessage.collect{
                if (it!=null)
                    Toast.makeText(this@ServiceBookingActivity,it,Toast.LENGTH_LONG).show()
            }
        }
        lifecycleScope.launch {
            viewModel.isSaved.collect{
                if (it==true) {
                    Toast.makeText(
                        this@ServiceBookingActivity,
                        "Booking saved successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    NotificationsRepository().sendNotification(MainActivity.adminUid, "New Booking", "A new booking of ${salon.title} has been placed.", this@ServiceBookingActivity)
                    finish()
                }
            }
        }

        binding.bookRoomButton.setOnClickListener {
            val rooms = binding.quantity.text.toString().toIntOrNull()
            val specialRequirements = binding.specialRequirements.text.toString()
            val userContact = binding.userContact.text.toString()

            if (rooms == null  || userContact.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val service=Service()
            service.item=salon
            service.status="Service Booked"
            service.rooms=rooms
            service.specialRequirements=specialRequirements
            service.userContact=userContact
            service.orderDate=SimpleDateFormat("yyyy-MM-dd HH:mm a").format(System.currentTimeMillis())
            val user=viewModel.getCurrentUser()
            service.userEmail=user?.email!!
            service.userName=user?.displayName!!
            service.userId=user?.uid!!

            viewModel.saveService(service)

            Toast.makeText(this, "Service Booked successfully!", Toast.LENGTH_SHORT).show()
        }

    }
}
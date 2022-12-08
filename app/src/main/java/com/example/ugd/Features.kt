package com.example.ugd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import com.example.ugd.databinding.ActivityFeaturesBinding

class Features : AppCompatActivity() {

    private lateinit var binding : ActivityFeaturesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_features)

        binding = ActivityFeaturesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonQr.setOnClickListener {
            val intent = Intent(this, QrCode::class.java)
            startActivity(intent)
        }

        binding.buttonToast.setOnClickListener {
            toast()
        }
    }

    private fun toast() {
        MotionToast.darkToast(this,"This is information toast!", "Succesfully Make Toast!",
            MotionToastStyle.INFO,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular)
        )
    }
}
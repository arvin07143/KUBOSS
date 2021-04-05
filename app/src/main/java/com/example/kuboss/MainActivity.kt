package com.example.kuboss

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kuboss.settings.Utils

class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val startBarcode: Button = findViewById(R.id.button)
//        startBarcode.setOnClickListener {
//            this.startActivity(Intent(this,LiveBarcodeScanningActivity::class.java))
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this)
        }
    }
}
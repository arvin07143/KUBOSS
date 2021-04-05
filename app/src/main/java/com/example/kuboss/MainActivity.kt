package com.example.kuboss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.kuboss.settings.Utils

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph)
        setupActionBarWithNavController(navController)

        setContentView(R.layout.activity_main)
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this)
        }
    }
}
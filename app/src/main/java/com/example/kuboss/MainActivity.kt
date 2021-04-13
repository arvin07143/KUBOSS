package com.example.kuboss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.example.kuboss.settings.Utils
import com.example.kuboss.warehouse.WarehouseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Utils.allPermissionsGranted(this)) {
            Utils.requestRuntimePermissions(this)
        }

        //setup nav host
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        if(savedInstanceState == null){
            navController.setGraph(R.navigation.nav_graph)
        }
        setupActionBarWithNavController(navController)

        //setup bottom nav
        val bottomNav:BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)
        val appBarConfig = AppBarConfiguration(setOf(R.id.warehouseFragment, R.id.searchItemFragment))
        setupActionBarWithNavController(navController, appBarConfig)
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}
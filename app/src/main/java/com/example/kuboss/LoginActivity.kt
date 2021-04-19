package com.example.kuboss


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class LoginActivity  : AppCompatActivity(R.layout.activity_login){
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        //setup nav host
        val navLoginFragment = supportFragmentManager.findFragmentById(R.id.nav_login_fragment) as NavHostFragment
        navController = navLoginFragment.navController
        if(savedInstanceState == null){
            navController.setGraph(R.navigation.nav_login)
        }
        setupActionBarWithNavController(navController)



    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}
package com.example.kuboss


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.kuboss.settings.Utils


import kotlinx.android.synthetic.main.fragment_login.*
private lateinit var navController: NavController
class LoginActivity  : AppCompatActivity(R.layout.activity_login){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        //setup nav host
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        if(savedInstanceState == null){
            navController.setGraph(R.navigation.nav_login)
        }
        setupActionBarWithNavController(navController)



    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}
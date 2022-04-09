package com.chairunissa.challengefour.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.chairunissa.challengefour.R
import com.chairunissa.challengefour.database.local.AppLocalData
import com.chairunissa.challengefour.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loggedInUser = AppLocalData.getUserLoggedIn(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        if (loggedInUser?.isLogin == true) {
            navController.navigate(R.id.action_global_homeFragment)
        } else {
            navController.navigate(R.id.action_global_loginFragment)
        }
    }
}
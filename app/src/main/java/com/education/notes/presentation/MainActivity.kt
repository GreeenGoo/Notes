package com.education.notes.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.education.notes.R
import com.education.notes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //supportActionBar?.hide()
       /* val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_graph) as NavHostFragment)
                .navController*/
        //findNavController().navigate(R.id.list_fragment)
        /*val bottomNavView = binding.bottomNavigationView
        bottomNavView.setupWithNavController(navController)*/
    }
}
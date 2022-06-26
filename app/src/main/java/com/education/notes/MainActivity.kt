package com.education.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.education.notes.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view, NotesFragment())
            .commit()

        val bottomNavView: BottomNavigationView = binding.bottomNavigationView
        initBottomNavView(bottomNavView)
    }

    private fun initBottomNavView(bottomNav: BottomNavigationView){
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_navigation_menu_button_notes -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container_view, NotesFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_navigation_menu_button_tasks -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container_view, TasksFragment())
                        .commit()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
        bottomNav.setOnItemReselectedListener{}
    }
}
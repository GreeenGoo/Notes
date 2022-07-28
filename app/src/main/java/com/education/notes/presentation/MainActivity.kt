package com.education.notes.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.education.notes.R
import com.education.notes.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.findFragmentById(R.id.fragment_container_view)?.findNavController()
            ?.let {
                setupActionBarWithNavController(
                    it
                )
            }
        val bottomNavigationView = binding.mainActivityBottomNavigationView
        initBottomNavView(bottomNavigationView)
    }

    private fun initBottomNavView(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_notes_fragment -> {
                    fragment_container_view.findNavController().navigate(R.id.nav_graph_list_fragment)
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_nav_tasks_fragment -> {
                    fragment_container_view.findNavController().navigate(R.id.nav_graph_task_fragment)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
        bottomNavigationView.setOnItemReselectedListener { }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_container_view)
        return super.onSupportNavigateUp() || navController.navigateUp()
    }


}
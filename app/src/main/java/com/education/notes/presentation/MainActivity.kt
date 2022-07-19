package com.education.notes.presentation

//import androidx.navigation.findNavController
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.education.notes.R
import com.education.notes.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.findFragmentById(R.id.fragmentContainerView)?.findNavController()
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
                    Navigation.findNavController(this, R.id.fragmentContainerView)
                        .navigate(R.id.nav_graph_list_fragment)
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_nav_tasks_fragment -> {
                    Navigation.findNavController(this, R.id.fragmentContainerView)
                        .navigate(R.id.nav_graph_task_fragment)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
        bottomNavigationView.setOnItemReselectedListener { }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return super.onSupportNavigateUp() || navController.navigateUp()
    }


}
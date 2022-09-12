package com.education.notes.presentation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.education.notes.R
import com.education.notes.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration
            .Builder(
                R.id.notesListFragment,
                R.id.tasksFragment)
            .build()

        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView = binding.mainActivityBottomNavigationView
        initBottomNavView(bottomNavigationView, navController)
    }

    private fun initBottomNavView(
        bottomNavigationView: BottomNavigationView,
        navController: NavController
    ) {
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_notes_fragment -> {
                    navController.navigate(R.id.notesListFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_nav_tasks_fragment -> {
                    navController.navigate(R.id.tasksFragment)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
        bottomNavigationView.setOnItemReselectedListener { }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return super.onSupportNavigateUp() || navController.navigateUp()
    }

    fun setBottomNavigationMenuVisibility(visibility: Int) {
        binding.mainActivityBottomNavigationView.visibility = visibility
    }

    companion object{
        fun hideKeyboardFrom(context: Context, view: View?) {
            val imm =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }
    }
}
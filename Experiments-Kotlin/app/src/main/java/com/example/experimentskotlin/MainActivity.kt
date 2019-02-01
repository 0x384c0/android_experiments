package com.example.experimentskotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val host = navHostFragment as NavHostFragment
        val navController = host.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        val drawerLayout : DrawerLayout? = drawer_layout
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.coreNetworkTestFragment, R.id.nav_graph),
            drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)

        setupNavigationMenu(navController)
    }

    private fun setupNavigationMenu(navController: NavController) {
        nav_view?.setupWithNavController(navController)
    }
}

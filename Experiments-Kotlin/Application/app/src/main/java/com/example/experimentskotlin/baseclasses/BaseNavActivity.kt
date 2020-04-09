package com.example.experimentskotlin.baseclasses

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.experimentskotlin.R
import com.example.experimentskotlin.util.localize.NavDestinationLocalizer
import kotlinx.android.synthetic.main.activity_base_nav.*


abstract class BaseNavActivity : BaseActivity(), NavActivityWithToolbar {

    //region for overriding
    protected open val useCustomToolbar = false
    protected open val layoutId = R.layout.activity_base_nav
    protected abstract val navigationGraphId: Int
    //endregion

    //region UI
    protected val navController: NavController
        get() {
            val host = navHostFragment as NavHostFragment
            return host.navController
        }

    //endregion

    //region UI Actions
    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    //endregion

    //region Life Cycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        navController.setGraph(navigationGraphId)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        if (useCustomToolbar) {
            customToolbar.visibility = View.VISIBLE
            setSupportActionBar(customToolbar)
        } else {
            customToolbar.visibility = View.GONE
        }
        setupNavigation()
    }

    //endregion

    //region Others
    protected open fun setupNavigation() {
        setupActionBarWithNavController(navController)
        localizeNavigationTitles()
    }

    protected fun getCurrentFragment(): Fragment? {
        return navHostFragment.childFragmentManager.fragments.getOrNull(0)
    }

    protected fun isCanGoBack(): Boolean {
        return navController.currentDestination?.id != navController.graph.startDestination
    }

    private var navDestinationLocalizer: NavDestinationLocalizer? = null
    protected open fun localizeNavigationTitles() {
        if (navDestinationLocalizer == null) {
            navDestinationLocalizer = NavDestinationLocalizer(this, supportActionBar)
            navController.addOnDestinationChangedListener(navDestinationLocalizer!!)
        }
    }
    //endregion


    //region Custom Toolbar
    override fun hideToolbar() {
        if (useCustomToolbar) {
            customToolbar.visibility = View.GONE
        }
    }

    override fun showToolbar() {
        if (useCustomToolbar) {
            customToolbar.visibility = View.VISIBLE
        }
    }
    //endregion

}

interface NavActivityWithToolbar {
    fun hideToolbar()
    fun showToolbar()
}

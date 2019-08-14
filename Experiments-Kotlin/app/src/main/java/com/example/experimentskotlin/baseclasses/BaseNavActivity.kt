package com.example.experimentskotlin.baseclasses

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.experimentskotlin.R
import com.example.experimentskotlin.util.localize.NavDestinationLocalizer
import kotlinx.android.synthetic.main.activity_base_nav.*


abstract class BaseNavActivity : BaseActivity(), NavActivityWithToolbar {

    //region UI
    private val navController: NavController
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
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    //endregion

    //region Life Cycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_nav)
        navController.setGraph(getNavigationGraphId())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setupNavigation()
        if (useCustomToolbar) {
            customToolbar.visibility = View.VISIBLE
            setSupportActionBar(customToolbar)
        } else {
            customToolbar.visibility = View.GONE
        }
    }

    //endregion

    //region Others
    private lateinit var navDestinationLocalizer:NavDestinationLocalizer
    private fun setupNavigation() {
        if (!::navDestinationLocalizer.isInitialized) {
            navDestinationLocalizer = NavDestinationLocalizer(this,supportActionBar)
            navController.addOnDestinationChangedListener(navDestinationLocalizer)
        }
    }

    internal fun getCurrentFragment():Fragment?{
        return navHostFragment.childFragmentManager.fragments.getOrNull(0)
    }
    //endregion

    //region for overriding
    open val useCustomToolbar = false
    abstract fun getNavigationGraphId(): Int
    //endregion


    //region Custom Toolbar
    override fun hideToolbar(){
        if (useCustomToolbar) {
            customToolbar.visibility = View.GONE
        }
    }
    override fun showToolbar(){
        if (useCustomToolbar) {
            customToolbar.visibility = View.VISIBLE
        }
    }
    //endregion

}

interface NavActivityWithToolbar{
    fun hideToolbar()
    fun showToolbar()
}

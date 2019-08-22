package com.example.experimentskotlin.activity

import com.example.experimentskotlin.R
import com.example.experimentskotlin.baseclasses.BaseNavActivity

class MainActivity : BaseNavActivity() {
    //region LifeCycle
    override fun getNavigationGraphId(): Int {
        return R.navigation.main_navigation
    }
    //endregion
}

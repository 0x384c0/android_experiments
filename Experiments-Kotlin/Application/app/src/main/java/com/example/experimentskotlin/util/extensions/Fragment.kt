package com.example.experimentskotlin.util.extensions

import android.util.Log
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.NavOptions
import com.example.experimentskotlin.R


fun Fragment.navigateSafe(@NonNull directions: NavDirections) {

    val navOptionsBuilder = NavOptions.Builder()
        .setEnterAnim(R.anim.nav_default_enter_anim)
        .setExitAnim(R.anim.nav_default_exit_anim)
        .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
        .setPopExitAnim(R.anim.nav_default_pop_exit_anim)

    val o = findNavController().currentDestination?.getAction(directions.actionId)?.navOptions
    if (o != null) {
        if (o.popUpTo != -1)
            navOptionsBuilder.setPopUpTo(o.popUpTo, o.isPopUpToInclusive)
        navOptionsBuilder.setLaunchSingleTop(o.shouldLaunchSingleTop())
    }

    try {
        findNavController().navigate(directions, navOptionsBuilder.build())
    } catch (e: Exception) {
        Log.e("Fragment", "navigateSafe", e)
    }
}
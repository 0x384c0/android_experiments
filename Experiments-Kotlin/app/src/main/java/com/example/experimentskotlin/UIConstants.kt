package com.example.experimentskotlin

import com.google.android.gms.maps.model.LatLng

/**
 * Набор констант для UI
 */
class UIConstants {

    data class LocaleUiInfo(val locale: String, val iconId: Int, val titleId: Int)

    companion object {
        const val DATE_FORMAT = "dd.MM.yyyy"
        const val DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm"
        const val DATE_HYPHEN_TIME_FORMAT = "dd.MM.yyyy - HH:mm"
        const val DATE_WEEKDAY_FORMAT = "dd MMMM yyyy, EEE"
        const val FORM_VALIDATOR_RESET_DELAY_MILLISECONDS = 100L
        const val SCROLL_TO_BOTTOM_DELAY_MILLISECONDS = 500L
        const val ANIMATION_DURATION_MILLISECONDS = 300L
        const val BANNER_ROTATION_INTERVAL_MILLISECONDS = 5000L
        const val MAP_ZOOM_MY_LOCATION = 14f
        const val MAP_ZOOM_POINTS_VISIBLE = 12f
        val MAP_DEFAULT_LOCATION = LatLng(42.8699284, 74.6049057)//Bishkek

        const val NOT_READY_TEXT = "This function is not implemented"
    }
}
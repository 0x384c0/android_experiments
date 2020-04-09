package com.example.experimentskotlin.baseclasses

import android.content.Intent
import android.os.Bundle
import com.example.corenetwork.SessionManager

/**
 *  базовый класс для активности, которая может показать пин
 *  все его методы описаны в документации Android к Activivty
 */
/*
abstract class BasePinPresenterActivity : BaseNavActivity() {

    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AutoLockManager.getInstance().lockHandler = {
            startActivity(Intent(this, PinCodeEnterActivity::class.java))
            true
        }
        if (!SessionManager.getInstance().pin.isNullOrBlank())
            AutoLockManager.getInstance().lockAppIfNeeded()
    }

    override fun onResume() {
        super.onResume()
        if (SessionManager.getInstance().pin.isNullOrBlank())
            startActivity(Intent(this, PinCodeCreateNavActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        AutoLockManager.getInstance().lockHandler = null
    }
    //endregion
}
*/
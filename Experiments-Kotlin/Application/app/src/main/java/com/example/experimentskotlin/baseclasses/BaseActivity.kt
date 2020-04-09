package com.example.experimentskotlin.baseclasses


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.example.corenetwork.model.ErrorResponse
import com.example.experimentskotlin.R
import com.kaopiz.kprogresshud.KProgressHUD
import io.reactivex.disposables.CompositeDisposable

/**
 * Базовый класс Activity
 *
 * отвечает за алерты, анимацию загрузки, навигацию
 */
abstract class BaseActivity : AppCompatActivity() {
    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLoading()
    }

    override fun onResume() {
//        Crashlytics.log("${javaClass.name}.onResume")
        super.onResume()
        subscribeToEvents()
    }

    override fun onPause() {
//        Crashlytics.log("${javaClass.name}.onPause")
        super.onPause()
    }

    override fun onDestroy() {
        unSubscribeFromEvents()
        alertDialog?.dismiss()
        progressHud.dismiss()
        super.onDestroy()
    }
    //endregion

    //region others
    fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            val rootView: View = findViewById(android.R.id.content)
            rootView.isFocusable = true
            rootView.isFocusableInTouchMode = true
            rootView.requestFocus()
        }
    }
    //endregion

    //region Alerts
    private var toast: Toast? = null

    fun Context.toast(message: CharSequence) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun showAlert(e: Throwable) {
        showAlert(
            getString(R.string.error),
            ErrorResponse.create(e)?.getErrorMessage() ?: e.localizedMessage ?: e.toString()
        )
    }

    private var alertDialog: AlertDialog? = null
    fun showAlert(title: String?, text: String) {
        hideLoading()
        alertDialog?.cancel()
        alertDialog = AlertDialog.Builder(this, R.style.AppTheme_AlertDialogTheme)
            .setTitle(title)
            .setMessage(text)
            .setNegativeButton(getString(R.string.close)) { d, _ -> d.cancel() }
            .create()
        alertDialog?.show()
    }
    //endregion

    //region Loading
    private lateinit var progressHud: KProgressHUD
    private lateinit var loadingImageView: ImageView
    private lateinit var loadingAnimation: AlphaAnimation
    private fun setupLoading() {
        loadingImageView = ImageView(this)
        loadingImageView.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.ic_launcher_background
            )
        )
        loadingImageView.scaleType = ImageView.ScaleType.CENTER_CROP

        loadingAnimation = AlphaAnimation(0.4f, 1.0f)
        loadingAnimation.duration = 1000
        loadingAnimation.startOffset = 0
        loadingAnimation.repeatMode = Animation.REVERSE
        loadingAnimation.repeatCount = Animation.INFINITE

        progressHud = KProgressHUD
            .create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
            .setLabel(getString(R.string.loading))
    }

    fun showLoading() {
        progressHud.show()
        loadingImageView.startAnimation(loadingAnimation)
    }

    fun hideLoading() {
        loadingImageView.clearAnimation()
        progressHud.dismiss()
    }
    //endregion

    //region notifications
    private var notificationCompositeDisposable: CompositeDisposable? = null

    private fun subscribeToEvents() {
        notificationCompositeDisposable?.dispose()
        notificationCompositeDisposable = CompositeDisposable()
    }

    private fun unSubscribeFromEvents() {
        notificationCompositeDisposable?.dispose()
        notificationCompositeDisposable = null
    }
    //endregion


    //region BackButton exit
    private var doubleBackToExitPressedOnce = false

    internal fun pressBackToExit() {
        if (doubleBackToExitPressedOnce) {
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.press_back_again_to_exit), Toast.LENGTH_SHORT)
            .show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
    //endregion
}
package com.example.experimentskotlin.coreNetowrkTest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.corenetwork.CoreNetwork
import com.example.experimentskotlin.R
import com.example.experimentskotlin.navigaton.NavHostActivity
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_core_network_test.*

class CoreNetworkTestActivity : AppCompatActivity() {

    //UI Actions
    fun onTestQueryClick(@Suppress("UNUSED_PARAMETER") view: View){
        val coreNetwork = CoreNetwork()
        Log.w(localClassName,coreNetwork.test())
        refresh()
    }
    fun onLoadImageQueryClick(@Suppress("UNUSED_PARAMETER") view: View){
        Picasso.get()
            .load("https://vignette.wikia.nocookie.net/kancolle/images/9/96/Tashkent_Card.png")
            .into(loadedImageView)
    }
    fun openNavigationClick(@Suppress("UNUSED_PARAMETER") view: View) {
        val intent = Intent(this, NavHostActivity::class.java)
        startActivity(intent)
    }


    // LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    //Others
    private var disposable: Disposable? = null
    private val coreNetwork = CoreNetwork()
    private fun refresh(){

        disposable = coreNetwork.wikiApi.query(
            "query",
            "json",
            "tashkent"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.w(localClassName,result.toString())
                    Log.w(localClassName,result.query.toString())
                    Log.w(localClassName,result.query.pages.toString())
                    Log.w(localClassName,result.query.pages.page633093.toString())
                    Log.w(localClassName,result.query.pages.page633093.pageid)
                },
                { error -> Log.e(localClassName,error.message) }
            )
    }
}

package com.example.experimentskotlin


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.corenetwork.CoreNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    //UI Actions
    fun onTestQueryClick(@Suppress("UNUSED_PARAMETER") view: View){
        val coreNetwork = CoreNetwork()
        Log.w(localClassName,coreNetwork.test())
        refresh()
    }
    fun onLoadImageQueryClick(@Suppress("UNUSED_PARAMETER") view: View){
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

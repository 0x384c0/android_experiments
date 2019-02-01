package com.example.experimentskotlin.coreNetowrkTest


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.corenetwork.CoreNetwork

import com.example.experimentskotlin.R
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_core_network_test.*


/**
 * A simple [Fragment] subclass.
 *
 */
class CoreNetworkTestFragment : Fragment() {

    //UI Actions
    private fun onTestQueryClick(@Suppress("UNUSED_PARAMETER") view: View){
        val coreNetwork = CoreNetwork()
        Log.w(javaClass.name,coreNetwork.test())
        refresh()
    }
    private fun onLoadImageQueryClick(@Suppress("UNUSED_PARAMETER") view: View){
        Picasso.get()
            .load("https://vignette.wikia.nocookie.net/kancolle/images/9/96/Tashkent_Card.png")
            .into(loadedImageView)
    }


    // LifeCycle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_core_network_test, container, false)
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queryButton.setOnClickListener { onTestQueryClick(it) }
        loadImageButton.setOnClickListener { onLoadImageQueryClick(it) }
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
                    Log.w(javaClass.name,result.toString())
                    Log.w(javaClass.name,result.query.toString())
                    Log.w(javaClass.name,result.query.pages.toString())
                    Log.w(javaClass.name,result.query.pages.page633093.toString())
                    Log.w(javaClass.name,result.query.pages.page633093.pageid)
                },
                { error -> Log.e(javaClass.name,error.message) }
            )
    }

}

package com.example.experimentskotlin

import android.app.Application
import com.example.corenetwork.Api

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.init()
    }
}
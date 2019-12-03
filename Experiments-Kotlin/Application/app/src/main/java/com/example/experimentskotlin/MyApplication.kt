package com.example.experimentskotlin

import androidx.multidex.MultiDexApplication
import com.example.corenetwork.Api

class MyApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        Api.init()
    }
}
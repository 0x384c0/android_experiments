package com.example.corenetwork

class CoreNetwork{
    fun test(): String {
        return "CoreNetwork test called"
    }
    val wikiApi by lazy {
        Api.getInstance()
    }
}


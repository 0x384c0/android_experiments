package com.example.experimentskotlin.util.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> MutableLiveData<T>.observe(lifecycleOwner: LifecycleOwner, handler:((T) -> Unit)){
    if (!hasObservers()) {
        observe(lifecycleOwner, Observer<T>(handler))
    }
}
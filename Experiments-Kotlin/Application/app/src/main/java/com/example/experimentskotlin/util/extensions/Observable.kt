package com.example.experimentskotlin.util.extensions


import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Запуск onNext в main thread
 */
@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T> Observable<T>.subscribeOnMain(
    onNext: ((T) -> Unit)?,
    onError: ((Throwable) -> Unit)? = null,
    onComplete: ((Unit) -> Unit)? = null
): Disposable {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onNext, { onError?.invoke(it) }, { onComplete?.invoke(Unit) })
}


/**
 * Запуск onNext в main thread
 */
@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun Completable.subscribeOnMain(
    onNext: (() -> Unit)?,
    onError: ((Throwable) -> Unit)? = null
): Disposable {
    return subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onNext, onError)
}

fun Disposable.disposedBy(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}


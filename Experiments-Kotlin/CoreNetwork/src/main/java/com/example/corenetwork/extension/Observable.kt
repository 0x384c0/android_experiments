package com.example.corenetwork.extension


import com.example.corenetwork.SessionManager
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Проверка авторизации, токенов, обработка ошибок авторизации
 */
fun <T> Observable<T>.flatMapAuth(sessionManager: SessionManager): Observable<T> {
    return sessionManager.updateTokenIfNeeded().flatMap { this }
}

/**
 * Проверка авторизации, токенов, обработка ошибок авторизации
 */
fun Completable.flatMapAuth(sessionManager: SessionManager): Completable {
    return sessionManager.updateTokenIfNeeded().flatMapCompletable { this }
}


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
        .subscribe(onNext,onError)
}

fun Disposable.disposedBy(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}


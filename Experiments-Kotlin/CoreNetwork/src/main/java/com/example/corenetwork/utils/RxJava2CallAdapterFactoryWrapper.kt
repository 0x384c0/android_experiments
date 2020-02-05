package com.example.corenetwork.utils

import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

class RxJava2CallAdapterFactoryWrapper(
    private val factory: RxJava2CallAdapterFactory,
    private val mapObservable: (Observable<*>) -> Observable<*>,
    private val mapCompletable: (Completable) -> Completable
) :
    CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val adapter = factory.get(returnType, annotations, retrofit)
        return if (adapter == null)
            null
        else
            CallAdapterWrapper(
                adapter,
                mapObservable,
                mapCompletable
            )
    }

    private class CallAdapterWrapper<R>(
        private val adapter: CallAdapter<R, *>,
        private val mapObservable: (Observable<*>) -> Observable<*>,
        private val mapCompletable: (Completable) -> Completable
    ) : CallAdapter<R, Any> {
        override fun adapt(call: Call<R>): Any {
            return when (val adapt = adapter.adapt(call)) {
                is Observable<*> -> mapObservable(adapt)
                is Completable -> mapCompletable(adapt)
                else -> adapt
            }
        }

        override fun responseType(): Type {
            return adapter.responseType()
        }
    }
}
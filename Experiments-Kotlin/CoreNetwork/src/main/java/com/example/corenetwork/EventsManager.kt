package com.example.corenetwork

import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class EventsManager {
    val loginStateChanged = RxBusEmpty()
    val localeChanged = RxBus<String>()

    companion object {
        private val instance = EventsManager()
        fun getInstance(): EventsManager {
            return instance
        }
    }

    //rx bus

    /**
     * Класс - обертка над PublishSubject
     */
    open inner class RxBus<T>(private var lateObservers: Boolean = false) {
        private val bus = PublishSubject.create<T>()
        private var lastObject: T? = null

        fun post(o: T) {
            bus.onNext(o)
            if (lateObservers)
                lastObject = o
        }

        @CheckReturnValue
        fun subscribe(onNext: (T) -> (Unit)): Disposable {
            var lastObjectMaybe: T? = null
            if (lateObservers && !bus.hasObservers() && lastObject != null) {
                lastObjectMaybe = lastObject
            }

            val disposable = bus.subscribe(onNext, { e ->
                System.out.print(e)
            })
            if (lastObjectMaybe != null)
                post(lastObjectMaybe)

            return disposable
        }
    }

    inner class RxBusEmpty(lateObservers: Boolean = false) : RxBus<Unit>(lateObservers) {
        fun post() {
            super.post(Unit)
        }
    }
}
package com.arny.aircraftrefueling.utils

import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <T> fromSingle(callable: () -> T): Single<T> {
    return Single.fromCallable(callable)
}

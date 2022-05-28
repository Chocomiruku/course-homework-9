package com.chocomiruku.homework9

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/*Не совсем поняла, куда пристроить вторую часть задания, посвящённую тестам использования
различных планировщиков, операторов объединения и т.д. Не хотелось перегружать класс фрагмента
или ВьюМодели поиска. Вынесла сюда, введя два произвольных Observable*/

const val TAG = "RxTest"

class SecondTask {
    fun run() {
        val firstObservable: Observable<String> = Observable.create<String> { emitter ->
            for (i in 0..20) {
                Thread.sleep(200)
                emitter.onNext("Item $i")
            }
        }.doOnNext {
            Log.i(TAG, "SubscribeOn thread: " + Thread.currentThread().name)
        }

        val secondObservable: Observable<Double> = Observable.create<Double> { emitter ->
            for (i in 0..20) {
                Thread.sleep(200)
                emitter.onNext(i * 55.0)
            }
        }.doOnNext {
            Log.i(TAG, "SubscribeOn thread: " + Thread.currentThread().name)
        }

        // Experiments with different Schedulers
        firstObservable
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.i(TAG, "Emit: " + it + ". ObserveOn thread: " + Thread.currentThread().name)
            }

        firstObservable
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe {
                Log.i(TAG, "Emit: " + it + ". ObserveOn thread: " + Thread.currentThread().name)
            }

        firstObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.io())
            .subscribe {
                Log.i(TAG, "Emit: " + it + ". ObserveOn thread: " + Thread.currentThread().name)
            }


        // Experimenting with combining operations, chaining observeOn/subscribeOn operators
        firstObservable
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .zipWith(
                secondObservable
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
            ) { first, second ->
                "$first Cost: $second"
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.i(
                    TAG,
                    "Result" + it + ". ObserveOn thread (combined): " + Thread.currentThread().name
                )
            }
    }
}
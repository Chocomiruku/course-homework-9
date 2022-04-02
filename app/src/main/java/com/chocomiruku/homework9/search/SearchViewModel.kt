package com.chocomiruku.homework9.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class SearchViewModel(private val application: Application) : ViewModel() {
    private lateinit var parsedModels: List<Model>
    var filteredModels: MutableList<Model> = mutableListOf()
    var oldFilteredModels: MutableList<Model> = mutableListOf()
    private val disposeBag = CompositeDisposable()

    init {
        getModels()
    }

    private fun getModels() {
        disposeBag.add(
            parseJson(application.applicationContext)
                .subscribeOn(Schedulers.computation())
                .subscribe({
                    parsedModels = it
                    oldFilteredModels.addAll(it)
                }, {
                    it.message?.let { message -> Log.e(TAG, message) }
                })
        )
    }

    fun search(query: String): Completable = Completable.create { it ->
        val wanted = parsedModels.filter {
            it.title.contains(query) || it.body.contains(query)
        }.toList()

        filteredModels.clear()
        filteredModels.addAll(wanted)
        it.onComplete()
    }

    override fun onCleared() {
        super.onCleared()
        disposeBag.dispose()
    }

    companion object {
        const val TAG = "SearchViewModel"
    }
}
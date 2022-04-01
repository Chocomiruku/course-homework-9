package com.chocomiruku.homework9.search

import android.app.Application
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Completable

class SearchViewModel(private val application: Application) : ViewModel() {
    lateinit var parsedModels: List<Model>
    var filteredModels: MutableList<Model> = mutableListOf()
    var oldFilteredModels: MutableList<Model> = mutableListOf()

    init {
        getModels()
    }

    private fun getModels() {
        parsedModels = parseJson(application.applicationContext)
        oldFilteredModels.addAll(parsedModels)
    }

    fun search(query: String): Completable = Completable.create { it ->
        val wanted = parsedModels.filter {
            it.title.contains(query) || it.body.contains(query)
        }.toList()

        filteredModels.clear()
        filteredModels.addAll(wanted)
        it.onComplete()
    }
}
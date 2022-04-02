package com.chocomiruku.homework9.search

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Single

fun AssetManager.readFile(fileName: String) = open(fileName)
    .bufferedReader()
    .use { it.readText() }

/* По заданию нужно было обернуть в Observable, но мне показалось,
    что в данном случае лучше обернуть в Single */
fun parseJson(context: Context): Single<List<Model>> = Single.create { subscriber ->
    try {
        val jsonString = context.assets.readFile("data.json")
        val typeToken = object : TypeToken<List<Model>>() {}.type
        subscriber.onSuccess(Gson().fromJson(jsonString, typeToken))
    } catch (e: Exception) {
        subscriber.onError(e)
    }
}
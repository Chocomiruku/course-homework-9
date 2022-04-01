package com.chocomiruku.homework9.search

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun AssetManager.readFile(fileName: String) = open(fileName)
    .bufferedReader()
    .use { it.readText() }

fun parseJson(context: Context): List<Model> {
    val jsonString = context.assets.readFile("data.json")
    val typeToken = object : TypeToken<List<Model>>() {}.type
    return Gson().fromJson(jsonString, typeToken)
}
package com.chocomiruku.homework9.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Model(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
) : Parcelable


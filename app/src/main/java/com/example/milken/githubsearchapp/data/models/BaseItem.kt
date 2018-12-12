package com.example.milken.githubsearchapp.data.models

import android.os.Parcelable

interface BaseItem : Parcelable {
    val id: Long
    val dataType: DataType
}
package com.example.milken.githubsearchapp.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SearchDataParcel(val itemList: List<BaseItem>) : Parcelable
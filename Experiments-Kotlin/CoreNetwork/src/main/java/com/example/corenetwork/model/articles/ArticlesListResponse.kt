package com.example.corenetwork.model.articles

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class ArticlesListResponse(
    val items: List<ArticleItem>,
    val basepath: String,
    val offset: String
)

@Parcelize
data class ArticleItem(
    val id: Int,
    val title: String,
    val url: String,
    val ns: Int
) : Parcelable
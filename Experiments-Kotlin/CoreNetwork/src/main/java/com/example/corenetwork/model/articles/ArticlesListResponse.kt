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
    var title: String,
    val url: String,
    val ns: Int
) : Parcelable{
    override fun equals(other: Any?): Boolean {
        return (other as? ArticleItem)?.id == id
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + ns
        return result
    }
}
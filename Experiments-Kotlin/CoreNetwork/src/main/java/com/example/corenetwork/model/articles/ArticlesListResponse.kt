package com.example.corenetwork.model.articles

data class ArticlesListResponse(
    val items: List<ArticleItem>,
    val basepath: String,
    val offset: String
)

data class ArticleItem(
    val id: Int,
    val title: String,
    val url: String,
    val ns: Int
)
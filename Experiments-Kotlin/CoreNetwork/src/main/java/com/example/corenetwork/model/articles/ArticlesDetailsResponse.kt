package com.example.corenetwork.model.articles

data class ArticlesDetailsResponse(
    val sections: List<ArticleSection>
)

data class ArticleSection(
    val title: String,
    val level: Int,
    val content: List<ArticleContent>
){
    fun getText():String{
        return content
            .map { it.text }
            .filter { !it.isNullOrBlank() }
            .joinToString { "$it\n"  }
    }
}

data class ArticleContent(
    val type: String,
    val text: String?
)
package com.example.experimentskotlin.fragment.articles

import androidx.lifecycle.MutableLiveData
import com.example.corenetwork.extension.disposedBy
import com.example.corenetwork.extension.subscribeOnMain
import com.example.corenetwork.model.articles.ArticleItem
import com.example.experimentskotlin.baseclasses.BaseViewModel
import com.example.experimentskotlin.util.InfinityScrollManager

class ArticlesViewModel: BaseViewModel(),InfinityScrollManager.InfinityScrollViewModelInterface{
    //region UI Binding
    var articles = MutableLiveData<List<ArticleItem>>()
    //endregion

    //region LifeCycle
    override fun onResume() {
        super.onResume()
        if (articles.value.isNullOrEmpty())
            refresh(false)
        else
            articles.value = articles.value
    }
    //endregion

    //region Others
    private var offset:String? = null
    private var canLoadMore = true
    override fun refresh(nextPage: Boolean){
        if (!nextPage) {
            offset = null
            canLoadMore = true
            articles.value = listOf()
        }

        showLoading()
        api
            .getArticlesList(offset)
            .subscribeOnMain(
                onNext = {
                    offset = it.offset
                    articles.value =  (articles.value ?: listOf()) + it.items
                    canLoadMore = it.items.isNotEmpty()
                    hideLoading()
                },
                onError = this::showAlert
            )
            .disposedBy(compositeDisposable)
    }
    //endregion

    //region InfinityScrollManager.InfinityScrollViewModelInterface
    override fun isCanLoadMore(): Boolean {
        return canLoadMore
    }
    //endregion

}
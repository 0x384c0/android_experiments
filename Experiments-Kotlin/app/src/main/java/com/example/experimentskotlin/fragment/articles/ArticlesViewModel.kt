package com.example.experimentskotlin.fragment.articles

import androidx.lifecycle.MutableLiveData
import com.example.corenetwork.extension.disposedBy
import com.example.corenetwork.extension.subscribeOnMain
import com.example.corenetwork.model.articles.ArticleItem
import com.example.experimentskotlin.baseclasses.BaseViewModel

class ArticlesViewModel: BaseViewModel(){
    //region UI Binding
    var articles = MutableLiveData<List<ArticleItem>>()
    //endregion

    //region LifeCycle
    override fun onResume() {
        super.onResume()
        refresh()
    }
    //endregion

    //region Others
    fun refresh(){
        showLoading()
        api
            .getArticlesList()
            .subscribeOnMain(
                onNext = {
                    hideLoading()
                    articles.value = it.items
                },
                onError = this::showAlert
            )
            .disposedBy(compositeDisposable)
    }
    //endregion
}
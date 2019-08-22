package com.example.experimentskotlin.fragment.articles

import androidx.lifecycle.MutableLiveData
import com.example.corenetwork.extension.disposedBy
import com.example.corenetwork.extension.subscribeOnMain
import com.example.corenetwork.model.articles.ArticleItem
import com.example.corenetwork.model.articles.ArticleSection
import com.example.experimentskotlin.baseclasses.BaseViewModel

class ArticlesDetailsViewModel : BaseViewModel() {

    //region UI Binding
    var articleDetails =
        MutableLiveData<List<ArticleSection>>()
    //endregion

    //region LifeCycle
    override fun onResume() {
        super.onResume()
        refresh()
    }
    //endregion

    //region Others
    private var id: Int = -1

    fun setup(item: ArticleItem) {
        id = item.id
    }

    private fun refresh() {
        showLoading()
        api
            .getArticlesDetails(id)
            .subscribeOnMain(
                onNext = {
                    hideLoading()
                    articleDetails.value = it.sections
                },
                onError = this::showAlert
            )
            .disposedBy(compositeDisposable)
    }
    //endregion
}
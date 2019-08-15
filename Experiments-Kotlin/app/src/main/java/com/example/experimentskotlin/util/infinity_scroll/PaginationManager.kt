package com.example.experimentskotlin.util.infinity_scroll

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * Класс для управлением пагинацией
 *
 * работает в связке с InfinityScrollManager
 * докачивает страницы, скрытно обновляет список при необходимости
 */
class PaginationManager<T, P>(
    private val dataBinding: MutableLiveData<List<T>>,
    private val getNextPage: (P?) -> P,
    private val getDataObservable: (P) -> Observable<List<T>>,
    private val subscribe: (Observable<List<T>>) -> Disposable,
    private val showLoading: () -> Unit,
    private val isCanLoadMore: (List<T>) -> Boolean
) : InfinityScrollManager.InfinityScrollViewModelInterface {
    //region LifeCycle
    /**
     * уведомление о onResume
     *
     * По умолчанию перегружаются все загруженные страницы в фоне
     */
    fun onResume() {
        if (dataBinding.value.isNullOrEmpty())
            refresh()
        else {
            dataBinding.value = dataBinding.value
            val pageForReloadAtNextOnResume = pageForReloadAtNextOnResume
            if (pageForReloadAtNextOnResume != null)
                reloadPageSilently(page = pageForReloadAtNextOnResume, withFirstPage = true)
            else {
                if (isSkipReloadPagesAtNextOnResume)
                    isSkipReloadPagesAtNextOnResume = false
                else
                    reloadPagesSilently()
            }
        }
    }
    //endregion

    //region UI Actions
    /**
     * уведомление от том, что страницу с элементом нужно перегрузить после onResume
     */
    fun reloadPageForItemAtNextOnResume(item: T) {
        isSkipReloadPagesAtNextOnResume = false
        pageForReloadAtNextOnResume = getPageForItem(item)
    }

    /**
     * уведомление от том, что нужно грузить с 1й страницы, сбросив данных
     */
    fun resetAndLoadFirstPageAtNextOnResume() {
        dataBinding.value = mutableListOf()
        resetPages()
    }

    /**
     * уведомление от том, что ненужно грузить страницы после onResume
     */
    fun skipReloadPagesAtNextOnResume() {
        isSkipReloadPagesAtNextOnResume = true
        pageForReloadAtNextOnResume = null
    }
    //endregion

    //region Others
    var recyclerViewData = mutableListOf<T>()
    private var page: P = getNextPage(null)
    private var loadedPages = mutableListOf<P>()
    private var pageForReloadAtNextOnResume: P? = null
    private var disposable: Disposable? = null
    private val itemPageMap = mutableMapOf<T, P>()
    private var isSkipReloadPagesAtNextOnResume = false


    private fun getPageForItem(item: T): P? {
        return itemPageMap[item]
    }

    private fun updateItemPageMap(items: List<T>?, page: P) {
        if (!items.isNullOrEmpty())
            for (item in items)
                itemPageMap[item] = page
    }

    private fun resetPages() {
        recyclerViewData = mutableListOf()
        loadedPages.clear()
        itemPageMap.clear()
        page = getNextPage(null)
        isSkipReloadPagesAtNextOnResume = false
        pageForReloadAtNextOnResume = null
    }

    /**
     * скрытно обновлить страницу page и первую страницу, если нужно
     */
    private fun reloadPageSilently(page: P, withFirstPage: Boolean) {
        val pagesToReload = mutableListOf(page)
        if (withFirstPage) {
            val firstPage = getNextPage(null)
            if (!pagesToReload.contains(firstPage))
                pagesToReload.add(firstPage)
        }

        val observables = pagesToReload
            .map { getDataObservable(it) }
            .toTypedArray()

        val getPagesObservable = Observable
            .combineLatest(observables, this::mergeResults)

        disposable?.dispose()
        disposable = subscribe(getPagesObservable
            .map { newPageData ->
                for (item in newPageData) {
                    val index = recyclerViewData.indexOf(item)
                    if (index != -1) {
                        recyclerViewData.removeAt(index)
                        recyclerViewData.add(index, item)
                    } else {
                        recyclerViewData.add(0, item)
                    }
                }
                updateItemPageMap(newPageData, page)
                recyclerViewData
            }
        )
    }

    /**
     * скрытно обновлить список
     */
    private fun reloadPagesSilently() {
        val loadedPages = loadedPages
        val observables = loadedPages
            .map { getDataObservable(it) }
            .toTypedArray()

        val updatePagesObservable = Observable
            .combineLatest(observables, this::mergeResults)

        disposable?.dispose()
        disposable = subscribe(
            updatePagesObservable.map { newList ->
                recyclerViewData = newList
                recyclerViewData
            }
        )
    }

    @Suppress("UNCHECKED_CAST", "NAME_SHADOWING")
    private fun mergeResults(results: Array<Any>): MutableList<T> {
        val results = results as Array<*>

        loadedPages.forEachIndexed { index, page ->
            updateItemPageMap(results.getOrNull(index) as? List<T>, page)
        }

        val result = mutableListOf<T>()
        for (item in results)
            result.addAll(item as List<T>)
        return result
    }
    //endregion

    //region InfinityScrollViewModelInterface
    override fun refresh(nextPage: Boolean) {
        if (nextPage) {
            page = getNextPage(page)
        } else {
            showLoading()
            resetPages()
        }

        disposable?.dispose()
        disposable = subscribe(
            getDataObservable(page).map { newPageData ->
                updateItemPageMap(newPageData, page)
                recyclerViewData.addAll(newPageData)
                loadedPages.add(page)
                recyclerViewData
            }
        )
    }

    override fun isCanLoadMore(): Boolean {
        return isCanLoadMore.invoke(recyclerViewData)
    }
    //endregion
}
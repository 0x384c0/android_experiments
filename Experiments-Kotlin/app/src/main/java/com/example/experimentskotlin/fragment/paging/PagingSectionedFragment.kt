package com.example.experimentskotlin.fragment.paging


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.corenetwork.model.articles.ArticleItem
import com.example.experimentskotlin.R
import com.example.experimentskotlin.util.adapters.SingleLayoutSectionedAdapter
import com.example.experimentskotlin.util.adapters.paging.PagedSingleLayoutSectionedAdapter
import kotlinx.android.synthetic.main.fragment_paging_sectioned.*
import kotlinx.android.synthetic.main.item_article.view.*
import kotlinx.android.synthetic.main.item_operation_section_date.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class PagingSectionedFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paging_sectioned, container, false)
    }

    lateinit var adapter: PagedSingleLayoutSectionedAdapter<ArticleItem, String>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PagedSingleLayoutSectionedAdapter(this,
                R.layout.item_article,
                R.layout.item_operation_section_date,
                { ItemVH(it, this::refresh) },
                { SectionVH(it) },
                { continueLoadingPages ->
                    data.addAll(getNextPage())
                    adapter.data = adapter.toItemOrSectionData(
                            items = data,
                            itemToSectionData = { (it.id / 10).toString() }
                    )
                    continueLoadingPages()
                }
        )
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        data.addAll(getNextPage())
        adapter.data = adapter.toItemOrSectionData(
                items = data,
                itemToSectionData = { (it.id / 10).toString() }
        )
    }

    private var page = 0
    private var data = mutableListOf<ArticleItem>()
    private fun getNextPage(): List<ArticleItem> {
        val r = getNextPage((page * 100)..((page + 1) * 100 - 1))
        page++
        return r
    }

    private fun getNextPage(range: IntRange): List<ArticleItem> {
        return range.toList()
                .map {
                    ArticleItem(it, it.toString(), it.toString(), it)
                }
    }

    private fun refresh() {
        adapter.data = adapter.toItemOrSectionData(
                items = data,
                itemToSectionData = { (it.id / 10).toString() }
        )
        adapter.invalidate()
    }


    class ItemVH(view: View, private val refresh: () -> Unit) : SingleLayoutSectionedAdapter.BaseItemViewHolder<ArticleItem>(view) {
        override fun setup(data: ArticleItem) {
            view.titleTextView.text = data.title
            view.urlTextView.text = data.url
            view.setOnClickListener {
                refresh()
            }
        }
    }

    class SectionVH(view: View) : SingleLayoutSectionedAdapter.BaseItemViewHolder<String>(view) {
        override fun setup(data: String) {
            view.sectionTitleTextView.text = data
        }
    }
}

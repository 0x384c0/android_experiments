package com.example.experimentskotlin.fragment.articles

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.corenetwork.model.articles.ArticleSection
import com.example.experimentskotlin.R
import com.example.experimentskotlin.baseclasses.BaseMVVMFragment
import com.example.experimentskotlin.util.adapters.SingleLayoutAdapter
import com.example.experimentskotlin.util.extensions.observe
import kotlinx.android.synthetic.main.fragment_article_details.*
import kotlinx.android.synthetic.main.item_article.view.*

class ArticlesDetailsFragment : BaseMVVMFragment<ArticlesDetailsViewModel>() {
    //region Overrides
    override val viewModelClass = ArticlesDetailsViewModel::class.java
    override val layoutID = R.layout.fragment_article_details
    //endregion

    //region LifeCycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }
    //endregion

    //region Data Binding
    override fun bindData() {
        super.bindData()
        viewModel.articleDetails.observe(this) {
            adapter.data = it
        }
        viewModel.setup(ArticlesDetailsFragmentArgs.fromBundle(arguments).articleItem)
    }
    //endregion

    //region RecyclerView
    private lateinit var adapter: SingleLayoutAdapter<ArticleSection>

    private fun setupRecyclerView() {
        adapter = SingleLayoutAdapter(
            itemLayoutId = R.layout.item_article,
            bindViewHandler = { view, data ->
                view.titleTextView.text = data.title
                view.urlTextView.text = data.getText()
            },
            onClickHandler = { _, _ ->

            }
        )
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }
    //endregion
}
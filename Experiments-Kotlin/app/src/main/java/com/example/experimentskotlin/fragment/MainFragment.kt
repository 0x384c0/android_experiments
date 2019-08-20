package com.example.experimentskotlin.fragment


import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.experimentskotlin.R
import com.example.experimentskotlin.baseclasses.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseFragment() {
    override val layoutID = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coreNetworkTestFragment.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.coreNetworkTestFragment())
        }
        articlesButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.articlesFragment())
        }
        pagingLibraryButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.pagingFragment())
        }
        pagingSectionedDiffUtil.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.pagingSectionedFragment())
        }

    }
}

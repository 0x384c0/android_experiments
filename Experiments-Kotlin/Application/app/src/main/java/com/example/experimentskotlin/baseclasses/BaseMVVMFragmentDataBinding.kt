package com.example.experimentskotlin.baseclasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * базовый класс для фрагмента с DataBinding
 */
abstract class BaseMVVMFragmentDataBinding<B : ViewDataBinding> : BaseMVVMFragment() {
    //region Binding
    lateinit var binding: B
    //endregion

    // binding.viewModel = viewModel // must be called in bindData()

    //region LifeCycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutID, container, false)
        return binding.root
    }
    //endregion
}
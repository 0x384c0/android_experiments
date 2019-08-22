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
abstract class BaseMVVMFragmentDataBinding<T:BaseViewModel,B: ViewDataBinding>:BaseMVVMFragment<T>(){
    abstract fun onGetViewModel(viewModel:T) // binding.viewModel = viewModel // must be called here

    //LifeCycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutID,container,false)
        return  binding.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onGetViewModel(viewModel)
    }

    //Binding
    lateinit var binding: B


}
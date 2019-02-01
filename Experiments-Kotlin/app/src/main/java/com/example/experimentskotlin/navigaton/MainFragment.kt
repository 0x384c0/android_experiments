package com.example.experimentskotlin.navigaton

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.experimentskotlin.R
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nextFragmentButton.setOnClickListener { onNextFragmentButtonClick(it) }
        nextActivitytButton.setOnClickListener { onNextActivityButtonClick(it) }
    }

    private fun onNextFragmentButtonClick(view: View) {
        Navigation.findNavController(view).navigate(R.id.navigationTargetFragment)
    }
    private fun onNextActivityButtonClick(view: View) {
        Navigation.findNavController(view).navigate(R.id.navigationTargetActivity)
    }
}

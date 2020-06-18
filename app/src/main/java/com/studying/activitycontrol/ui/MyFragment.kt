package com.studying.activitycontrol.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.studying.activitycontrol.R
import kotlinx.android.synthetic.main.fragment.*

class MyFragment(
    private val activ: Int,
    private val screenWidth: Int
) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        red_view.apply {
            layoutParams.width = (40 - activ) * (screenWidth.div(40))
        }
        green_view.apply {
            layoutParams.width = activ * (screenWidth.div(40))
        }
    }
}

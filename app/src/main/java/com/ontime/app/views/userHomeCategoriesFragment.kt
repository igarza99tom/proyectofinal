package com.ontime.app.views

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ontime.app.R
import com.ontime.app.viewmodels.userHomeCategoriesViewModel

class userHomeCategoriesFragment : Fragment() {

    companion object {
        fun newInstance() = userHomeCategoriesFragment()
    }

    private lateinit var viewModel: userHomeCategoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_home_categories_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(userHomeCategoriesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
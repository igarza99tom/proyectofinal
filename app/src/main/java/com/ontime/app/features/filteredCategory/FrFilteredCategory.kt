package com.ontime.app.features.filteredCategory

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ontime.app.R

class FrFilteredCategory : Fragment() {

    companion object {
        fun newInstance() = FrFilteredCategory()
    }

    private lateinit var viewModel: VmFilteredCategory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fr_filtered_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VmFilteredCategory::class.java)
        // TODO: Use the ViewModel
    }

}
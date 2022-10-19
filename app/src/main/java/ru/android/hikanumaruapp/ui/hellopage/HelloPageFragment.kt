package ru.android.hikanumaruapp.ui.hellopage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.android.hikanumaruapp.BaseFragment
import ru.android.hikanumaruapp.R

class HelloPageFragment : BaseFragment() {

    companion object {
        fun newInstance() = HelloPageFragment()
    }

    private lateinit var viewModel: HelloPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hello_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HelloPageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
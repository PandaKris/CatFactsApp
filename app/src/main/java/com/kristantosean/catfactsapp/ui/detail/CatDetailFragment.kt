package com.kristantosean.catfactsapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.kristantosean.catfactsapp.R
import com.kristantosean.catfactsapp.data.CatFact
import com.kristantosean.catfactsapp.ui.list.CatListViewModel
import com.kristantosean.catfactsapp.utils.DateFormatter
import kotlinx.android.synthetic.main.fragment_cat_detail.*
import kotlinx.android.synthetic.main.fragment_cat_list.*
import kotlinx.android.synthetic.main.fragment_cat_list.progressBar

class CatDetailFragment : Fragment() {

    val args: CatDetailFragmentArgs by navArgs()
    private val dateFormatter = DateFormatter.makeFormatter()

    private val viewModel: CatDetailViewModel by lazy {
        val activity = requireNotNull(this.activity) { "You can only access the viewModel after onActivityCreated()" }
        ViewModelProvider(this, CatDetailViewModel.Factory(activity.application, args.id)).get(CatDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cat_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.cat.observe(viewLifecycleOwner, Observer {
            it?.apply { updateView(it) }
        })

        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer {
            if (it) onNetworkError()
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            toggleLoading(it)
        })
    }

    private fun updateView(cat: CatFact) {
        catID.text = cat.id
        catText.text = cat.text
        catDate.text = cat.updatedAt?.format(dateFormatter)
        catSource.text = cat.source
    }

    private fun toggleLoading(isLoading: Boolean) {
        if (isLoading) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    private fun onNetworkError() {
        Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show()
    }
}
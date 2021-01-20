package com.kristantosean.catfactsapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.kristantosean.catfactsapp.R
import com.kristantosean.catfactsapp.data.CatFact
import com.kristantosean.catfactsapp.utils.DateFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cat_detail.*

@AndroidEntryPoint
class CatDetailFragment : Fragment() {

    val args: CatDetailFragmentArgs by navArgs()
    private val dateFormatter = DateFormatter.makeFormatter()

    private val viewModel: CatDetailViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cat_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.refreshDataFromRepository(args.id)

        viewModel.cat.observe(viewLifecycleOwner, Observer {
            it?.apply { updateView(it) }
        })

        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer {
            if (it != null) onNetworkError()
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            toggleLoading(it)
        })
    }

    private fun updateView(cat: CatFact) {
        catID.text = cat.id
        catText.text = cat.text
        catDate.text = cat.updatedAt?.format(dateFormatter)
        catSource.text = getString(R.string.source_text, cat.source)
    }

    private fun toggleLoading(isLoading: Boolean) {
        if (isLoading) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    private fun onNetworkError() {
        Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show()
    }
}
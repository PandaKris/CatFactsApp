package com.kristantosean.catfactsapp.ui.list

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kristantosean.catfactsapp.R
import com.kristantosean.catfactsapp.ui.list.adapters.CatListAdapter
import kotlinx.android.synthetic.main.fragment_cat_list.*
import kotlinx.android.synthetic.main.fragment_cat_list.view.*

class CatListFragment : Fragment() {

    private var listAdapter: CatListAdapter? = null

    private val viewModel: CatListViewModel by lazy {
        val activity = requireNotNull(this.activity) { "You can only access the viewModel after onActivityCreated()" }
        ViewModelProvider(this, CatListViewModel.Factory(activity.application)).get(CatListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listAdapter = CatListAdapter(activity as Context, View.OnClickListener {
            val id = it.tag.toString()
            findNavController().navigate(CatListFragmentDirections.actionCatListFragmentToCatDetailFragment(id))
        })
        view.catRecyclerView.layoutManager = LinearLayoutManager(activity as Context)
        view.catRecyclerView.adapter = listAdapter

        viewModel.cats.observe(viewLifecycleOwner, Observer {
            it?.apply { listAdapter?.updateList(it) }
        })

        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer {
            if (it) onNetworkError()
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            toggleLoading(it)
        })

        setHasOptionsMenu(true)
    }

    private fun onNetworkError() {
        Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show()
    }

    private fun toggleLoading(isLoading: Boolean) {
        if (isLoading) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.refreshMenu) {
            viewModel.refreshDataFromRepository()
        }
        return super.onOptionsItemSelected(item)
    }

}
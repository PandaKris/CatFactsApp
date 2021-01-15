package com.kristantosean.catfactsapp.ui.list.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kristantosean.catfactsapp.R
import com.kristantosean.catfactsapp.data.CatFact
import kotlinx.android.synthetic.main.adapter_cat_list.view.*

class CatListAdapter(private val context: Context, private val onClickListener: View.OnClickListener) : RecyclerView.Adapter<CatListAdapter.ViewHolder>() {

    private lateinit var list: List<CatFact>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_cat_list, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = context.getString(R.string.id_text, list[position].id)
        holder.description.text = list[position].text
        holder.itemLayout.tag = list[position].id
        holder.itemLayout.setOnClickListener(onClickListener)
    }

    override fun getItemCount(): Int {
        return if (::list.isInitialized) list.size else 0
    }

    fun updateList(updatedList: List<CatFact>) {
        list = updatedList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.itemTitle
        val description = view.itemDescription
        val itemLayout = view.itemView
    }
}
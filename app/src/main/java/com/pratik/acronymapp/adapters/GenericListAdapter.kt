package com.pratik.acronymapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pratik.acronymapp.databinding.ListItemBinding

/*Generic list adapter which runs on ListItemBinding
Accept higher order function as a parameter.
*/
class GenericListAdapter<T>(
    private var dataList: MutableList<T>,
    val onForeignBind: (holder: GenericListAdapter<T>.GenericViewHolder, data: T, position: Int) -> Unit
) :
    RecyclerView.Adapter<GenericListAdapter<T>.GenericViewHolder>() {

    private lateinit var mContext: Context

    inner class GenericViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        mContext = parent.context
        return GenericViewHolder(
            ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        onForeignBind(holder, dataList[position], position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun refreshList(dataList: MutableList<T>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }
}
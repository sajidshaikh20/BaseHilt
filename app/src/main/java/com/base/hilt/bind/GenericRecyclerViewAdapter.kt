package com.base.hilt.bind

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Generic Adapter for set recycler view
 * @param T Response model
 * @param D Item Layout binding class
 * @property context Context object
 * @property mArrayList ArrayList<T>? model type of list
 * @property layoutResId Int layout name
 * @constructor
 */
abstract class GenericRecyclerViewAdapter<T, D>(
    val context: Context,
    private var mArrayList: ArrayList<T>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract val layoutResId: Int

    abstract fun onBindData(model: T, position: Int, dataBinding: D)

    abstract fun onItemClick(model: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val dataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layoutResId,
            parent,
            false
        )
        return ItemViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindData(
            mArrayList!![position], holder.adapterPosition,
            dataBinding = (holder as GenericRecyclerViewAdapter<*, *>.ItemViewHolder).mDataBinding as D
        )

        (holder.mDataBinding as ViewDataBinding).root.setOnClickListener {
            onItemClick(
                mArrayList!![position],
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return mArrayList!!.size
    }

    fun addItems(arrayList: ArrayList<T>) {
        val startSize = mArrayList!!.size
        mArrayList = arrayList
        this.notifyDataSetChanged()
//        this.notifyItemRangeChanged(startSize, arrayList.size)
    }

    fun updateItem(model: T, position: Int) {
        mArrayList?.set(position, model)
        this.notifyItemChanged(position)
    }

    fun addItemsRange(arrayList: ArrayList<T>, startSize: Int) {
        this.mArrayList = arrayList
        this.notifyItemRangeChanged(startSize, arrayList.size)
    }

    fun getItem(position: Int): T {
        return mArrayList!![position]
    }

    internal inner class ItemViewHolder(binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var mDataBinding: D = binding as D
    }
}

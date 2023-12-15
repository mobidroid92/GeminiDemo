package com.bdour.gemini.common.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder(viewDataBinding: ViewBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {

    abstract fun bindData(position: Int)

}
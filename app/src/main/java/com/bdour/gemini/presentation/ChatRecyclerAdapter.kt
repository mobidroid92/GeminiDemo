package com.bdour.gemini.presentation

import android.view.ViewGroup
import androidx.core.view.isVisible
import coil.load
import com.bdour.gemini.R
import com.bdour.gemini.common.base.BaseRecyclerAdapter
import com.bdour.gemini.common.base.BaseViewHolder
import com.bdour.gemini.databinding.RecyclerItemLoadingBinding
import com.bdour.gemini.databinding.RecyclerItemMeBinding
import com.bdour.gemini.databinding.RecyclerItemOtherBinding
import com.bdour.gemini.presentation.model.MsgType.*
import com.bdour.gemini.common.toTime
import com.bdour.gemini.presentation.model.MsgModel
import com.bdour.gemini.presentation.model.MsgType

class ChatRecyclerAdapter : BaseRecyclerAdapter<MsgModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(MsgType.entries[viewType]) {
            Me -> ChatsByMeViewHolder(
                RecyclerItemMeBinding.inflate(layoutInflater, parent, false)
            )
            Loading -> LoadingViewHolder(
                RecyclerItemLoadingBinding.inflate(layoutInflater, parent, false)
            )
            Other -> ChatsByOtherViewHolder(
                RecyclerItemOtherBinding.inflate(layoutInflater, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).msgType.ordinal
    }

    private inner class ChatsByMeViewHolder(
        private val binding: RecyclerItemMeBinding
    ): BaseViewHolder(binding) {

        private var item: MsgModel? = null

        override fun bindData(position: Int) {
            item = getItem(position)
            binding.msgTextView.text = item?.text
            binding.errorTextView.isVisible = item?.isSuccess == false
            binding.timeTextView.text = item?.created?.toTime()
        }
    }

    private inner class ChatsByOtherViewHolder(
        private val binding: RecyclerItemOtherBinding
    ): BaseViewHolder(binding) {

        private var item: MsgModel? = null

        override fun bindData(position: Int) {
            item = getItem(position)
            binding.msgTextView.text = item?.text
            binding.timeTextView.text = item?.created?.toTime()
        }
    }

    private inner class LoadingViewHolder(
        private val binding: RecyclerItemLoadingBinding
    ) : BaseViewHolder(binding) {

        override fun bindData(position: Int) {
            binding.loadingImage.load(R.drawable.typing)
        }
    }
}
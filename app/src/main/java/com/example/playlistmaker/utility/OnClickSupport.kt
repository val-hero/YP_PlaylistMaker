package com.example.playlistmaker.utility

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

class OnClickSupport private constructor(private val recyclerView: RecyclerView) {

    private var onItemClickListener: OnItemClickListener? = null

    private val attachListener: RecyclerView.OnChildAttachStateChangeListener =
        object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                if (onItemClickListener != null) {
                    view.setOnClickListener(onClickListener)
                }
            }

            override fun onChildViewDetachedFromWindow(view: View) {}
        }

    init {
        this.recyclerView.setTag(R.id.on_click_support, this)
        this.recyclerView.addOnChildAttachStateChangeListener(attachListener)
    }

    companion object {
        fun addTo(view: RecyclerView): OnClickSupport {
            var support = view.getTag(R.id.on_click_support) as? OnClickSupport
            if (support == null) {
                support = OnClickSupport(view)
            }
            return support
        }

        fun removeFrom(view: RecyclerView): OnClickSupport? {
            val support = view.getTag(R.id.on_click_support) as? OnClickSupport
            support?.detach(view)
            return support
        }
    }

    private val onClickListener = View.OnClickListener { view ->
        val holder = this.recyclerView.getChildViewHolder(view)
        onItemClickListener?.onItemClick(recyclerView, holder.adapterPosition, view)
    }

    private fun detach(view: RecyclerView) {
        view.removeOnChildAttachStateChangeListener(attachListener)
        view.setTag(R.id.on_click_support, null)
    }

    fun onItemClick(listener: OnItemClickListener?): OnClickSupport {
        onItemClickListener = listener
        return this
    }

}

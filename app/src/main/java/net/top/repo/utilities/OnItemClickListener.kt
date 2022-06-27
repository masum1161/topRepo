package net.top.repo.utilities

import android.view.View

interface OnItemClickListener {
    fun onItemClick(view: View, item: Any, position: Int)
}
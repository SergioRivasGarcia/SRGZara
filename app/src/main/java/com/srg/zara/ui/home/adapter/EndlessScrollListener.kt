package com.srg.zara.ui.home.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener : RecyclerView.OnScrollListener() {
    private var previousTotalItems = 0
    private var loading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (loading && totalItemCount > previousTotalItems) {
            loading = false
            previousTotalItems = totalItemCount
        }

        val visibleThreshold = 5
        if (!loading && (totalItemCount - visibleItemCount <= firstVisibleItemPosition + visibleThreshold)) {
            onLoadMore()
            loading = true
        }
    }

    abstract fun onLoadMore()
}
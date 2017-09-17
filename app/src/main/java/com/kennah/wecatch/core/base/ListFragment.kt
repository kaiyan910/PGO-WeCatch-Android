package com.kennah.wecatch.core.base

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import butterknife.BindView
import com.kennah.wecatch.R
import com.kennah.wecatch.core.utils.LogUtils

abstract class ListFragment<T, V>: BaseFragment(),
        BaseListView<T>, 
        SwipeRefreshLayout.OnRefreshListener where T: Any, V: BaseHolder<T> {

    @BindView(R.id.refresh_layout)
    protected lateinit var mRefreshLayout: SwipeRefreshLayout
    @BindView(R.id.list)
    protected lateinit var mRecyclerView: RecyclerView
    @BindView(R.id.empty_data)
    protected lateinit var mTextEmptyData: TextView

    private var mInitData: Boolean = false

    override fun afterViews(savedInstanceState: Bundle?) {
        mRecyclerView.apply {
            adapter = getListAdapter()
            layoutManager = layoutManager()
        }
        mRefreshLayout.isEnabled = enableRefresh()
        mRefreshLayout.setOnRefreshListener(this)
        LogUtils.debug(this, "afterViews()")
    }

    override fun layout(): Int = R.layout.fragment_list

    override fun showLoading() {
        mTextEmptyData.visibility = View.GONE
        mRefreshLayout.isRefreshing = true
    }

    override fun showData() {
        mTextEmptyData.visibility = View.GONE
        mRefreshLayout.isRefreshing = false
    }

    override fun showEmpty() {
        mTextEmptyData.visibility = View.VISIBLE
        mRefreshLayout.isRefreshing = false
    }

    override fun update(data: List<T>) {

        when {
            data.isEmpty() -> showEmpty()
            else -> {
                getListAdapter().setData(data)
                showData()
            }
        }
    }

    override fun onError(errorCode: Int) {
    }

    override fun onRefresh() {
        showData()
    }

    override fun setData(data: List<T>) {

        if (mInitData) {
            update(data)
            return
        }

        when {
            data.isNotEmpty() -> {
                LogUtils.debug(this, "not empty data.")
                getListAdapter().setData(data)
                showData()
            }
            else -> {
                showEmpty()
            }
        }

        mInitData = true
    }

    open fun layoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(activity)

    open fun setEmptyData(text: String) = {
        mTextEmptyData.text = text
    }

    open fun enableRefresh(): Boolean = true

    abstract fun getListAdapter(): BaseAdapter<T, V>
}
package com.sp.dangdang.view

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration

import com.sp.dangdang.R

/**
 * Created by Administrator on 2017/6/24.
 */

class SwipeRefreshView(context: Context, attrs: AttributeSet) : SwipeRefreshLayout(context, attrs) {

    private val mScaledTouchSlop: Int

    private val mFooterView: View

    private var recyclerview: RecyclerView? = null

    @Volatile private var condition2: Boolean = false

    private var MonLoadListener:()->Unit={}
    /**
     * 正在加载状态
     */
    private var isLoading: Boolean = false

    internal var layoutInflater: LayoutInflater

    init {
        // 填充底部加载布局
        mFooterView = View.inflate(context, R.layout.view_footer, null)
        // 表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
        mScaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        layoutInflater = LayoutInflater.from(context)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        // recyclerview,设置recyclerview的布局位置
        if (recyclerview == null) {
            // 判断容器有多少个孩子
            if (childCount > 0) {
                // 判断第一个孩子是不是ListView
                if (getChildAt(0) is RecyclerView) {
                    // 创建ListView对象
                    recyclerview = getChildAt(0) as RecyclerView

                    // 设置ListView的滑动监听
                    setRecyclerViewOnScroll()
                }
            }
        }
    }

    /**
     * 在分发事件的时候处理子控件的触摸事件

     * @param ev
     * *
     * @return
     */
    private var mDownY: Float = 0.toFloat()
    private var mUpY: Float = 0.toFloat()

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        when (ev.action) {
            MotionEvent.ACTION_DOWN ->
                // 移动的起点
                mDownY = ev.y
            MotionEvent.ACTION_MOVE ->
                // 移动过程中判断时候能下拉加载更多
                if (condition2 && canLoadMore()) {
                    // 加载数据
                    loadData()
                    return false
                }
            MotionEvent.ACTION_UP ->
                // 移动的终点
                mUpY = y
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 判断是否满足加载更多条件

     * @return
     */
    private fun canLoadMore(): Boolean {
        // 1. 是上拉状态
        val condition1 = mDownY - mUpY >= mScaledTouchSlop
        if (condition1) {
            println("是上拉状态")
        }

        // 2. 当前页面可见的item是最后一个条目
        /*condition2 = false;
        if (recyclerview != null && recyclerview.getAdapter() != null) {
            recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    RecyclerView.LayoutManager layoutManager = recyclerview.getLayoutManager();
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    if (recyclerview.getAdapter().getItemCount()-1==lastItemPosition){
                        condition2=true;
                    }
                    LogUtil.log(TAG,"move"+lastItemPosition+"/"+recyclerview.getAdapter().getItemCount()+condition2);
                }
            });
        }

        if (condition2) {
            System.out.println("是最后一个条目");
        }*/
        // 3. 正在加载状态
        val condition3 = !isLoading
        if (condition3) {
            println("不是正在加载状态")
        }
        return condition1 && condition3
    }

    /**
     * 处理加载数据的逻辑
     */
    private fun loadData() {
        println("加载数据...")
        if (MonLoadListener != null) {
            // 设置加载状态，让布局显示出来
            setLoading(true)
            MonLoadListener()
        }
    }

    /**
     * 设置加载状态，是否加载传入boolean值进行判断

     * @param loading
     */
    fun setLoading(loading: Boolean) {
        // 修改当前的状态
        isLoading = loading
        /*if (isLoading) {
            // 显示布局
            View view=layoutInflater.inflate(R.layout.view_footer,null);
            recyclerview.addView(view);
        } else {
            // 隐藏布局
            recyclerview.removeViewAt(recyclerview.getAdapter().getItemCount()-1);

            // 重置滑动的坐标
            mDownY = 0;
            mUpY = 0;
        }*/
    }


    /**
     * 设置RecyclerView的滑动监听
     */
    private fun setRecyclerViewOnScroll() {

        recyclerview!!.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val layoutManager = recyclerview!!.layoutManager
            val linearManager = layoutManager as LinearLayoutManager
            val lastItemPosition = linearManager.findLastVisibleItemPosition()
            if (recyclerview!!.adapter.itemCount - 1 == lastItemPosition) {
                condition2 = true
            } else {
                condition2 = false
            }
        }
    }

    fun setCondition2(isLoading: Boolean) {
        condition2 = isLoading
    }


    /**
     * 上拉加载的接口回调
     */


    fun setMOnLoadListener(listener:()->Unit){
        MonLoadListener=listener
    }

    companion object {

        private val TAG = "SwipeRefreshView"
    }
}

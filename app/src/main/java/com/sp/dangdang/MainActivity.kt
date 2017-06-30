package com.sp.dangdang

import android.content.Context
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import org.jetbrains.anko.toast
import com.sp.dangdang.adapter.MainAdapter
import com.sp.dangdang.listener.IMainActivity
import com.sp.dangdang.model.Bookitem
import com.sp.dangdang.presenter.Mainpresenter
import com.sp.dangdang.view.SwipeRefreshView

import java.util.ArrayList
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.UI
import org.jetbrains.anko.onUiThread
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity(), IMainActivity {

    private val mainpresenter = Mainpresenter(this)

    private var adapter: MainAdapter? = null

    private val bookitems = ArrayList<Bookitem>()

    private var bookname = ""

    private var currentpage = 1

    private var snackbar: Snackbar? = null

    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        init()
    }

    private fun init() {
        snackbar = Snackbar.make(rootview, "正在加载更多...", BaseTransientBottomBar.LENGTH_INDEFINITE)
        adapter = MainAdapter(this, bookitems){
            //detail-> if(detail.length>0) toast(detail)
            bookitem->startActivity<DetailActivity>("DETAILURL" to bookitem.detailurl,
                "DETAIL" to bookitem.detail,
                "IMGURL" to bookitem.imgurl)
        }
        val linearLayoutManager = LinearLayoutManager(this)
        recy_list!!.layoutManager = linearLayoutManager
        recy_list!!.adapter = adapter
        to_search!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                bookname = query
                mainpresenter.getBookList(query, currentpage)
                to_search!!.setIconifiedByDefault(false)
                to_search!!.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        title_tv!!.setOnClickListener { recy_list!!.smoothScrollToPosition(0) }

        //加载更多监听器

        swipe_loadmore.setMOnLoadListener {
            snackbar!!.show()
            mainpresenter.getMoreList(bookname, currentpage++)
        }

        swipe_loadmore.setOnRefreshListener(object :SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                mainpresenter.getBookList(bookname,1)
            }
        })
    }

    override fun showProgressBar() {
        onUiThread { progress_bar!!.visibility = View.VISIBLE }
    }
    override fun hideProgressBar() {
        onUiThread{ progress_bar!!.visibility = View.GONE }
    }

    override fun updateUI(list: ArrayList<Bookitem>) {
        onUiThread { swipe_loadmore.isRefreshing=false
            bookitems.clear()
            bookitems.addAll(list)
            adapter!!.notifyDataSetChanged() }
    }

    override fun addMore(list: ArrayList<Bookitem>) {
        onUiThread{
            swipe_loadmore.isRefreshing=false
            swipe_loadmore!!.setCondition2(false)
            snackbar!!.dismiss()
            bookitems.addAll(list)
            adapter!!.notifyDataSetChanged()
            swipe_loadmore!!.setLoading(false)
        }
    }

    companion object {
        private val TAG = "MainActivity"
    }
}

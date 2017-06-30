package com.sp.dangdang.presenter

import com.sp.dangdang.biz.MainBiz
import com.sp.dangdang.listener.IMainActivity
import com.sp.dangdang.model.Bookitem

import java.util.ArrayList

/**
 * Created by Administrator on 2017/6/23.
 */

class Mainpresenter(var iMainActivity: IMainActivity) {

    var mainBiz: MainBiz

    init {
        this.mainBiz = MainBiz(this)
    }

    fun getBookList(name: String, page: Int) {
        mainBiz.getBookListByNameAndPage(name, page, false)
    }

    fun getMoreList(name: String, page: Int) {
        mainBiz.getBookListByNameAndPage(name, page, true)
    }

    fun addMoreList(list: ArrayList<Bookitem>) {
        iMainActivity.addMore(list)
    }

    fun updateUI(list: ArrayList<Bookitem>) {
        iMainActivity.updateUI(list,true)
    }

    fun showPB() {
        iMainActivity.showProgressBar()
    }

    fun hidePB() {
        iMainActivity.hideProgressBar()
    }
}

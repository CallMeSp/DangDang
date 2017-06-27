package com.sp.dangdang.listener

import com.sp.dangdang.model.Bookitem

import java.util.ArrayList

/**
 * Created by Administrator on 2017/6/23.
 */

interface IMainActivity {
    fun showProgressBar()
    fun hideProgressBar()
    fun updateUI(list: ArrayList<Bookitem>)
    fun addMore(list: ArrayList<Bookitem>)
}

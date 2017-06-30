package com.sp.dangdang.presenter

import com.sp.dangdang.biz.DetailBiz
import com.sp.dangdang.listener.IDetailActivity

/**
 * Created by Administrator on 2017/6/29.
 */
class Detailpresenter(var iDetailActivity: IDetailActivity) {

    var detailBiz:DetailBiz

    init {
        detailBiz= DetailBiz(this)
    }

    fun getDetailByUrl(url:String){
        detailBiz.getDetail(url)
    }

    fun setProContent(content:String){
        iDetailActivity.setProContent(content)
    }
}
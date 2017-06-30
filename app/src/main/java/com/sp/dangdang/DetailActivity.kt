package com.sp.dangdang

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.sp.dangdang.listener.IDetailActivity
import com.sp.dangdang.presenter.Detailpresenter
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.onUiThread

class DetailActivity : Activity() ,IDetailActivity{

    private var detailurl:String=""

    private var detail:String=""

    private var imgurl:String=""

    companion object {
        val TAG:String="DetailActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        init()
    }
    fun init(){
        val detailpresenter:Detailpresenter=Detailpresenter(this)
        detailurl=intent.getStringExtra("DETAILURL")
        detail=intent.getStringExtra("DETAIL")
        imgurl=intent.getStringExtra("IMGURL")
        if(detail.length>0){
            card_detail.visibility=View.VISIBLE
        }
        Log.e(TAG,detailurl)
        detailpresenter.getDetailByUrl(detailurl)
        txt_detail.text=detail
        Glide.with(this)
                .load(imgurl)
                .into(img_bookcover)
    }

    override fun setProContent(content: String) {
        onUiThread {
            if(content.length>0){
                card_content.visibility=View.VISIBLE
                txt_procontent.text=content
            } }
    }
}

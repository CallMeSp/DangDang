package com.sp.dangdang.biz

import android.util.Log
/*import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage*/
import com.sp.dangdang.presenter.Detailpresenter

import java.util.ArrayList

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.nodes.Element


/**
 * Created by Administrator on 2017/6/29.
 */
class DetailBiz(private val detailpresenter: Detailpresenter) {
    companion object {
        val TAG:String="DetailBiz"
    }
    fun getDetail(url:String){
        Observable.just(url)
                .observeOn(Schedulers.newThread())
                .subscribe(object :DisposableObserver<String>(){
                    override fun onError(e: Throwable?) {

                    }

                    override fun onComplete() {

                    }

                    override fun onNext(t: String?) {
                        val detailurl = url
                        Log.e(TAG, "url?:" + t)
                        /*val wc = WebClient(BrowserVersion.CHROME)
                        wc.options.isUseInsecureSSL = true
                        wc.options.isJavaScriptEnabled = true // 启用JS解释器，默认为true
                        wc.options.isCssEnabled = false // 禁用css支持
                        wc.options.isThrowExceptionOnScriptError = false // js运行错误时，是否抛出异常
                        wc.options.timeout = 100000 // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待
                        wc.options.isDoNotTrackEnabled = false
                        val page = wc.getPage<HtmlPage>("http://localhost:8080/strurts2fileupload/main.html")
                        val pageXml = page.asXml() //以xml的形式获取响应文本
                        val doc2= Jsoup.parse(pageXml)*/
                        val doc = Jsoup.connect(detailurl).get()
                        Log.e("??", doc.toString())
                        var elements_content = doc.select("div.t_box").select("div.t_box_left")
                        var procontent = doc.select("div.t_box").select("div.t_box_left").select("div.pro_content").select("ul").select("li")
                        var contents = doc.select("div.section")
                        var catalog = doc.select("div.section")

                        var stb:StringBuffer= StringBuffer()
                        //procontent加入换行符
                        for (str:Element in procontent){
                            stb.append(str.text())
                            stb.append("\n")
                        }

                        detailpresenter.setProContent(stb.toString())
                        Log.e("procontent",stb.toString())
                        //Log.e("elements", elements_content.toString())
                        //Log.e("contents:", contents.toString())
                        //Log.e("catalog:", catalog.toString())
                    }
                })

    }
}
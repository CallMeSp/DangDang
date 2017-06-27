package com.sp.dangdang.biz

import com.sp.dangdang.model.Bookitem
import com.sp.dangdang.presenter.Mainpresenter

import org.jsoup.Jsoup

import java.util.ArrayList

import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by Administrator on 2017/6/23.
 */

class MainBiz(private val mainpresenter: Mainpresenter) {
    fun getBookListByNameAndPage(bookname: String, page: Int, ismore: Boolean) {
        Observable.just(bookname)
                .observeOn(Schedulers.newThread())
                .map { s ->
                    mainpresenter.showPB()
                    val bookitemArrayList = ArrayList<Bookitem>()
                    val doc = Jsoup.connect("http://search.dangdang.com/?key=$s&act=input&page_index=$page").get()
                    val elements = doc.select("ul.bigimg").select("li")
                    for (element in elements) {
                        val name = element.select("a").attr("title")
                        val detailurl = element.select("a").attr("href")
                        val imgurl = if (!element.select("a").select("img").attr("src").startsWith("http"))
                            element.select("a").select("img").attr("data-original")
                        else
                            element.select("a").select("img").attr("src")
                        val detail = element.select("p.detail").text()
                        val price = element.select("p.price").select("span.search_now_price").text()
                        val author = element.select("p.search_book_author").select("span")[0].select("a").attr("title")
                        val publisher = element.select("p.search_book_author").select("span")[2].select("a").text()
                        val bookitem = Bookitem(name ,detail,price,author,imgurl,publisher)
                        bookitemArrayList.add(bookitem)
                    }
                    bookitemArrayList
                }
                .subscribe(object : DisposableObserver<ArrayList<Bookitem>>() {
                    override fun onNext(bookitems: ArrayList<Bookitem>) {
                        if (!ismore) {
                            mainpresenter.updateUI(bookitems)
                        } else {
                            mainpresenter.addMoreList(bookitems)
                        }
                    }

                    override fun onError(e: Throwable) {
                        mainpresenter.updateUI(ArrayList<Bookitem>())
                        mainpresenter.hidePB()
                    }

                    override fun onComplete() {
                        mainpresenter.hidePB()
                    }
                })
    }

    companion object {
        private val TAG = "MainBiz"
    }
}

package com.sp.dangdang.model

/**
 * Created by Administrator on 2017/6/23.
 */

class Bookitem (var map:MutableMap<String,Any?>){
    var name: String by map

    var detail: String by map

    var price: String by map

    var author: String by map

    var imgurl: String by map

    var publisher: String by map

    var detailurl:String by map
    constructor(name :String,detail:String,price:String,author:String,imgurl:String,publisher:String,detailurl:String):this(HashMap()){
        this.name=name
        this.detail=detail
        this.price=price
        this.author=author
        this.imgurl=imgurl
        this.publisher=publisher
        this.detailurl=detailurl
    }
}

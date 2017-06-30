package com.sp.dangdang.utils

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.sp.dangdang.model.Bookitem
import org.jetbrains.anko.db.*

/**
 * Created by Administrator on 2017/6/30.
 */
class DbHelper(context:Context):ManagedSQLiteOpenHelper(context, DB_NAME,null, DB_VERSION){
    val mcontext=context
    @Volatile var TB_NAME:String=""
    companion object {
        val DB_NAME="history.db"
        val DB_VERSION=1
        val TAG="DbHelper"
        //val instance by lazy{ DbHelper()}
    }
    override fun onCreate(db: SQLiteDatabase?) {
        Log.e(TAG,"onCreate")
    }

    fun insertNewTable(tbname:String,booklist:ArrayList<Bookitem>){
        DbHelper(mcontext).use {
            /*createTable(tbname,true,"id" to INTEGER+ PRIMARY_KEY+ AUTOINCREMENT,
                    "bookname" to TEXT,
                    "price" to TEXT,
                    "author" to TEXT,
                    "publisher" to TEXT,
                    "detail" to TEXT,
                    "imgurl" to TEXT,
                    "detailurl" to TEXT)*/
            execSQL("create table if not exists "+tbname+"(it INTEGER primary key AUTOINCREMENT, " +
                    "name text," +
                    "detail text," +
                    "price text," +
                    "author text,"
                    +"imgurl text," +
                    "publisher text," +
                    "detailurl text)")
            Log.e(TAG,"create table:"+tbname)
            booklist.forEach { insert(tbname,
                    "name" to it.name,
                    "price" to it.price,
                    "author" to it.author,
                    "publisher" to it.author,
                    "detail" to it.detail,
                    "imgurl" to it.imgurl,
                    "detailurl" to it.detailurl)
                Log.e(TAG,"insert into table:"+tbname+": "+it.name)}
        }
    }
    fun addToOldTable(tbname:String,booklist:ArrayList<Bookitem>) {
        DbHelper(mcontext).use {
            booklist.forEach {
                insert(tbname,
                        "name" to it.name,
                        "price" to it.price,
                        "author" to it.author,
                        "publisher" to it.author,
                        "detail" to it.detail,
                        "imgurl" to it.imgurl,
                        "detailurl" to it.detailurl)
                Log.e(TAG,"insert into table:"+tbname+": "+it.name)
            }
        }
    }

    fun getListFormDb(bookname:String)= DbHelper(mcontext).use {
        select(bookname).parseList {
            Bookitem(HashMap(it))
        }
    }

    fun dropTable(bookname: String)=DbHelper(mcontext).use {
        clear(bookname)
    }
    fun IshaveTB(bookname:String):Boolean=DbHelper(mcontext).use {
        var result:Boolean=false
        var cursor=rawQuery("select count(*) as c from sqlite_master where type ='table' and name ="+"\'"+bookname+"\';",null)
        if (cursor.moveToNext()){
            var count:Int=cursor.getInt(0)
            if (count>0){
                result=true
            }
        }
        result
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.dropTable(TB_NAME,true)
        onCreate(db)
    }
    fun <T : Any> SelectQueryBuilder.parseList(parser: (Map<String, Any?>) -> T): List<T> =
            parseList(object : MapRowParser<T> {
                override fun parseRow(columns: Map<String, Any?>): T = parser(columns)
            })
    fun <T : Any> SelectQueryBuilder.parseOpt(parser: (Map<String, Any?>) -> T): T? =
            parseOpt(object : MapRowParser<T> {
                override fun parseRow(columns: Map<String, Any?>): T = parser(columns)
            })
    fun SQLiteDatabase.clear(tableName: String) {
        execSQL("delete from $tableName")
    }
}
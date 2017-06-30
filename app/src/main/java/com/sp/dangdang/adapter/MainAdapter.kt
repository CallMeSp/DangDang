package com.sp.dangdang.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.sp.dangdang.R
import com.sp.dangdang.model.Bookitem

import java.util.ArrayList

/**
 * Created by Administrator on 2017/6/23.
 */

class MainAdapter(private val context: Context, list: ArrayList<Bookitem>,val itemclickListener:(Bookitem)->Unit) : RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_name: TextView
        var txt_author: TextView
        var txt_price: TextView
        var imageView: ImageView

        init {
            txt_name = itemView.findViewById(R.id.name) as TextView
            txt_author = itemView.findViewById(R.id.author) as TextView
            txt_price = itemView.findViewById(R.id.price) as TextView
            imageView = itemView.findViewById(R.id.book_cover) as ImageView
        }
    }

    private var bookitemArrayList = ArrayList<Bookitem>()

    private val inflater: LayoutInflater


    init {
        this.bookitemArrayList = list
        inflater = LayoutInflater.from(context)
    }

    override fun getItemCount(): Int {
        return bookitemArrayList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = inflater.inflate(R.layout.bookitem, parent, false)
        val viewHolder = MyViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txt_name.text = bookitemArrayList[position].name
        holder.txt_author.text = bookitemArrayList[position].author
        holder.txt_price.text = bookitemArrayList[position].price
        Glide.with(context)
                .load(bookitemArrayList[position].imgurl)
                .into(holder.imageView)
        holder.itemView.setOnClickListener { itemclickListener(bookitemArrayList[position]) }
    }
}

package com.example.wallpaper

import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

//import kotlinx.android.synthetic.main.itemview.view.*

class adapter:RecyclerView.Adapter<adapter.CustomViewHolder>()
{
    var editor=ArrayList<Photo>()

    class CustomViewHolder(itemview:View): RecyclerView.ViewHolder(itemview){
        var editorimage=itemview.findViewById<ImageView>(R.id.editorimage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemview=LayoutInflater.from(parent.context).inflate(R.layout.editor_item,parent,false)
        return  CustomViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val s=(editor[position]).src.large2x
//        Glide.with(context).load(s).into(holder.editorimage);
        Picasso.get()
            .load(s).placeholder(R.drawable.ic_launcher_backgorund)
            .into(holder.editorimage);


    }

    override fun getItemCount(): Int {
        return editor.size

    }
    fun seteditor(s:ArrayList<Photo>){
        this.editor=s
    }

}
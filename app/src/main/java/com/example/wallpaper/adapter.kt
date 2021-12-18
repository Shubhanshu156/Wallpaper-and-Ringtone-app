package com.example.wallpaper

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.editor_item.view.*

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
        val s=(editor[position]).src.medium
        val s2=(editor[position]).src.original
        holder.itemView.setOnClickListener {
            val intent= Intent(holder.itemView.context,wallpaper_activity::class.java)
            intent.putExtra("url",s2)
            holder.itemView.context.startActivity(intent)
        }
//                Glide
//            .with(holder.itemView.context)
//            .load(s).fit()
//            .centerCrop()
//            .placeholder(R.drawable.ic_launcher_backgorund)
//            .into(holder.itemView.editorimage);

        Picasso.get().load(s).fit()
            .placeholder(R.drawable.ic_launcher_backgorund).centerCrop()
            .into(holder.itemView.editorimage)


    }

    override fun getItemCount(): Int {
        return editor.size

    }
    fun seteditor(s:ArrayList<Photo>){
        this.editor=s
    }

}
package com.example.wallpaper

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class categoryadapter(private val mList: ArrayList<ArrayList<categoryclass>>) : RecyclerView.Adapter<categoryadapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val txt=mList[position][0].categoryname
        val txt2=mList[position][1].categoryname
        val url1=mList[position][0].categoryurl
        val url2=mList[position][1].categoryurl


//        Glide.with(context).load(s).into(holder.editorimage);
        Picasso.get()
            .load(url1).placeholder(R.drawable.ic_launcher_backgorund)
            .into(holder.cat1);

        Picasso.get()
            .load(url2).placeholder(R.drawable.ic_launcher_backgorund)
            .into(holder.cat2);
        val ItemsViewModel = mList[position]

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val cat1: ImageView = itemView.findViewById(R.id.cat1)
        val cat2: ImageView = itemView.findViewById(R.id.cat2)

    }
}



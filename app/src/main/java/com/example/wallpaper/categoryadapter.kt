package com.example.wallpaper

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wallpaper.categoryclass
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_item.view.*
import kotlinx.android.synthetic.main.editor_item.view.*
import kotlinx.android.synthetic.main.fragment_homefragment.view.*
import kotlinx.android.synthetic.main.fragment_homefragment.view.cat

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
        Glide
            .with(holder.itemView.context)
            .load(url1)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_backgorund)
            .into(holder.itemView.cat1);
        Glide
            .with(holder.itemView.context)
            .load(url2)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_backgorund)
            .into(holder.itemView.cat2);
        holder.cat1namw.text=txt
        holder.cat2namw.text=txt2
        holder.itemView.rlt1.setOnClickListener {
            val q="https://api.pexels.com/v1/search?query="+txt
            val intent= Intent(holder.itemView.context,wallpaper_result::class.java)
            intent.putExtra("url",q)
            holder.itemView.context.startActivity(intent)

        }
        holder.itemView.rlt2.setOnClickListener {
            val q="https://api.pexels.com/v1/search?query="+txt2
            val intent= Intent(holder.itemView.context,wallpaper_result::class.java)
            intent.putExtra("url",q)
            holder.itemView.context.startActivity(intent)

        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val cat1: ImageView = itemView.findViewById(R.id.cat1)
        val cat2: ImageView = itemView.findViewById(R.id.cat2)
        val cat1namw:TextView=itemView.findViewById(R.id.cat1name)
        val cat2namw:TextView=itemView.findViewById(R.id.cat2name)
        val rlt1:RelativeLayout=itemView.findViewById(R.id.rlt1)
        val rlt2:RelativeLayout=itemView.findViewById(R.id.rlt2)


    }
}



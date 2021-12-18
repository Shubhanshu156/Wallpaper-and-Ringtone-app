package com.example.wallpaper
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.wallpaper.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_wallpaper.*
import kotlinx.android.synthetic.main.category_item.view.*
import android.os.Bundle
import com.example.wallpaper.wallpaper_activity


class favadapter : RecyclerView.Adapter<favadapter.ViewHolder>() {

    // create new views
    var mList=ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.res_sample, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class

        Glide
            .with(holder.itemView.context)
            .load(ItemsViewModel)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_backgorund)

            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
//                    Toast.makeText(this@wallpaper, "Unable to fetch wallpaper try again later", Toast.LENGTH_SHORT).show()
                    Toast.makeText(holder.itemView.context, "Unable to fetch wallpaper try again later", Toast.LENGTH_SHORT).show()
                    return false
                }
                override fun onResourceReady(p0: Drawable?, p1: Any?, p2: Target<Drawable>?, p3: DataSource?, p4: Boolean): Boolean {



                    //do something when picture already loaded
                    return false
                }
            }) .into(holder.imageView)
//        Picasso.get().load(ItemsViewModel.src.portrait).centerCrop().into(holder.imageView)



        holder.imageView.setOnClickListener{
            val intent= Intent(holder.itemView.context, wallpaper_activity::class.java)
            val bundle = Bundle()
//            bundle.putString("url2",ItemsViewModel.src.medium)
            intent.putExtra("url",ItemsViewModel)
//            Toast.makeText(holder.itemView.context, "url", Toast.LENGTH_SHORT).show()


            holder.itemView.context.startActivity(intent)
        }


    }
    fun setresult(s:ArrayList<String>){
        this.mList=s
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.resultimage)


    }
}

package com.example.wallpaper
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import android.os.Bundle


class resultadapter : RecyclerView.Adapter<resultadapter.ViewHolder>() {

    // create new views
    var mList=ArrayList<Photo>()
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
            .load(ItemsViewModel.src.portrait)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_backgorund)

            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(p0: GlideException?, p1: Any?, p2: Target<Drawable>?, p3: Boolean): Boolean {
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
            intent.putExtra("url",ItemsViewModel.src.portrait)
//
            holder.itemView.context.startActivity(intent)
        }


    }
    fun setresult(s:ArrayList<Photo>){
        this.mList=s
    }
    fun addresult(s:ArrayList<Photo>){
        this.mList.addAll(s)
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

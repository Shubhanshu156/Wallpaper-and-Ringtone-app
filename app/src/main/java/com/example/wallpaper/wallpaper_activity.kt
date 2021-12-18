package com.example.wallpaper
import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Bitmap

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import kotlinx.android.synthetic.main.activity_wallpaper.*
import kotlinx.android.synthetic.main.editor_item.view.*
import com.bumptech.glide.request.target.Target

import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception
import com.google.firebase.firestore.QueryDocumentSnapshot

import com.google.firebase.firestore.QuerySnapshot

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener



class wallpaper_activity : AppCompatActivity() {
    private lateinit var url: String
    val TAG="HELLO"
    val db = FirebaseFirestore.getInstance()
    val f = FirebaseAuth.getInstance()
    val user = f.uid
    val notres = db.collection("fav").document("fav")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallpaper)
        val TAG = "HELLO"
        val button=findViewById<Button>(R.id.applybtn)


        val KEY = user

        val dbref = FirebaseDatabase.getInstance().reference
        val name = f.uid

        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val intent = intent
        val id = intent.getStringExtra("url")
        url = intent.getStringExtra("url")!!
        val VALUE = url
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val wallpapermanager = WallpaperManager.getInstance(applicationContext)
        val height: Int = metrics.heightPixels
        val width: Int = metrics.widthPixels
        val imageid = url.subSequence(32, 41)

        wallpapermanager.setWallpaperOffsetSteps(1F, 1F)
        wallpapermanager.suggestDesiredDimensions(width, height)

        Glide.with(this)
            .load(id).fitCenter()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    p0: GlideException?,
                    p1: Any?,
                    p2: Target<Drawable>?,
                    p3: Boolean
                ): Boolean {
//                    Toast.makeText(this@wallpaper, "Unable to fetch wallpaper try again later", Toast.LENGTH_SHORT).show()
                    Toast.makeText(
                        this@wallpaper_activity,
                        "Unable to fetch wallpaper try again later",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }

                override fun onResourceReady(
                    p0: Drawable?,
                    p1: Any?,
                    p2: Target<Drawable>?,
                    p3: DataSource?,
                    p4: Boolean
                ): Boolean {
                    prgrs.visibility = View.GONE


                    //do something when picture already loaded
                    return false
                }
            })
            .into(mnwalpaper)

        var answer=ArrayList<String> ()
        notres.get().addOnSuccessListener {
            try{
            val ans = it.getData() as HashMap<String, ArrayList<String>>
            answer.addAll(ans.getValue(user!!))
            if (answer.contains(url)){
                fav.isChecked=true
            }
        }
        catch (e:Exception){}}






        button.setOnClickListener {
            applying.visibility=View.VISIBLE

            try {
                val b=mnwalpaper.drawable.toBitmap()
                val bitmap = Bitmap.createScaledBitmap(b, width, height, true)
                wallpapermanager.setBitmap(bitmap)
            }
            catch (e:Exception){
                Toast.makeText(this, "Unable to set Wallpaper"+e.message, Toast.LENGTH_SHORT).show()
            }
            applying.visibility=View.GONE
            Toast.makeText(this@wallpaper_activity, "wallpaper added", Toast.LENGTH_SHORT).show()


        }

fav.setOnClickListener{
    val isChecked=fav.isChecked

    var c=HashMap<String,ArrayList<String>>()
    if (isChecked){
        Log.d(TAG, "check again")
        answer.add(url)
        c.put(user!!,answer)
        notres.set(c).addOnSuccessListener {"Suceesfully added to fav"

        }
    }
    else{
        if (answer.contains(url)){
            answer.remove(url)
            c.put(user!!,answer)
            notres.set(c).addOnSuccessListener {"Suceesfully added to fav"
                Log.d(TAG, "successfully added")
            }
//
//                .addOnFailureListener {
//                    Log.d(TAG, "successfully added")
//                    Toast.makeText(
//                        this,
//                        "Added Successfully",
//                        Toast.LENGTH_SHORT
//                    ).show()}
//
//        }
//    }

}







    }

    fun setdata(s:ArrayList<String>){
        val v=HashMap<String,ArrayList<String>>()
        v[user!!]=s


    }


}
    }
    fun  DownloadWallpaperEvent(view:View) {
        applying.visibility=View.VISIBLE
        try{

            val downloadManager:DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            val  uri:Uri = Uri.parse(url)
            val request:DownloadManager.Request =  DownloadManager.Request(uri)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            downloadManager.enqueue(request)
            Toast.makeText(this, "Downloading Start", Toast.LENGTH_SHORT).show()
            applying.visibility=View.GONE}
        catch (e:Exception){
            Toast.makeText(this, "Just a second", Toast.LENGTH_SHORT).show()
        }
    }
}

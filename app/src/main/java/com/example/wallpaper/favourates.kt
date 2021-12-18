package com.example.wallpaper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_favourates.*
import kotlinx.android.synthetic.main.activity_wallpaper.*
import java.lang.Exception

class favourates : AppCompatActivity() {
    val TAG="HELLOHOW"
    val adp=favadapter()

    val uid= FirebaseAuth.getInstance().uid
    val db = FirebaseFirestore.getInstance()
    val notres = db.collection("fav").document("fav")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourates)

        val TAG = "HELLOHOW"
        val favlst = findViewById<RecyclerView>(R.id.favlst)

        val g = GridLayoutManager(this, 2)
        favlst.setHasFixedSize(true)
        favlst.setItemViewCacheSize(20)
        favlst.setDrawingCacheEnabled(true)
        favlst.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
        favlst.layoutManager = g
        favlst.adapter = adp
        var ans = HashMap<String, ArrayList<String>>()
        var s = ArrayList<String>()
        Log.d(TAG, "enter team")

        notres.get().addOnSuccessListener {

            try {


                var ans = it.getData() as HashMap<String, ArrayList<String>>
                val s = ArrayList<String>()
                s.addAll(ans.getValue(uid!!))
                val hashSet: HashSet<String> = HashSet(s)
                val flower_array: ArrayList<String> = ArrayList(hashSet)
                Log.d(TAG, "onCreate: ${flower_array.size}")
                if (flower_array.size>0){

                    favload.visibility = View.GONE
                }
                if (flower_array.size == 0) {
                    favload.visibility = View.GONE
                    nofav.visibility = View.VISIBLE
                }
                adp.setresult(flower_array)
                adp.notifyDataSetChanged()
            } catch (e: Exception) {
            }
        }
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
        notres.get().addOnSuccessListener {

            try {


                var ans = it.getData() as HashMap<String, ArrayList<String>>
                val s = ArrayList<String>()
                s.addAll(ans.getValue(uid!!))
                val hashSet: HashSet<String> = HashSet(s)
                val flower_array: ArrayList<String> = ArrayList(hashSet)
                Log.d(TAG, "onCreate: ${flower_array.size}")
                if (flower_array.size == 0) {
                    nofav.visibility = View.VISIBLE
                    favload.visibility = View.GONE
                }
                adp.setresult(flower_array)
                adp.notifyDataSetChanged()
            } catch (e: Exception) {
            }
        }

    }override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")

        notres.get().addOnSuccessListener {

            try{



            var ans = it.getData() as HashMap<String, ArrayList<String>>
            val s=ArrayList<String>()
            s.addAll(ans.getValue(uid!!))
            val hashSet: HashSet<String> = HashSet(s)
            val flower_array: ArrayList<String> = ArrayList(hashSet)
            Log.d(TAG, "onCreate: ${flower_array.size}")
            if (flower_array.size==0){
                nofav.visibility=View.VISIBLE
                favload.visibility=View.GONE
            }
            adp.setresult(flower_array)
            adp.notifyDataSetChanged()}
            catch (e:Exception){}

        }
    }

}


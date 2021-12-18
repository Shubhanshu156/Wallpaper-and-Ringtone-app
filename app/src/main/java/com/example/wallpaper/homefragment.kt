package com.example.wallpaper

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import com.example.wallpaper.adapter
import kotlinx.android.synthetic.main.fragment_homefragment.*
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.VolleyError
import com.android.volley.VolleyLog
import com.google.gson.GsonBuilder


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class homefragment : Fragment() {
    var TAG = "WELCOME"
    lateinit var rcv2: RecyclerView

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview = inflater.inflate(R.layout.fragment_homefragment, container, false)
        val rcv = rootview.findViewById<RecyclerView>(R.id.editorchoice)
        rcv.setHasFixedSize(true)
        rcv.setItemViewCacheSize(20)
        rcv.setDrawingCacheEnabled(true)
        rcv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
        rcv
            .getViewTreeObserver()
            .addOnGlobalLayoutListener(
                object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {

                        rcv
                            .getViewTreeObserver()
                            .removeOnGlobalLayoutListener(this)
                        prgrsmain.visibility=View.GONE
                    }
                })

        val editoradapter = adapter()
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.HORIZONTAL
        rcv.setLayoutManager(llm)
        rcv.setAdapter(editoradapter)
//        var a = ArrayList<Photo>()
//        GlobalScope.launch(Dispatchers.Main) {
//            val arr = withContext(Dispatchers.IO) { geteditor() }}

        val linkTrang = "https://api.pexels.com/v1/curated"
        val queue = Volley.newRequestQueue(context)
        var ans=ArrayList<Photo>()

        val stringRequest = object: StringRequest(linkTrang,
            Response.Listener<String> { response ->

                val gsonBuilder=GsonBuilder()
                val gson=gsonBuilder.create()
                val editorreslut = gson.fromJson<result>(response,result::class.java)!!
                ans=editorreslut.photos
                editoradapter.seteditor(ans)
                editoradapter.notifyDataSetChanged()

                Log.d(TAG, "geteditor: in suceess "+ans.size)

            },
            ErrorListener {
                Toast.makeText(context, "not able to fetch data", Toast.LENGTH_SHORT).show()
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "563492ad6f91700001000001064410a0217f42f5836f093e332ea9c0"
                return headers
            }
        }
        Log.d(TAG, "geteditor: calling fun"+ans.size)
        queue.add(stringRequest)


        rcv2 = rootview.findViewById<RecyclerView>(R.id.colorrcv)
        val coloradapter=CustomAdapter(setupcolor())
        val colormanager = LinearLayoutManager(context)
        colormanager.orientation = LinearLayoutManager.HORIZONTAL
        rcv2.setLayoutManager(colormanager)
        rcv2.adapter=coloradapter

        val rcv3 = rootview.findViewById<RecyclerView>(R.id.categories)
        val categoryadatper=categoryadapter(setupcategory())
        val catmanager = LinearLayoutManager(context)
        catmanager.orientation = LinearLayoutManager.VERTICAL
        rcv3.setLayoutManager(catmanager)
        rcv3.adapter=categoryadatper
        val more=rootview.findViewById<TextView>(R.id.more)
        more.setOnClickListener{
            val intent=Intent(context,wallpaper_result::class.java)
            intent.putExtra("url","https://api.pexels.com/v1/curated")
            startActivity(intent)
        }




        return rootview
    }

    private fun setupcolor() :ArrayList<colormodel>{
        var b=ArrayList<colormodel>()
        b.add(colormodel("red","#FF0000"))
        b.add(colormodel("orange","#FFA500"))
        b.add(colormodel("yellow","#FFFF00"))
        b.add(colormodel("green","#00FF00"))
        b.add(colormodel("cyan","#00FFFF"))
        b.add(colormodel("blue","#0000FF"))
        b.add(colormodel("magnata","#FF00FF"))
        b.add(colormodel("purple","#800080"))
        b.add(colormodel("white","#FFFFFF"))
        b.add(colormodel("black","#000000"))
        b.add(colormodel("violet","#8F00FF"))
        b.add(colormodel("indigo","#4B0082"))
        return b




    }
    private fun setupcategory():ArrayList<ArrayList<categoryclass>>{
        var b=ArrayList<ArrayList<categoryclass>>()

        var temp=ArrayList<categoryclass>()
        temp.add(categoryclass("Art","https://images.pexels.com/photos/3246665/pexels-photo-3246665.png?auto=compress&cs=tinysrgb&h=650&w=940"))
        temp.add(categoryclass("Ocean","https://images.pexels.com/photos/189349/pexels-photo-189349.jpeg?auto=compress\\u0026cs=tinysrgb\\u0026h=650\\u0026w=940"))
        b.add(temp)

         temp=ArrayList<categoryclass>()
        temp.add(categoryclass("Bicycle","https://images.pexels.com/photos/5465176/pexels-photo-5465176.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
        temp.add(categoryclass("Bikes","https://images.pexels.com/photos/2949302/pexels-photo-2949302.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
        b.add(temp)

        temp=ArrayList<categoryclass>()
        temp.add(categoryclass("Flowers","https://images.pexels.com/photos/931177/pexels-photo-931177.jpeg?auto=compress\\u0026cs=tinysrgb\\u0026h=650\\u0026w=940"))
        temp.add(categoryclass("Cars","https://images.pexels.com/photos/170811/pexels-photo-170811.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
        b.add(temp)

         temp=ArrayList<categoryclass>()
        temp.add(categoryclass("Entertainment","https://images.pexels.com/photos/1763075/pexels-photo-1763075.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
        temp.add(categoryclass("Gaming","https://images.pexels.com/photos/3165335/pexels-photo-3165335.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
        b.add(temp)

         temp=ArrayList<categoryclass>()
        temp.add(categoryclass("Gods","https://images.pexels.com/photos/2969469/pexels-photo-2969469.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
        temp.add(categoryclass("Travel","https://images.pexels.com/photos/2325446/pexels-photo-2325446.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
        b.add(temp)

         temp=ArrayList<categoryclass>()
        temp.add(categoryclass("Food","https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
        temp.add(categoryclass("Music","https://images.pexels.com/photos/6966/abstract-music-rock-bw.jpg?auto=compress&cs=tinysrgb&h=650&w=940"))
        b.add(temp)

         temp=ArrayList<categoryclass>()
        temp.add(categoryclass("Nature","https://images.pexels.com/photos/624015/pexels-photo-624015.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
        temp.add(categoryclass("Neon","https://images.pexels.com/photos/5411700/pexels-photo-5411700.png?auto=compress&cs=tinysrgb&h=650&w=940"))
        b.add(temp)

         temp=ArrayList<categoryclass>()
        temp.add(categoryclass("Space","https://images.pexels.com/photos/220201/pexels-photo-220201.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
        temp.add(categoryclass("Rain","https://images.pexels.com/photos/1100946/pexels-photo-1100946.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
        b.add(temp)

         temp=ArrayList<categoryclass>()
        temp.add(categoryclass("Sports","https://images.pexels.com/photos/46798/the-ball-stadion-football-the-pitch-46798.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
        temp.add(categoryclass("Wildlife","https://images.pexels.com/photos/247431/pexels-photo-247431.jpeg?auto=compress&cs=tinysrgb&h=650&w=940"))
        b.add(temp)




        return  b





    }

    suspend fun geteditor():ArrayList<Photo>{
        val linkTrang = "https://api.pexels.com/v1/curated"
        val queue = Volley.newRequestQueue(context)
        var ans=ArrayList<Photo>()

        val stringRequest = object: StringRequest(linkTrang,
            Response.Listener<String> { response ->

                val gsonBuilder=GsonBuilder()
                val gson=gsonBuilder.create()
                val editorreslut = gson.fromJson<result>(response,result::class.java)!!
                ans=editorreslut.photos

                Log.d(TAG, "geteditor: in suceess "+ans.size)

            },
            ErrorListener {
                Toast.makeText(context, "not able to fetch data", Toast.LENGTH_SHORT).show()
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "563492ad6f91700001000001064410a0217f42f5836f093e332ea9c0"
                return headers
            }
        }
        Log.d(TAG, "geteditor: calling fun"+ans.size)
        queue.add(stringRequest)
        return  ans




        }
















    suspend fun getcolor():ArrayList<Photo>{
        val okHttpClient= OkHttpClient()
//        val request: Request = Builder().url(url).headers(headerbuild).build()
        val request= Request.Builder().addHeader("Authorization","563492ad6f91700001000001064410a0217f42f5836f093e332ea9c0").url("https://api.pexels.com/v1/curated").build()
        val response=okHttpClient.newCall(request).execute()
        val res=response.body?.string()
        val gson= Gson()
        val editorreslut=gson.fromJson<result>(res,result::class.java)
        Log.d(TAG, "geteditor: "+editorreslut.photos.size)
        return editorreslut.photos

    }



    companion object {

        fun newInstance(param1: String, param2: String) =
            homefragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
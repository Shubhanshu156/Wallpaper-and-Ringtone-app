package com.example.wallpaper


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AbsListView

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_wallpaper_result.*
import java.lang.Exception


class wallpaper_result : AppCompatActivity() {
var lastpage="https://api.pexels.com/v1/curated"
lateinit var maxpage:String
lateinit var geturl:String
var cond=true
var isscrolling=false
    var currentItems = 0
    var totalItems:Int = 0
    var scrollOutItems:Int = 0

    val TAG="welcome"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_wallpaper_result)
        val intent= intent
        val url=intent.getStringExtra("url")
        val i1=url!!.indexOf("query=")
        val a=url.substring(i1+5)
        if (url!! !="https://api.pexels.com/v1/curated")
        lastpage="https://api.pexels.com/v1/search?query=$a"
        geturl=url!!

        val adp=resultadapter()

        val g=GridLayoutManager(this,2)
        res.setHasFixedSize(true)
        res.setItemViewCacheSize(20)
        res.setDrawingCacheEnabled(true)
        res.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
        res.layoutManager = g
        res.adapter = adp


        pulltorefresh.setOnRefreshListener {
            var arr=ArrayList<Photo>()

            GlobalScope.launch(Dispatchers.Main) {

                val linkTrang = lastpage
                val queue = Volley.newRequestQueue(this@wallpaper_result)

                val stringRequest = object: StringRequest(linkTrang,
                    Response.Listener<String> { response ->

                        val gsonBuilder= GsonBuilder()
                        val gson=gsonBuilder.create()
                        val editorreslut = gson.fromJson<result>(response,result::class.java)!!
                        arr=editorreslut.photos
                        adp.setresult(arr)
                        adp.notifyDataSetChanged()

                        try {

                            lastpage=editorreslut.next_page
                        }
                        catch (e:Exception){
                            lastpage=geturl
                            cond=false
                        }


                    },
                    Response.ErrorListener {
                        Toast.makeText(this@wallpaper_result, "not able to fetch data", Toast.LENGTH_SHORT).show()
                    }
                )
                {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Authorization"] = "563492ad6f91700001000001064410a0217f42f5836f093e332ea9c0"
                        return headers
                    }
                }
                queue.add(stringRequest)















//
                Log.d(TAG, "onCreate: "+arr.size)
                adp.setresult(arr)
                adp.notifyDataSetChanged()

            }

//            Handler().postDelayed(Runnable { pulltorefresh.isRefreshing=false }, 5000)
            pulltorefresh.isRefreshing=false
//
        }


        res.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && cond) {

                    loadmore.visibility=View.VISIBLE
                    Toast.makeText(this@wallpaper_result, "loading", Toast.LENGTH_SHORT).show()
                    GlobalScope.launch(Dispatchers.Main) {


                            val linkTrang = lastpage
                            val queue = Volley.newRequestQueue(this@wallpaper_result)

                            val stringRequest = object: StringRequest(linkTrang,
                                Response.Listener<String> { response ->

                                    val gsonBuilder= GsonBuilder()
                                    val gson=gsonBuilder.create()
                                    val editorreslut = gson.fromJson<result>(response,result::class.java)!!
                                    var arr=editorreslut.photos
                                    adp.addresult(arr)
                                    adp.notifyDataSetChanged()

                                    try {

                                        lastpage=editorreslut.next_page
                                    }
                                    catch (e:Exception){
                                        lastpage=geturl
                                        cond=false
                                    }


                                },
                                Response.ErrorListener {
                                    Toast.makeText(this@wallpaper_result, "not able to fetch data", Toast.LENGTH_SHORT).show()
                                }
                            )
                            {
                                override fun getHeaders(): MutableMap<String, String> {
                                    val headers = HashMap<String, String>()
                                    headers["Authorization"] = "563492ad6f91700001000001064410a0217f42f5836f093e332ea9c0"
                                    return headers
                                }
                            }
                            queue.add(stringRequest)



















                    }
                    loadmore.visibility= View.GONE

                }

            }
        })




        GlobalScope.launch(Dispatchers.Main) {


                val linkTrang = url
                val queue = Volley.newRequestQueue(this@wallpaper_result)

                val stringRequest = object: StringRequest(linkTrang,
                    Response.Listener<String> { response ->

                        val gsonBuilder= GsonBuilder()
                        val gson=gsonBuilder.create()
                        val editorreslut = gson.fromJson<result>(response,result::class.java)!!
                        val arr=editorreslut.photos
                        adp.setresult(arr)
                        adp.notifyDataSetChanged()

                        try {

                            lastpage=editorreslut.next_page
                        }
                        catch (e:Exception){
                            lastpage=geturl
                            cond=false
                        }


                    },
                    Response.ErrorListener {
                        Toast.makeText(this@wallpaper_result, "not able to fetch data", Toast.LENGTH_SHORT).show()
                    }
                )
                {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Authorization"] = "563492ad6f91700001000001064410a0217f42f5836f093e332ea9c0"
                        return headers
                    }
                }
                queue.add(stringRequest)









        }



       usersearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

           override fun onQueryTextSubmit(query: String?): Boolean {


               GlobalScope.launch(Dispatchers.Main) {


                   val linkTrang = "https://api.pexels.com/v1/search?query="+query
                   val queue = Volley.newRequestQueue(this@wallpaper_result)

                   val stringRequest = object: StringRequest(linkTrang,
                       Response.Listener<String> { response ->

                           val gsonBuilder= GsonBuilder()
                           val gson=gsonBuilder.create()
                           val editorreslut = gson.fromJson<result>(response,result::class.java)!!
                           val arr=editorreslut.photos
                           adp.setresult(arr)
                           adp.notifyDataSetChanged()

                           try {

                               lastpage=editorreslut.next_page
                           }
                           catch (e:Exception){
                               lastpage=geturl
                               cond=false
                           }


                       },
                       Response.ErrorListener {
                           Toast.makeText(this@wallpaper_result, "not able to fetch data", Toast.LENGTH_SHORT).show()
                       }
                   )
                   {
                       override fun getHeaders(): MutableMap<String, String> {
                           val headers = HashMap<String, String>()
                           headers["Authorization"] = "563492ad6f91700001000001064410a0217f42f5836f093e332ea9c0"
                           return headers
                       }
                   }
                   queue.add(stringRequest)









               }
           return false}
           override fun onQueryTextChange(newText: String?): Boolean {
               GlobalScope.launch(Dispatchers.Main) {


                   val linkTrang = "https://api.pexels.com/v1/search?query="+newText
                   val queue = Volley.newRequestQueue(this@wallpaper_result)

                   val stringRequest = object: StringRequest(linkTrang,
                       Response.Listener<String> { response ->

                           val gsonBuilder= GsonBuilder()
                           val gson=gsonBuilder.create()
                           val editorreslut = gson.fromJson<result>(response,result::class.java)!!
                           val arr=editorreslut.photos
                           adp.setresult(arr)
                           adp.notifyDataSetChanged()

                           try {

                               lastpage=editorreslut.next_page
                           }
                           catch (e:Exception){
                               lastpage=geturl
                               cond=false
                           }


                       },
                       Response.ErrorListener {
                           Toast.makeText(this@wallpaper_result, "not able to fetch data", Toast.LENGTH_SHORT).show()
                       }
                   )
                   {
                       override fun getHeaders(): MutableMap<String, String> {
                           val headers = HashMap<String, String>()
                           headers["Authorization"] = "563492ad6f91700001000001064410a0217f42f5836f093e332ea9c0"
                           return headers
                       }
                   }
                   queue.add(stringRequest)









               }
               return false
           }

       })


    }



    }

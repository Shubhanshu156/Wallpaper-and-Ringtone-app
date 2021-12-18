package com.example.wallpaper

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import android.media.AudioManager

import android.media.AudioAttributes
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.AudioFocusRequest
import android.os.Build
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var mymediaplayer=MediaPlayer()

/**
 * A simple [Fragment] subclass.
 * Use the [discoverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class discoverFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var mediaPlayer: MediaPlayer
    lateinit var audioManager: AudioManager
    lateinit var playbackAttributes: AudioAttributes
    lateinit var editoradapter: songAdapter
    var param1: String? = null
    private var param2: String? = null
    val TAG="HELLO"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onStop() {
        super.onStop()
        mymediaplayer.reset()
    }

    override fun onPause() {
        super.onPause()
        mymediaplayer.reset()
    }
    var audioFocusChangeListener =
        OnAudioFocusChangeListener { focusChange ->
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start()
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                mediaPlayer.pause()
                mediaPlayer.seekTo(0)
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                mediaPlayer.release()
            }
        }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_discover, container, false)

        val songrcv=view.findViewById<RecyclerView>(R.id.songrcv)

        editoradapter = songAdapter(requireContext(), mymediaplayer)
        val llm = LinearLayoutManager(context)
//        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        playbackAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(playbackAttributes)
            .setAcceptsDelayedFocusGain(true)
            .setOnAudioFocusChangeListener(audioFocusChangeListener)
            .build()

        llm.orientation = LinearLayoutManager.VERTICAL
        val loading=view.findViewById<ProgressBar>(R.id.failed)
        val error=view.findViewById<TextView>(R.id.error)
            songrcv.layoutManager=llm
            songrcv.adapter=editoradapter

        val search=view.findViewById<SearchView>(R.id.search)

        search.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {

                            var url="https://itunes.apple.com/search?term=$query&entity=song"

                    GlobalScope.launch(Dispatchers.Main) {


                        val linkTrang=url
                        val queue = Volley.newRequestQueue(context)

                        val stringRequest = object: StringRequest(linkTrang,
                            Response.Listener<String> { response ->

                                val gsonBuilder= GsonBuilder()
                                val gson=gsonBuilder.create()
                                val editorreslut = gson.fromJson<SongSample>(response,SongSample::class.java)!!
                                if (editorreslut.resultCount>0){
                                editoradapter.setmlist(editorreslut.results)
                                    loading.visibility=View.VISIBLE
                                editoradapter.notifyDataSetChanged()}
                                else{
                                    error.visibility=View.VISIBLE
                                    loading.visibility=View.GONE

                                }


                            },
                            Response.ErrorListener {
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
                        queue.add(stringRequest)









                    }
                    return false
                }
              override  fun onQueryTextChange(newText: String?): Boolean {
                  var url="https://itunes.apple.com/search?term=$newText&entity=song"
                  var v=newText
                  GlobalScope.launch(Dispatchers.Main) {


                      val linkTrang=url
                      val queue = Volley.newRequestQueue(context)

                      val stringRequest = object: StringRequest(linkTrang,
                          Response.Listener<String> { response ->

                              val gsonBuilder= GsonBuilder()
                              val gson=gsonBuilder.create()
                              val editorreslut = gson.fromJson<SongSample>(response,SongSample::class.java)!!


                                  editoradapter.setmlist(editorreslut.results)

                                  editoradapter.notifyDataSetChanged()

                                  error.visibility=View.GONE
                                  loading.visibility=View.GONE





                          },
                          Response.ErrorListener {
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
                      queue.add(stringRequest)                  }

                    return false
                }
            })



        GlobalScope.launch(Dispatchers.Main) {


            val linkTrang="https://itunes.apple.com/search?term=music&entity=song"
            val queue = Volley.newRequestQueue(context)

            val stringRequest = object: StringRequest(linkTrang,
                Response.Listener<String> { response ->

                    val gsonBuilder= GsonBuilder()
                    val gson=gsonBuilder.create()
                    val editorreslut = gson.fromJson<SongSample>(response,SongSample::class.java)!!
                    editoradapter.setmlist(editorreslut.results)
                    editoradapter.notifyDataSetChanged()



                },
                Response.ErrorListener {
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
            queue.add(stringRequest)









        }

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            discoverFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }





}
package com.example.wallpaper
import android.app.DownloadManager
import android.content.*
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import android.media.AudioManager
import android.net.Uri
import android.util.Log
import android.os.Environment
import android.content.IntentFilter


class songAdapter(val context: Context,var mymediaplyer:MediaPlayer) : RecyclerView.Adapter<songAdapter.ViewHolder>() {

    val TAG="welcome"
    val intentFilter: IntentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)

//    private var mymediaplyer:MediaPlayer=MediaPlayer()
    private var isreleased=false
    lateinit var preferenceManager: SharedPreferences
    lateinit var downloadmanager:DownloadManager
    var lastid="welcome"
    val strPref_Download_ID = "PREF_DOWNLOAD_ID"



    var mList=ArrayList<ResultX>()
//    private val downloadReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(arg0: Context, arg1: Intent) {
//            Log.d(TAG, "onReceive: checking downnload status")
//            CheckDwnloadStatus()
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
//        context.registerReceiver(downloadReceiver, intentFilter)
          val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sample_song, parent, false)


        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val time=(28000..33000).random()
        var realtime: Int =mList[position].trackTimeMillis
      



        if (time<(mList[position].trackTimeMillis)){realtime=time}
        Log.d(TAG, "onBindViewHolder: "+realtime)
        val seconds=realtime/1000
        val mn=seconds/60
        val sec=seconds%60
        val ans=mList[position].artistName+"||0$mn.$sec⏲"
        holder.details.text=ans
        holder.trackname.text=mList[position].trackName


//        holder.playbtn.setOnCheckedChangeListener { buttonView, isChecked ->
//            if (isChecked){
//
//                mymediaplyer.reset()
//                if (mymediaplyer.isPlaying){
//
//                    mymediaplyer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    try {
//                        mymediaplyer.setDataSource(mList[position].previewUrl)
//                        // below line is use to prepare
//                        // and start our media player.
//                        mymediaplyer.prepare()
//                        mymediaplyer.prepare()
//                        val cntr_aCounter: CountDownTimer = object : CountDownTimer(realtime.toLong(), 1000) {
//                            override fun onTick(millisUntilFinished: Long) {
//                                mymediaplyer.start()
//                            }
//
//                            override fun onFinish() {
//                                //code fire after finish
//                                mymediaplyer.reset()
//                                holder.playbtn.isChecked=true
//                            }
//                        }
//                        cntr_aCounter.start()
//
//                        mymediaplyer.start()
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//                else{
//                    try {
//                        mymediaplyer.setDataSource(mList[position].previewUrl)
//                        // below line is use to prepare
//                        // and start our media player.
//                        mymediaplyer.prepare()
//                        mymediaplyer.start()
//
//
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//
//            }
//
//
//
//            else{
//                if (mymediaplyer.isPlaying()) {
//
//                    holder.playbtn.isChecked=false
//                    mymediaplyer.reset()
//
//                }
//                else {
//                    holder.playbtn.isChecked=false
//                    mymediaplyer.start()
//                }
//
//            }
//        }
        holder.btn.setOnClickListener {


           //            downloadmanager.addCompletedDownload()
            downloadmanager=holder.itemView.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            val dmr:DownloadManager.Request =  DownloadManager.Request(Uri.parse(mList[position].previewUrl))

            val fileName=mList[position].artistId.toString()
            dmr.setTitle(fileName)
            dmr.setDescription("Downloading your Ringtone") //optional


            dmr.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            dmr.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            dmr.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            val manager = holder.itemView.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val id=manager.enqueue(dmr)


//            val PrefEdit = preferenceManager!!.edit()
//            PrefEdit.putLong(strPref_Download_ID, id)
//            PrefEdit.commit()
//             val k: File = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
//            if(k.exists()){
//
//                val values = ContentValues()
//                values.put(MediaStore.MediaColumns.DATA, k.absolutePath)
//                values.put(MediaStore.MediaColumns.TITLE, "My Song title")
//                values.put(MediaStore.MediaColumns.SIZE, 215454)
//                values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3")
//                values.put(MediaStore.Audio.Media.ARTIST, "Madonna")
//                values.put(MediaStore.Audio.Media.DURATION, 230)
//                values.put(MediaStore.Audio.Media.IS_RINGTONE, true)
//                values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false)
//                values.put(MediaStore.Audio.Media.IS_ALARM, false)
//                values.put(MediaStore.Audio.Media.IS_MUSIC, false)
//
//                val uri = MediaStore.Audio.Media.getContentUriForPath(k.absolutePath)
//                val newUri: Uri = holder.itemView.context.getContentResolver().insert(uri!!, values)!!
//
//
//            }
           }
        holder.itemView.setOnClickListener {
            val bol=mList[position].trackId.toString()==lastid
            if (mymediaplyer.isPlaying and bol){
                mymediaplyer.reset()
            }
            else{
                lastid=mList[position].trackId.toString()
                mymediaplyer.reset()
                mymediaplyer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mymediaplyer.setDataSource(mList[position].previewUrl)
                mymediaplyer.prepare()
                mymediaplyer.start()


            }
        }




    }
    fun setmlist(v:ArrayList<ResultX>){
        mList=v

    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val btn: Button =ItemView.findViewById(R.id.setringtone)
//        val playbtn: ToggleButton =ItemView.findViewById(R.id.play)
        val details: TextView =ItemView.findViewById(R.id.info)
        val trackname: TextView =ItemView.findViewById(R.id.trackname)



    }


//    private fun CheckDwnloadStatus() {
//        Log.d(TAG, "CheckDwnloadStatus:checking ")
//
//        val query = DownloadManager.Query()
//        val id = preferenceManager!!.getLong(strPref_Download_ID, 0)
//        query.setFilterById(id)
//        Log.d(TAG, "CheckDwnloadStatus: 2")
//        val cursor = downloadmanager!!.query(query)
//        if (cursor.moveToFirst()) {
//            val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
//            val status = cursor.getInt(columnIndex)
//            val columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
//            val reason = cursor.getInt(columnReason)
//            when (status) {
//                DownloadManager.STATUS_FAILED -> {
//                    var failedReason = ""
//                    when (reason) {
//                        DownloadManager.ERROR_CANNOT_RESUME -> failedReason = "ERROR_CANNOT_RESUME"
//                        DownloadManager.ERROR_DEVICE_NOT_FOUND -> failedReason =
//                            "ERROR_DEVICE_NOT_FOUND"
//                        DownloadManager.ERROR_FILE_ALREADY_EXISTS -> failedReason =
//                            "ERROR_FILE_ALREADY_EXISTS"
//                        DownloadManager.ERROR_FILE_ERROR -> failedReason = "ERROR_FILE_ERROR"
//                        DownloadManager.ERROR_HTTP_DATA_ERROR -> failedReason =
//                            "ERROR_HTTP_DATA_ERROR"
//                        DownloadManager.ERROR_INSUFFICIENT_SPACE -> failedReason =
//                            "ERROR_INSUFFICIENT_SPACE"
//                        DownloadManager.ERROR_TOO_MANY_REDIRECTS -> failedReason =
//                            "ERROR_TOO_MANY_REDIRECTS"
//                        DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> failedReason =
//                            "ERROR_UNHANDLED_HTTP_CODE"
//                        DownloadManager.ERROR_UNKNOWN -> failedReason = "ERROR_UNKNOWN"
//                    }
//
//                }
//                DownloadManager.STATUS_PAUSED -> {
//                    var pausedReason = ""
//                    when (reason) {
//                        DownloadManager.PAUSED_QUEUED_FOR_WIFI -> pausedReason =
//                            "PAUSED_QUEUED_FOR_WIFI"
//                        DownloadManager.PAUSED_UNKNOWN -> pausedReason = "PAUSED_UNKNOWN"
//                        DownloadManager.PAUSED_WAITING_FOR_NETWORK -> pausedReason =
//                            "PAUSED_WAITING_FOR_NETWORK"
//                        DownloadManager.PAUSED_WAITING_TO_RETRY -> pausedReason =
//                            "PAUSED_WAITING_TO_RETRY"
//                    }
//
//                }
//
//
//                DownloadManager.STATUS_SUCCESSFUL -> {
//                    Log.d(TAG, "CheckDwnloadStatus: ")
//                    GetFile()
//                }
//            }
//        }
//        Log.d(TAG, "CheckDwnloadStatus: 3")
//    }

    private fun GetFile() {
        //Retrieve the saved request id
        Log.d(TAG, "GetFile: successfully downloaded")
    }


}



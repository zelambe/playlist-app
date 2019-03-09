package com.example.playlistapp.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask
import android.util.Log
import com.example.playlistapp.model.Song
import com.example.playlistapp.model.SongDetail
import com.example.playlistapp.util.QueryUtils

class SongDetailViewModel(application: Application): AndroidViewModel(application) {
    private var songInfo: MutableLiveData<ArrayList<SongDetail>> = MutableLiveData()

    fun getInfo(artist : String, song: String): MutableLiveData<ArrayList<SongDetail>> {
        Log.d("get Info", artist)
        Log.d("get Info", song)
        loadInfo(artist, song)
        return songInfo
    }

    private fun loadInfo(artist : String, song: String) {
        Log.d("load Info", artist)
        Log.d("load Info", song)
        ProductAsyncTask().execute(artist, song)
    }


    @SuppressLint("StaticFieldLeak")
    inner class ProductAsyncTask: AsyncTask<String, Unit, ArrayList<SongDetail>>() {
        override fun doInBackground(vararg params: String?): ArrayList<SongDetail>? {
            var param0 = params[0]!!
            var param1 = params[1]!!
            Log.d("async task", param0)
            Log.d("async task", param1)
            return QueryUtils.fetchSingleSongData(params[0]!!, params[1]!!)
        }

        override fun onPostExecute(result: ArrayList<SongDetail>?) {
            if (result == null) {
                Log.e("RESULTS", "No Results Found")
            }
            else {
                Log.e("RESULTS", result.toString())
                val song = result[0]
                Log.d("post execute", song.getSongTitle())
                Log.d("post execute", song.getSongArtist())
                Log.d("post execute", song.getAlbumArtUrl())
                Log.d("post execute", song.getPlayCount().toString())
                Log.d("post execute", song.getAlbum())
                songInfo.value = result
            }
        }
    }
}
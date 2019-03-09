package com.example.playlistapp.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask
import android.util.Log
import com.example.playlistapp.model.Song
import com.example.playlistapp.util.QueryUtils

class ArtistTracksViewModel(application: Application): AndroidViewModel(application) {
    private var _tracksList: MutableLiveData<ArrayList<Song>> = MutableLiveData()

    fun getSongs(query :String): MutableLiveData<ArrayList<Song>> {
        loadSongs(query)
        return _tracksList
    }

    private fun loadSongs(query: String) {
        ProductAsyncTask().execute(query)
    }


    @SuppressLint("StaticFieldLeak")
    inner class ProductAsyncTask: AsyncTask<String, Unit, ArrayList<Song>>() {
        override fun doInBackground(vararg params: String?): ArrayList<Song>? {
            return QueryUtils.fetchArtistTracks(params[0]!!)
        }

        override fun onPostExecute(result: ArrayList<Song>?) {
            if (result == null) {
                Log.e("RESULTS", "No Results Found")
            }
            else {
                Log.e("RESULTS", result.toString())
                _tracksList.value = result
            }
        }
    }
}
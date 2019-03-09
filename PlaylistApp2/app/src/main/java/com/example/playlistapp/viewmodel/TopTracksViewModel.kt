package com.example.playlistapp.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask
import android.util.Log
import com.example.playlistapp.model.Song
import com.example.playlistapp.util.QueryUtils

class TopTracksViewModel(application: Application): AndroidViewModel(application) {
    private var _tracksList: MutableLiveData<ArrayList<Song>> = MutableLiveData()

    fun getSongs(): MutableLiveData<ArrayList<Song>> {
        loadSongs()
        return _tracksList
    }

    private fun loadSongs() {
        ProductAsyncTask().execute()
    }


    @SuppressLint("StaticFieldLeak")
    inner class ProductAsyncTask: AsyncTask<String, Unit, ArrayList<Song>>() {
        override fun doInBackground(vararg params: String?): ArrayList<Song>? {
           return QueryUtils.fetchTopTracks()
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
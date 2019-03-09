package com.example.playlistapp.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.playlistapp.db.DbSettings
import com.example.playlistapp.db.PlaylistDatabaseHelper
import com.example.playlistapp.model.PlaylistSong

class PlaylistViewModel(application: Application): AndroidViewModel(application) {
    private var _playlistDBHelper: PlaylistDatabaseHelper = PlaylistDatabaseHelper(application)
    private var _tracksList: MutableLiveData<ArrayList<PlaylistSong>> = MutableLiveData()


    fun getSongs(): MutableLiveData<ArrayList<PlaylistSong>> {
        val returnList = this.loadSongs()

        this._tracksList.value = returnList
        return this._tracksList
    }

    private fun loadSongs(): ArrayList<PlaylistSong> {
        val tracks: ArrayList<PlaylistSong> = ArrayList()
        val database = this._playlistDBHelper.readableDatabase

        val selection: Array<String> = arrayOf(DbSettings.DBPlaylistEntry.SONG_ARTIST,
            DbSettings.DBPlaylistEntry.SONG_TITLE,
            DbSettings.DBPlaylistEntry.IMG_URL)

        val cursor = database.query(
            DbSettings.DBPlaylistEntry.TABLE,
            selection ,
            null, null, null, null, null)


        while (cursor.moveToNext()) {
            val cursorArtist = cursor.getColumnIndex(DbSettings.DBPlaylistEntry.SONG_ARTIST)
            val cursorSongTitle = cursor.getColumnIndex(DbSettings.DBPlaylistEntry.SONG_TITLE)
            val cursorImageURL = cursor.getColumnIndex(DbSettings.DBPlaylistEntry.IMG_URL)

            val songTitle = cursor.getString(cursorSongTitle)
            val artist = cursor.getString(cursorArtist)
            val imageURL = cursor.getString(cursorImageURL)

            tracks.add(PlaylistSong(songTitle,artist,imageURL))

        }

        cursor.close()
        database.close()

        return tracks
    }



//    fun removeFavorite(id: String, isFromResultList: Boolean = false) { //TODO: Creative?
//        val database = _favouriteDBHelper.writableDatabase
//
//        database.delete(
//            DbSettings.DBFavoriteEntry.TABLE,
//            "${DbSettings.DBFavoriteEntry.COL_API_ID}=?",
//            arrayOf(id)
//        )
//        database.close()
//
//        var index = 0
//        val favorites = this._productsList.value
//        if (favorites != null) {
//            for (i in 0 until favorites.size) {
//                if (favorites[i].getId() == id) {
//                    index = i
//                }
//            }
//            if (isFromResultList) {
//                favorites[index].isFavorite = false
//            }
//            else {
//                favorites.removeAt(index)
//            }
//
//            this._productsList.value = favorites
//        }
//    }
}
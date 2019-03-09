package com.example.playlistapp.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PlaylistDatabaseHelper(context: Context): SQLiteOpenHelper(context, DbSettings.DB_NAME, null, DbSettings.DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createPlaylistTableQuery = "CREATE TABLE " + DbSettings.DBPlaylistEntry.TABLE + " ( " +
                DbSettings.DBPlaylistEntry.SONG_ARTIST+ " TEXT NULL, " +
                DbSettings.DBPlaylistEntry.SONG_TITLE+ " TEXT NULL," +
                DbSettings.DBPlaylistEntry.IMG_URL +" TEXT NULL);"

        db?.execSQL(createPlaylistTableQuery)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + DbSettings.DBPlaylistEntry.TABLE)
        onCreate(db)
    }

    fun addSong(artist: String, song: String, image : String) {
        // Gets the data repository in write mode
        val db = this.writableDatabase

// Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(DbSettings.DBPlaylistEntry.SONG_ARTIST, artist)
            put(DbSettings.DBPlaylistEntry.SONG_TITLE, song)
            put(DbSettings.DBPlaylistEntry.IMG_URL, image)
        }

// Insert the new row, returning the primary key value of the new row
        val newRowId = db?.insert(DbSettings.DBPlaylistEntry.TABLE, null, values)
    }
}
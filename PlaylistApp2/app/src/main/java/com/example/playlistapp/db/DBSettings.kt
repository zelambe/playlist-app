package com.example.playlistapp.db

import android.provider.BaseColumns


class DbSettings {
    companion object {
        const val DB_NAME = "playlist.db"
        const val DB_VERSION = 1
    }



    class DBPlaylistEntry:BaseColumns  {
        companion object {
            const val TABLE = "app_playlist"
            const val SONG_TITLE = "song_title"
            const val SONG_ARTIST = "song_artist"
            const val IMG_URL = "img_url"
        }
    }

}
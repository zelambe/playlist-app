package com.example.playlistapp.model

import java.io.Serializable

class Song(): Serializable {
    private var song_title: String = ""
    private var artist_picture_url: String = ""
    private var song_artist: String = " "

    constructor(
        song_title: String = "",
        artist_picture_url: String = "",
        song_artist : String = ""

    ) : this() {
        this.song_title = song_title
        this.artist_picture_url =artist_picture_url
        this.song_artist = song_artist
    }

    //                        //
    // ------ Getters ------- //
    //                        //

    fun getSongTitle(): String {
        return this.song_title
    }

    fun getArtistPictureURL(): String{
        return this.artist_picture_url
    }

    fun getArtist(): String{
        return this.song_artist
    }

}

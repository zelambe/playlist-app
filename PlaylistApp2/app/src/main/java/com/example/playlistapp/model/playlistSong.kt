package com.example.playlistapp.model

import java.io.Serializable

class PlaylistSong(): Serializable {
    private var song_title: String = ""
    private var song_artist: String = ""
    private var img_url: String= ""

    constructor(
        song_title: String = "",
        song_artist: String = "",
        img_url: String= ""

        ) : this() {
        this.song_title = song_title
        this.song_artist = song_artist
        this.img_url = img_url

    }

    //                        //
    // ------ Getters ------- //
    //                        //

    fun getSongTitle(): String {
        return this.song_title
    }

    fun getSongArtist(): String {
        return this.song_artist
    }

    fun getImageUrl(): String{
        return this.img_url
    }


}


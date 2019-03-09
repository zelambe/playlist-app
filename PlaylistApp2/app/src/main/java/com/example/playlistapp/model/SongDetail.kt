package com.example.playlistapp.model

import java.io.Serializable

class SongDetail(): Serializable {
    private var song_title: String = ""
    private var song_artist: String = ""
    private var album: String = ""
    private var play_count: Int = 0
    private var album_art_url : String = ""

    constructor(
        song_title: String = "",
        song_artist: String = "",
        album : String = "",
        play_count : Int = 0,
        album_art_url : String =""

    ) : this() {
        this.song_title = song_title
        this.song_artist = song_artist
        this.album = album
        this.play_count = play_count
        this.album_art_url = album_art_url

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

    fun getAlbum(): String {
        return this.album
    }

    fun getPlayCount(): Int {
        return this.play_count
    }
    fun getAlbumArtUrl(): String{
        return this.album_art_url
    }



    //Setters

    fun setSongTitle(string: String) {
        this.song_title = string
    }

    fun setSongArtist(string: String) {
        this.song_artist = string
    }
    fun setAlbum(string: String) {
        this.album = string
    }
    fun setPlayCount(int: Int) {
        this.play_count = int
    }
    fun setAlbumArtUrl(string: String){
        this.album_art_url = string
    }


}

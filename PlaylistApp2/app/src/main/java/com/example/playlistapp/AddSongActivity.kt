package com.example.playlistapp

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.playlistapp.db.PlaylistDatabaseHelper
import com.example.playlistapp.model.Song
import com.example.playlistapp.model.SongDetail
import com.example.playlistapp.viewmodel.SongDetailViewModel
import com.example.playlistapp.viewmodel.TopTracksViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_song.*
import kotlinx.android.synthetic.main.fragment_whats_new.*
import kotlinx.android.synthetic.main.song_detail_item.view.*
import kotlinx.android.synthetic.main.song_item.view.*


class AddSongActivity : AppCompatActivity() {
    private var songTitle :String = ""
    private var artistName : String= ""
    private var context: Context =  this
    private lateinit var viewModel: SongDetailViewModel
    private var adapter = SongDetailAdapter()
    private var songList: ArrayList<SongDetail> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_song)

        if (intent.hasExtra("song_title")){
            songTitle = intent.getStringExtra("song_title")
        }

        Log.d("oncreate", intent.getStringExtra("song_artist"))
        if (intent.hasExtra("song_artist")){
            artistName = intent.getStringExtra("song_artist")
        }

        Log.d("oncreate", songTitle)
        Log.d("oncreate", artistName)

    }

    override fun onStart() {
        super.onStart()
        Log.d("onstart", songTitle)
        Log.d("onstart", artistName)

        single_song_list.layoutManager = LinearLayoutManager(this)
        viewModel = ViewModelProviders.of(this).get(SongDetailViewModel::class.java)

        val observer = Observer<ArrayList<SongDetail>>{
            single_song_list.adapter = adapter
            val result = DiffUtil.calculateDiff(object: DiffUtil.Callback(){

                override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
                    return (songList[p0].getSongTitle() == songList[p1].getSongTitle()) && (songList[p0].getSongArtist()== songList[p1].getSongArtist())
                }

                override fun getOldListSize(): Int {
                    return songList.size
                }

                override fun getNewListSize(): Int {
                    if (it == null) {
                        return 0
                    }
                    return it.size
                }

                override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
                    return (songList[p0] == songList[p1])
                }
            })
            result.dispatchUpdatesTo(adapter)
            songList = it ?: ArrayList()
        }
        Log.d("observed", songTitle)
        Log.d("observed", artistName)
        viewModel.getInfo(artistName,songTitle).observe(this,observer)
    }


    inner class SongDetailAdapter : RecyclerView.Adapter<SongDetailAdapter.SongDetailViewHolder>(){
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SongDetailAdapter.SongDetailViewHolder {
            val songView = LayoutInflater.from(p0.context).inflate(R.layout.song_detail_item, p0,false)
            Log.d("create view holder", "here")
            return SongDetailViewHolder(songView)
        }

        override fun onBindViewHolder(p0: SongDetailAdapter.SongDetailViewHolder, p1: Int) {
            val song = songList[p1]
            val songImage = song.getAlbumArtUrl()//TODO: Getting the image of the artist
            if (songImage != ""){
                Picasso.with(this@AddSongActivity.context).load(songImage).into(p0.image)
            }

            Log.d("bind view holder", song.getSongTitle())

            p0.songTitle.text = "Song: " + song.getSongTitle()
            p0.artist.text = "Artist: " + song.getSongArtist()
            p0.album.text = "Album: " + song.getAlbum()
            p0.playcount.text = "Play Count: " + song.getPlayCount().toString()
            p0.button.setOnClickListener{
                val db = PlaylistDatabaseHelper(this@AddSongActivity.context)
                db.addSong(song.getSongArtist(),song.getSongTitle(), song.getAlbumArtUrl())
                val text = "Song Added!"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(this@AddSongActivity.context, text, duration)
                toast.show()
            }
        }

        override fun getItemCount(): Int {
            return songList.size
        }

        inner class SongDetailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            var image = itemView.album_art_image_view
            var songTitle: TextView = itemView.detail_song_title
            var artist: TextView = itemView.detail_artist
            var album: TextView = itemView.detail_album
            var playcount: TextView = itemView.detail_playcount
            var button: Button = itemView.add_song_button

        }
    }
    }




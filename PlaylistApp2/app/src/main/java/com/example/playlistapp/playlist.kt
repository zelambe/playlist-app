package com.example.playlistapp

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.playlistapp.db.PlaylistDatabaseHelper
import com.example.playlistapp.model.PlaylistSong
import com.example.playlistapp.model.Song
import com.example.playlistapp.viewmodel.PlaylistViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_playlist.*
import kotlinx.android.synthetic.main.playlist_item.view.*
import kotlinx.android.synthetic.main.song_item.view.*


@SuppressLint("ValidFragment")
class playlist(context: Context) : Fragment() {
    private var adapter = PlaylistAdapter()
    private var parentContext: Context? = context
    private lateinit var viewModel: PlaylistViewModel

    private var tracksList: ArrayList<PlaylistSong> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

    override fun onStart() {
        super.onStart()

        playlist_view.layoutManager = LinearLayoutManager(parentContext)
        playlist_view.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        viewModel = ViewModelProviders.of(this).get(PlaylistViewModel::class.java)

        val observer = Observer<java.util.ArrayList<PlaylistSong>> {
            playlist_view.adapter = adapter
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
                    return tracksList[p0].getSongTitle() == tracksList[p1].getSongTitle()
                }

                override fun getOldListSize(): Int {
                    return tracksList.size
                }

                override fun getNewListSize(): Int {
                    if (it == null) {
                        return 0
                    }
                    return it.size
                }

                override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
                    return tracksList[p0] == tracksList[p1]
                }
            })
            result.dispatchUpdatesTo(adapter)
            tracksList = it ?: java.util.ArrayList()
        }
        //viewModel.getFavorites().observe(this, observer)
        viewModel.getSongs().observe(this, observer)

    }


    inner class PlaylistAdapter : RecyclerView.Adapter<playlist.PlaylistAdapter.PlaylistViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): playlist.PlaylistAdapter.PlaylistViewHolder {
            val songView = LayoutInflater.from(p0.context).inflate(R.layout.playlist_item, p0, false)
            return PlaylistViewHolder(songView)
        }

        override fun onBindViewHolder(p0: playlist.PlaylistAdapter.PlaylistViewHolder, p1: Int) {
            val song = tracksList[p1]
            //val songImage = song.getArtistPictureURL()//TODO: Getting the image of the artist
            p0.songTitle.text = song.getSongTitle()
            p0.songArtist.text = song.getSongArtist()
            val imageURL = song.getImageUrl()
            if (imageURL != ""){
                Picasso.with(this@playlist.parentContext).load(imageURL).into(p0.image)
            }

            p0.row.setOnClickListener {
                //dialog box with options to delete song?
//                val db = PlaylistDatabaseHelper(this@AddSongActivity.context)
//                db.addSong(song.getSongArtist(),song.getSongTitle(), song.getAlbumArtUrl())
//                val text = "Song Added!"
//                val duration = Toast.LENGTH_SHORT
//                val toast = Toast.makeText(this@AddSongActivity.context, text, duration)
//                toast.show()
            }
        }

        override fun getItemCount(): Int {
            return tracksList.size
        }

        inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val row = itemView
            var image = itemView.imageView //TODO: For creative?
            var songTitle: TextView = itemView.playlist_item_song_title//TODO:?????
            var songArtist: TextView = itemView.playlist_item_artist


        }
    }

}

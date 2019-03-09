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
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.example.playlistapp.model.Song
import com.example.playlistapp.viewmodel.ArtistTracksViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_result_list.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_whats_new.*
import kotlinx.android.synthetic.main.song_item.view.*

@SuppressLint("ValidFragment")
class ResultList(context: Context, query: String) : Fragment() {

    private var adapter = ArtistTracksAdapter()
    private var parentContext: Context = context
    private lateinit var viewModel: ArtistTracksViewModel

    private var tracksList: ArrayList<Song> = ArrayList()

    private var _query : String = query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result_list, container, false)
    }


    override fun onStart() {
        super.onStart()

        result_list.layoutManager = GridLayoutManager(parentContext, 2)
        viewModel = ViewModelProviders.of(this).get(ArtistTracksViewModel::class.java)

        val observer = Observer<ArrayList<Song>> {
            result_list.adapter = adapter
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

                override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
                    return (tracksList[p0].getSongTitle() == tracksList[p1].getSongTitle()) && (tracksList[p0].getArtistPictureURL() == tracksList[p1].getArtistPictureURL())
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
                    return (tracksList[p0] == tracksList[p1])
                }
            })
            result.dispatchUpdatesTo(adapter)
            tracksList = it ?: ArrayList()
        }
        Log.d("observe" , _query)

        viewModel.getSongs(_query).observe(this, observer) //TODO: pass query into this...?

    }

    inner class ArtistTracksAdapter : RecyclerView.Adapter<ResultList.ArtistTracksAdapter.ArtistTracksViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ResultList.ArtistTracksAdapter.ArtistTracksViewHolder {
            val songView = LayoutInflater.from(p0.context).inflate(R.layout.song_item, p0, false)
            return ArtistTracksViewHolder(songView)
        }

        override fun onBindViewHolder(p0: ResultList.ArtistTracksAdapter.ArtistTracksViewHolder, p1: Int) {
            val song = tracksList[p1]
            val songImage = song.getArtistPictureURL()//TODO: Getting the image of the artist
            if (songImage != ""){
                Picasso.with(this@ResultList.context).load(songImage).into(p0.image) //TODO: why is this not working
            }
            p0.songTitle.text = song.getSongTitle()
            p0.row.setOnClickListener {
                val intent = Intent(this@ResultList.parentContext, AddSongActivity::class.java)
                Log.d("onbind song", song.getSongTitle())
                intent.putExtra("song_title", song.getSongTitle())
                intent.putExtra("song_artist", song.getArtist()) //TODO: Put artist
                Log.d("on bind artist" , song.getArtist())
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return tracksList.size
        }

        inner class ArtistTracksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val row = itemView
            var image = itemView.artist_img //TODO:??????
            var songTitle: TextView = itemView.song_title //TODO:?????
        }

    }



}

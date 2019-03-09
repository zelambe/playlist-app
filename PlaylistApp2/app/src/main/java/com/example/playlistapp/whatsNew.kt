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
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.example.playlistapp.model.Song
import com.example.playlistapp.viewmodel.TopTracksViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_whats_new.*
import kotlinx.android.synthetic.main.song_item.view.*


@SuppressLint("ValidFragment")
class whatsNew(context: Context) : Fragment() {
    private var adapter = TopTracksAdapter()
    private var parentContext: Context = context
    private lateinit var viewModel: TopTracksViewModel

    private var tracksList: ArrayList<Song> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_whats_new, container, false)
    }


    override fun onStart() {
        super.onStart()



        top_tracks_list.layoutManager = GridLayoutManager(parentContext,2)
        viewModel = ViewModelProviders.of(this).get(TopTracksViewModel::class.java)

        val observer = Observer<ArrayList<Song>>{
            top_tracks_list.adapter = adapter
            val result = DiffUtil.calculateDiff(object: DiffUtil.Callback(){

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
        viewModel.getSongs().observe(this,observer)
    }


    inner class TopTracksAdapter : RecyclerView.Adapter<TopTracksAdapter.TopTracksViewHolder>(){
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TopTracksAdapter.TopTracksViewHolder {
            val songView = LayoutInflater.from(p0.context).inflate(R.layout.song_item, p0,false)
            return TopTracksViewHolder(songView)
        }

        override fun onBindViewHolder(p0: TopTracksAdapter.TopTracksViewHolder, p1: Int) {
            val song = tracksList[p1]
            val songImage = song.getArtistPictureURL()//TODO: Getting the image of the artist
            if (songImage != ""){
                Picasso.with(this@whatsNew.context).load(songImage).into(p0.image) //TODO: why is this not working
            }
            p0.songTitle.text = song.getSongTitle()
            p0.row.setOnClickListener{
                val intent = Intent(this@whatsNew.parentContext, AddSongActivity::class.java)
                intent.putExtra("song_title", song.getSongTitle())
                intent.putExtra("song_artist", song.getArtist()) //TODO: Put artist
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return tracksList.size
        }

        inner class TopTracksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val row = itemView
            var image = itemView.artist_img
            var songTitle:TextView = itemView.song_title //TODO:?????
        }
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
//    interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        fun onFragmentInteraction(uri: Uri)
//    }

}

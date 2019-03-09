package com.example.playlistapp.viewmodel

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast

import com.example.playlistapp.R
import com.example.playlistapp.ResultList
import com.example.playlistapp.whatsNew
import kotlinx.android.synthetic.main.fragment_search.*


@SuppressLint("ValidFragment")
class Search(context: Context) : Fragment() {
    val parentContext = context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onStart() {
        super.onStart()

        //put the whats new fragment inside of this

        val fm = fragmentManager

        // add
        val fragment = whatsNew(this.parentContext)
        val ft = fm?.beginTransaction()
        ft?.replace(R.id.frag_placehoder, fragment, "RESULTS_FRAG") //TODO: is this right????
        ft?.commit()

        //TODO:Problem with action id
        input_artist_search.setOnEditorActionListener { _, actionId, _ ->
            Log.d("in editor listener", "VVVV action id VVVV")
            Log.d("in editor listener", actionId.toString())
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchText = input_artist_search.text
                Log.d("in editor listener", searchText.toString())
                input_artist_search.setText("")
                if (searchText.toString() == "") {
                    val toast = Toast.makeText(this.parentContext, "Please enter text", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    return@setOnEditorActionListener true
                }
                else {
                    Log.d("whats new search", searchText.toString())
                    performSearch(searchText.toString())
                    return@setOnEditorActionListener false
                }
            }
            Log.d("in editor listener", "not working")
            return@setOnEditorActionListener false
        }
    }

    private fun performSearch(query: String) {
        // Load Fragment into View
        val fm = fragmentManager

        // add
        val fragment = ResultList(this.parentContext, query)
        val ft = fm?.beginTransaction()
        ft?.replace(R.id.frag_placehoder, fragment, "RESULTS_FRAG") //TODO: is this right????
        ft?.commit()
    }


}

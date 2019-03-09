package com.example.playlistapp.util

import android.text.TextUtils
import android.util.Log
import com.example.playlistapp.model.PlaylistSong
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

import com.example.playlistapp.model.Song
import com.example.playlistapp.model.SongDetail

import kotlin.collections.ArrayList

class QueryUtils {
    companion object {
        private val LogTag = this::class.java.simpleName
        private const val SingleSongBaseURLPrefix = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=&api_key=f288de7c63a93d8936fa3ccae19a89b7&format=json&artist="
        private const val SingleSongBaseURLMiddle ="&track="
        private const val SinglesongBaseURLSuffix ="&format=json"
        private const val TopTracksBaseURL = "http://ws.audioscrobbler.com/2.0/?method=chart.gettoptracks&api_key=f288de7c63a93d8936fa3ccae19a89b7&format=json"
        private const val ArtistTracksBaseURLPrefix ="http://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&artist="
        private const val ArtistTracksBaseURLSuffix="&api_key=f288de7c63a93d8936fa3ccae19a89b7&format=json"

        fun fetchTopTracks(): ArrayList<Song>? { //TODO:For top tracks on whats new frag
            val url: URL? = createUrl("${this.TopTracksBaseURL}")

            var jsonResponse: String? = null
            try {
                jsonResponse = makeHttpRequest(url)
            }
            catch (e: IOException) {
                Log.e(this.LogTag, "Problem making the HTTP request.", e)
            }

            return extractTopTracksFromJson(jsonResponse)
        }

        fun fetchArtistTracks(jsonQueryString: String): ArrayList<Song>? { //TODO: for songs when an artist is searched
            jsonQueryString.replace("\\s+", "+") //put plus signs where spaces are to match api format
            Log.d("fetchArtist input", jsonQueryString)
            val url: URL? = createUrl("${this.ArtistTracksBaseURLPrefix}$jsonQueryString${this.ArtistTracksBaseURLSuffix}")

            var jsonResponse: String? = null
            try {
                jsonResponse = makeHttpRequest(url)
            }
            catch (e: IOException) {
                Log.e(this.LogTag, "Problem making the HTTP request.", e)
            }

            return extractTracksFromJson(jsonResponse)
        }

        //TODO: for the details page

        fun fetchSingleSongData(Artist: String, Song: String): ArrayList<SongDetail>?{
            Artist.replace(" ", "+").toLowerCase()//put plus signs where spaces are to match api format
            Log.d("artist replace", Artist)
            Song.replace(" ", "+").toLowerCase()//put plus signs where spaces are to match api format
            Log.d("song replace", Song)
            Log.d("artist", "hello")

            val url: URL? = createUrl("${this.SingleSongBaseURLPrefix}$Artist${this.SingleSongBaseURLMiddle}$Song${this.SinglesongBaseURLSuffix}")

            var jsonResponse: String? = null
            try {
                jsonResponse = makeHttpRequest(url)
            }
            catch (e: IOException) {
                Log.e(this.LogTag, "Problem making the HTTP request.", e)
            }

            return extractSingleSongDataFromJson(jsonResponse)
        }


        private fun createUrl(stringUrl: String): URL? {
            var url: URL? = null
            try {
                url = URL(stringUrl)
            }
            catch (e: MalformedURLException) {
                Log.e(this.LogTag, "Problem building the URL.", e)
            }

            return url
        }

        private fun makeHttpRequest(url: URL?): String {
            var jsonResponse = ""

            if (url == null) {
                return jsonResponse
            }

            var urlConnection: HttpURLConnection? = null
            var inputStream: InputStream? = null
            try {
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.readTimeout = 10000 // 10 seconds
                urlConnection.connectTimeout = 15000 // 15 seconds
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                if (urlConnection.responseCode == 200) {
                    inputStream = urlConnection.inputStream
                    jsonResponse = readFromStream(inputStream)
                }
                else {
                    Log.e(this.LogTag, "Error response code: ${urlConnection.responseCode}")
                }
            }
            catch (e: IOException) {
                Log.e(this.LogTag, "Problem retrieving the song results: $url", e)
            }
            finally {
                urlConnection?.disconnect()
                inputStream?.close()
            }

            return jsonResponse
        }

        private fun readFromStream(inputStream: InputStream?): String {
            val output = StringBuilder()
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
                val reader = BufferedReader(inputStreamReader)
                var line = reader.readLine()
                while (line != null) {
                    output.append(line)
                    line = reader.readLine()
                }
            }

            return output.toString()
        }

        private fun extractTopTracksFromJson(songJson: String?): ArrayList<Song>? {
            if (TextUtils.isEmpty(songJson)) {
                return null
            }

            val songList = ArrayList<Song>()
            try {
                val baseJsonResponse = JSONObject(songJson).getJSONObject("tracks").getJSONArray("track")
                for (i in 0 until baseJsonResponse.length()) {
                    val songTitle = baseJsonResponse.getJSONObject(i)
                    val artistPictureURL = baseJsonResponse.getJSONObject(i).getJSONArray("image")
                    val songArtist = baseJsonResponse.getJSONObject(i).getJSONObject("artist")
                    songList.add(Song(
                        returnValueOrDefault<String>(songTitle , "name") as String,
                        returnImageURL(artistPictureURL, 2 ),
                        returnValueOrDefault<String>(songArtist, "name") as String))// add the song to the songlist
                }

            }
            catch (e: JSONException) {
                Log.e(this.LogTag, "Problem parsing the product JSON results", e)
            }


            return songList
        }

        private fun extractTracksFromJson(songJson: String?): ArrayList<Song>? {
            if (TextUtils.isEmpty(songJson)) {
                return null
            }

            val songList = ArrayList<Song>()
            try {
                val baseJsonResponse = JSONObject(songJson).getJSONObject("toptracks").getJSONArray("track")
                for (i in 0 until baseJsonResponse.length()) {
                    val songTitle = baseJsonResponse.getJSONObject(i)
                    val artistPictureURL = baseJsonResponse.getJSONObject(i).getJSONArray("image")
                    val artist = baseJsonResponse.getJSONObject(i).getJSONObject("artist")
                    songList.add(Song(
                        returnValueOrDefault<String>(songTitle , "name") as String,
                        returnImageURL(artistPictureURL, 2 ),
                        returnValueOrDefault<String>(artist, "name") as String))// add the song to the songlist
                }

            }
            catch (e: JSONException) {
                Log.e(this.LogTag, "Problem parsing the product JSON results", e)
            }
            for (i in 0 until songList.size){
                Log.d("artist tracks",songList[i].getSongTitle())
            }

            return songList
        }

        private fun extractSingleSongDataFromJson(songJson: String?): ArrayList<SongDetail>? {
            if (TextUtils.isEmpty(songJson)) {
                return null
            }
            val songList: ArrayList<SongDetail> = ArrayList()

            val song = SongDetail("","","", 0, "")
            try {
                Log.d("single song response", JSONObject(songJson).toString())
                val baseJsonResponse = JSONObject(songJson).getJSONObject("track")
                song.setSongTitle(returnValueOrDefault<String>(baseJsonResponse, "name") as String)

                if (baseJsonResponse.has("album")){
                    song.setAlbum(returnValueOrDefault<String>(baseJsonResponse.getJSONObject("album"), "title") as String)
                    song.setSongArtist(returnValueOrDefault<String>(baseJsonResponse.getJSONObject("album"), "artist") as String)
                    song.setPlayCount(returnValueOrDefault<Int>(baseJsonResponse, "playcount") as Int)
                    song.setAlbumArtUrl(returnImageURL(baseJsonResponse.getJSONObject("album").getJSONArray("image"), 3))

                }else{
                    song.setAlbum("")
                    song.setSongArtist(returnValueOrDefault<String>(baseJsonResponse.getJSONObject("artist"), "name") as String)
                    song.setPlayCount(returnValueOrDefault<Int>(baseJsonResponse, "playcount") as Int)
                    song.setAlbumArtUrl("")
                }


                Log.d("extractsinglesongdata",song.toString())
                songList.add(song)

            }
            catch (e: JSONException) {
                Log.e(this.LogTag, "Problem parsing the product JSON results", e)
            }
            Log.d("extractSingleSongData", songList.size.toString())
            return songList
        }



        private inline fun <reified T> returnValueOrDefault(json: JSONObject, key: String): Any? {
            when (T::class) {
                String::class -> {
                    return if (json.has(key)) {
                        json.getString(key)
                    } else {
                        ""
                    }
                }
                Int::class -> {
                    return if (json.has(key)) {
                        json.getInt(key)
                    }
                    else {
                        return -1
                    }
                }
                Double::class -> {
                    return if (json.has(key)) {
                        json.getDouble(key)
                    }
                    else {
                        return -1.0
                    }
                }
                Long::class -> {
                    return if (json.has(key)) {
                        json.getLong(key)
                    }
                    else {
                        return (-1).toLong()
                    }
                }
                JSONObject::class -> {
                    return if (json.has(key)) {
                        json.getJSONObject(key)
                    }
                    else {
                        return null
                    }
                }
                JSONArray::class -> {
                    return if (json.has(key)) {
                        json.getJSONArray(key)
                    }
                    else {
                        return null
                    }
                }
                else -> {
                    return null
                }
            }
        }

        private fun returnImageURL(json: JSONArray, key: Int): String{
           if (json.isNull(key)){
               return ""
           }
            return json.getJSONObject(key).get("#text") as String
        }

    }
}